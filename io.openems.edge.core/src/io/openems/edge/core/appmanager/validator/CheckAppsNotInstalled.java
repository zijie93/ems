package io.openems.edge.core.appmanager.validator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.openems.common.session.Language;
import io.openems.edge.core.appmanager.AppManager;
import io.openems.edge.core.appmanager.AppManagerImpl;

@Component(name = CheckAppsNotInstalled.COMPONENT_NAME)
public class CheckAppsNotInstalled extends AbstractCheckable implements Checkable {

	public static final String COMPONENT_NAME = "Validator.Checkable.CheckAppsNotInstalled";

	private final AppManager appManager;
	private String[] appIds;

	private List<String> installedApps = new LinkedList<>();

	@Activate
	public CheckAppsNotInstalled(@Reference AppManager appManager, ComponentContext componentContext) {
		super(componentContext);
		this.appManager = appManager;
	}

	@Override
	public void setProperties(Map<String, ?> properties) {
		this.appIds = (String[]) properties.get("appIds");
	}

	@Override
	public boolean check() {
		this.installedApps = new LinkedList<>();
		var appManagerImpl = this.getAppManagerImpl();
		if (appManagerImpl == null) {
			return false;
		}
		var instances = appManagerImpl.getInstantiatedApps();
		for (String item : this.appIds) {
			if (instances.stream().anyMatch(t -> t.appId.equals(item))) {
				this.installedApps.add(item);
			}
		}
		return this.installedApps.isEmpty();
	}

	private AppManagerImpl getAppManagerImpl() {
		if (this.appManager == null) {
			return null;
		}
		if (!(this.appManager instanceof AppManagerImpl)) {
			return null;
		}
		return (AppManagerImpl) this.appManager;
	}

	@Override
	public String getErrorMessage(Language language) {
		return "Apps with ID[" + this.installedApps.stream().collect(Collectors.joining(", ")) + "] are installed!"
				+ System.lineSeparator() + "Delete them to be able to install this App.";
	}

}
