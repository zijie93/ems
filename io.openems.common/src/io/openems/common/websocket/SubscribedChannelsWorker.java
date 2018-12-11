package io.openems.common.websocket;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

import io.openems.common.exceptions.OpenemsException;
import io.openems.common.jsonrpc.base.JsonrpcNotification;
import io.openems.common.jsonrpc.notification.CurrentData;
import io.openems.common.types.ChannelAddress;

public abstract class SubscribedChannelsWorker {

	protected final static int UPDATE_INTERVAL_IN_SECONDS = 2;

	private final Logger log = LoggerFactory.getLogger(SubscribedChannelsWorker.class);

	/**
	 * Executor for subscriptions task
	 */
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	/**
	 * Holds subscribed channels
	 */
	private final TreeSet<ChannelAddress> channels = new TreeSet<>();

	/**
	 * Holds the scheduled task for currentData
	 */
	private Optional<ScheduledFuture<?>> futureOpt = Optional.empty();

	protected final WsData wsData;

	public SubscribedChannelsWorker(WsData wsData) {
		this.wsData = wsData;
	}

	public synchronized void setChannels(Set<ChannelAddress> channels) {
		// stop current thread
		if (this.futureOpt.isPresent()) {
			this.futureOpt.get().cancel(true);
			this.futureOpt = Optional.empty();
		}

		// clear existing channels
		this.channels.clear();

		// set new channels
		this.channels.addAll(channels);

		if (!channels.isEmpty()) {
			// registered channels -> create new thread
			this.futureOpt = Optional.of(this.executor.scheduleWithFixedDelay(() -> {
				/*
				 * This task is executed regularly. Sends data to Websocket.
				 */
				WebSocket ws = this.wsData.getWebsocket();
				if (ws == null || !ws.isOpen()) {
					// disconnected; stop worker
					this.dispose();
					return;
				}

				try {
					this.wsData.send(this.getJsonRpcNotification(this.getCurrentData()));
				} catch (OpenemsException e) {
					this.log.warn("Unable to send SubscribedChannels: " + e.getMessage());
				}

			}, 0, UPDATE_INTERVAL_IN_SECONDS, TimeUnit.SECONDS));
		}
	}

	public void dispose() {
		// unsubscribe regular task
		if (this.futureOpt.isPresent()) {
			futureOpt.get().cancel(true);
		}
	}

	/**
	 * Gets a JSON-RPC Notification with all subscribed channels data
	 *
	 * @return
	 */
	private CurrentData getCurrentData() {
		CurrentData result = new CurrentData();
		for (ChannelAddress channel : this.channels) {
			JsonElement value = this.getChannelValue(channel);
			result.add(channel, value);
		}
		return result;
	}

	protected abstract JsonElement getChannelValue(ChannelAddress channelAddress);

	protected abstract JsonrpcNotification getJsonRpcNotification(CurrentData currentData);
}
