package io.openems.edge.scheduler.allalphabetically;

import java.util.Collection;

import org.osgi.service.component.ComponentContext;

import io.openems.edge.common.channel.Channel;
import io.openems.edge.controller.api.Controller;

public class DummyController implements Controller {

	private final String id;
	
	public DummyController(String id) {
		this.id = id;
	}
	
	@Override
	public String id() {
		return this.id;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String servicePid() {
		return "";
	}
	
	@Override
	public String factoryPid() {
		return "";
	}

	@Override
	public ComponentContext componentContext() {
		return null;
	}

	@Override
	public Channel<?> _channel(String channelName) {
		return null;
	}

	@Override
	public Collection<Channel<?>> channels() {
		return null;
	}

	@Override
	public void run() {

	}

}
