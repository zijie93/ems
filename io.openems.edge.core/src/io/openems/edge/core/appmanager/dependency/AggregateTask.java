package io.openems.edge.core.appmanager.dependency;

import java.util.List;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.user.User;
import io.openems.edge.core.appmanager.AppConfiguration;

public interface AggregateTask {

//	public boolean shouldAggregate(DependencyConfig instance);

	public void aggregate(AppConfiguration instance, AppConfiguration oldConfig) throws OpenemsNamedException;

	public void create(User user, List<AppConfiguration> otherAppConfigurations) throws OpenemsNamedException;

//	public abstract void update() throws OpenemsException;
	public void delete(User user, List<AppConfiguration> otherAppConfigurations) throws OpenemsNamedException;

}
