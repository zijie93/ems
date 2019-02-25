package io.openems.edge.controller.chp.soc;

import java.util.Optional;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;
import io.openems.common.types.ChannelAddress;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.WriteChannel;
import io.openems.edge.common.channel.doc.Doc;
import io.openems.edge.common.channel.doc.Level;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.type.TypeUtils;
import io.openems.edge.controller.api.Controller;

@Designate(ocd = Config.class, factory = true)
@Component(name = "Controller.chp.soc", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)

public class ControllerChpSoc extends AbstractOpenemsComponent implements Controller, OpenemsComponent {

	private final Logger log = LoggerFactory.getLogger(ControllerChpSoc.class);

	@Reference
	protected ComponentManager componentManager;

	public ControllerChpSoc() {
		Utils.initializeChannels(this).forEach(channel -> this.addChannel(channel));
	}

	private ChannelAddress inputChannelAddress;
	private ChannelAddress outputChannelAddress;
	private int lowThreshold = 0;
	private int highThreshold = 0;

	public enum ChannelId implements io.openems.edge.common.channel.doc.ChannelId {
		STATE_MACHINE(new Doc() //
				.level(Level.INFO) //
				.text("Current State of State-Machine") //
				.options(State.values()));

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsNamedException {
		/*
		 * parse config
		 */
		this.lowThreshold = config.lowThreshold();
		this.highThreshold = config.highThreshold();
		this.inputChannelAddress = ChannelAddress.fromString(config.inputChannelAddress());
		this.outputChannelAddress = ChannelAddress.fromString(config.outputChannelAddress());

		super.activate(context, config.id(), config.enabled());
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	/**
	 * The current state in the State Machine
	 */
	private State state = State.UNDEFINED;

	@Override
	public void run() throws IllegalArgumentException, OpenemsNamedException {

		int value;
		try {
			Channel<?> inputChannel = this.componentManager.getChannel(this.inputChannelAddress);
			value = TypeUtils.getAsType(OpenemsType.INTEGER, inputChannel.value().getOrError());
		} catch (Exception e) {
			this.logError(this.log, e.getClass().getSimpleName() + ": " + e.getMessage());
			return;
		}

		switch (this.state) {
		case UNDEFINED:
			if (value <= this.lowThreshold) {
				this.state = State.ON;
			} else if (value >= this.highThreshold) {
				this.state = State.OFF;
			} else {
				this.state = State.UNDEFINED;
			}
			break;
		case ON:
			/*
			 * If the value is larger than highThreshold signal OFF
			 */
			if (value >= this.highThreshold) {
				this.state = State.OFF;
				break;
			}
			/*
			 * If the value is larger than lowThreshold and smaller than highThreshold, do
			 * not signal anything.
			 */
			if (this.lowThreshold <= value && value <= this.highThreshold) {
				this.state = State.UNDEFINED;
				break; // do nothing
			}
			this.on();
			break;
		case OFF:
			/*
			 * If the value is smaller than lowThreshold signal ON
			 */
			if (value <= this.lowThreshold) {
				this.state = State.ON;
				break;
			}
			/*
			 * If the value is larger than lowThreshold and smaller than highThreshold, do
			 * not signal anything.
			 */
			if (this.lowThreshold <= value && value <= this.highThreshold) {
				this.state = State.UNDEFINED;
				break; // do nothing
			}
			this.off();
			break;
		}

	}

	/**
	 * Switch the output ON.
	 * 
	 * @throws OpenemsNamedException
	 * @throws IllegalArgumentException
	 */
	private void on() throws IllegalArgumentException, OpenemsNamedException {
		this.setOutput(true);
	}

	/**
	 * Switch the output OFF.
	 * 
	 * @throws OpenemsNamedException
	 * @throws IllegalArgumentException
	 */
	private void off() throws IllegalArgumentException, OpenemsNamedException {
		this.setOutput(false);
	}

	/**
	 * Helper function to switch an output if it was not switched before.
	 *
	 * @param value true to switch ON, false to switch OFF;
	 * @throws OpenemsNamedException    on error
	 * @throws IllegalArgumentException on error
	 */
	private void setOutput(Boolean value) throws IllegalArgumentException, OpenemsNamedException {
		try {
			WriteChannel<Boolean> outputChannel = this.componentManager.getChannel(this.outputChannelAddress);
			Optional<Boolean> currentValueOpt = outputChannel.value().asOptional();
			if (currentValueOpt.isPresent()) {
				this.logInfo(this.log, "Set output [" + outputChannel.address() + "] " + (value) + ".");
				outputChannel.setNextWriteValue(value);
			}
		} catch (OpenemsException e) {
			this.logError(this.log, "Unable to set output: [" + this.outputChannelAddress + "] " + e.getMessage());
		}
	}
}
