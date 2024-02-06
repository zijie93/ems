package io.openems.edge.controller.api.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.mqttv5.client.IMqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.event.propertytypes.EventTopics;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.EdgeConfig;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.controller.api.Controller;
import io.openems.edge.timedata.api.Timedata;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Controller.Api.MQTT", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE //
)
@EventTopics({ //
		EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE, //
		EdgeEventConstants.TOPIC_CONFIG_UPDATE //
})
public class ControllerApiMqttImpl extends AbstractOpenemsComponent
		implements ControllerApiMqtt, Controller, OpenemsComponent, EventHandler {

	protected static final String COMPONENT_NAME = "Controller.Api.MQTT";
	private ScheduledExecutorService scheduledExecutorService;

	private final Logger log = LoggerFactory.getLogger(ControllerApiMqttImpl.class);
	private final SendChannelValuesWorker sendChannelValuesWorker = new SendChannelValuesWorker(this);
	private final MqttConnector mqttConnector = new MqttConnector();

	@Reference(policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.OPTIONAL)
	private volatile Timedata timedata = null;

	@Reference
	protected ComponentManager componentManager;

	protected Config config;

	private String topicPrefix;
	private IMqttClient mqttClient = null;

	public ControllerApiMqttImpl() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Controller.ChannelId.values(), //
				ControllerApiMqtt.ChannelId.values() //
		);
	}

	@Activate
	private void activate(ComponentContext context, Config config) throws Exception {
	    this.config = config;
	    this.topicPrefix = String.format(ControllerApiMqtt.TOPIC_PREFIX, config.clientId());

	    super.activate(context, config.id(), config.alias(), config.enabled());

	    this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	    attemptConnect();

	    this.scheduledExecutorService.scheduleAtFixedRate(this::attemptConnect, 60, 60, TimeUnit.SECONDS);
	}

	private final Object connectLock = new Object();

	private void attemptConnect() {
	    synchronized (connectLock) {
	        try {
	            if (mqttClient == null || !mqttClient.isConnected()) {
	                this.mqttConnector.connect(config.uri(), config.clientId(), config.username(), config.password(),
	                    config.certPem(), config.privateKeyPem(), config.trustStorePem()).thenAccept(client -> {
	                        mqttClient = client;
	                        logInfo(this.log, "Connected to MQTT Broker [" + config.uri() + "]");
	                    }).exceptionally(ex -> {
	                        log.error("Failed to connect to MQTT broker: " + ex.getMessage(), ex);
	                        return null;
	                    });
	            }
	        } catch (Exception e) {
	            log.error("Error attempting to connect to MQTT broker", e);
	        }
	    }
	}



	@Override
	@Deactivate
	protected void deactivate() {
	    super.deactivate();
	    if (this.scheduledExecutorService != null && !this.scheduledExecutorService.isShutdown()) {
	        this.scheduledExecutorService.shutdownNow();
	    }
	    if (this.mqttClient != null) {
	        try {
	            this.mqttClient.disconnect();
	            this.mqttClient.close();
	        } catch (MqttException e) {
	            this.logWarn(this.log, "Unable to close connection to MQTT broker: " + e.getMessage());
	        }
	    }
	}

	@Override
	public void run() throws OpenemsNamedException {
		// nothing to do here
	}

	@Override
	protected void logInfo(Logger log, String message) {
		super.logInfo(log, message);
	}

	@Override
	protected void logWarn(Logger log, String message) {
		super.logInfo(log, message);
	}

	@Override
	public void handleEvent(Event event) {
		if (!this.isEnabled()) {
			return;
		}
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE:
			this.sendChannelValuesWorker.collectData();
			break;

		case EdgeEventConstants.TOPIC_CONFIG_UPDATE:
			// Send new EdgeConfig
			var config = (EdgeConfig) event.getProperty(EdgeEventConstants.TOPIC_CONFIG_UPDATE_KEY);
			this.publish(ControllerApiMqtt.TOPIC_EDGE_CONFIG, config.toJson().toString(), //
					1 /* QOS */, true /* retain */, new MqttProperties() /* no specific properties */);

			// Trigger sending of all channel values, because a Component might have
			// disappeared
			this.sendChannelValuesWorker.sendValuesOfAllChannelsOnce();
		}
	}

	/**
	 * Publish a message to a topic.
	 *
	 * @param subTopic the MQTT topic. The global MQTT Topic prefix is added in
	 *                 front of this string
	 * @param message  the message
	 * @return true if message was successfully published; false otherwise
	 */
	protected boolean publish(String subTopic, MqttMessage message) {
		var mqttClient = this.mqttClient;
		if (mqttClient == null) {
			return false;
		}
		try {
			mqttClient.publish(this.topicPrefix + subTopic, message);
			return true;
		} catch (MqttException e) {
			this.logWarn(this.log, e.getMessage());
			return false;
		}
	}

	/**
	 * Publish a message to a topic.
	 *
	 * @param subTopic   the MQTT topic. The global MQTT Topic prefix is added in
	 *                   front of this string
	 * @param message    the message; internally translated to a UTF-8 byte array
	 * @param qos        the MQTT QOS
	 * @param retained   the MQTT retained parameter
	 * @param properties the {@link MqttProperties}
	 * @return true if message was successfully published; false otherwise
	 */
	protected boolean publish(String subTopic, String message, int qos, boolean retained, MqttProperties properties) {
		var msg = new MqttMessage(message.getBytes(StandardCharsets.UTF_8), qos, retained, properties);
		return this.publish(subTopic, msg);
	}
}
