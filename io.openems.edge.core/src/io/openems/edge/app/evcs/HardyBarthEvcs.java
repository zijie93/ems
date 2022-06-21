package io.openems.edge.app.evcs;

import java.util.EnumMap;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.function.ThrowingTriFunction;
import io.openems.common.session.Language;
import io.openems.common.utils.EnumUtils;
import io.openems.common.utils.JsonUtils;
import io.openems.edge.app.evcs.HardyBarthEvcs.Property;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.core.appmanager.AbstractOpenemsApp;
import io.openems.edge.core.appmanager.AppAssistant;
import io.openems.edge.core.appmanager.AppConfiguration;
import io.openems.edge.core.appmanager.AppDescriptor;
import io.openems.edge.core.appmanager.ComponentUtil;
import io.openems.edge.core.appmanager.ConfigurationTarget;
import io.openems.edge.core.appmanager.DefaultEnum;
import io.openems.edge.core.appmanager.JsonFormlyUtil;
import io.openems.edge.core.appmanager.JsonFormlyUtil.InputBuilder.Validation;
import io.openems.edge.core.appmanager.OpenemsApp;
import io.openems.edge.core.appmanager.OpenemsAppCardinality;

/**
 * Describes a Hardy Barth evcs App.
 *
 * <pre>
  {
    "appId":"App.Evcs.HardyBarth",
    "alias":"eCharge Hardy Barth Ladestation",
    "instanceId": UUID,
    "image": base64,
    "properties":{
      "EVCS_ID": "evcs0",
      "CTRL_EVCS_ID": "ctrlEvcs0",
      "IP":"192.168.25.30"
    },
    "appDescriptor": {
    	"websiteUrl": <a href=
"https://fenecon.de/fems-app-echarge-hardy-barth-ladestation/">https://fenecon.de/fems-app-echarge-hardy-barth-ladestation/</a>
    }
  }
 * </pre>
 */
@Component(name = "App.Evcs.HardyBarth")
public class HardyBarthEvcs extends AbstractEvcsApp<Property> implements OpenemsApp {

	public static enum Property implements DefaultEnum {
		ALIAS("eCharge Hardy Barth Ladestation"), //
		EVCS_ID("evcs0"), //
		CTRL_EVCS_ID("ctrlEvcs0"), //
		IP("192.168.25.30");

		private final String defaultValue;

		private Property(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		@Override
		public String getDefaultValue() {
			return this.defaultValue;
		}

	}

	@Activate
	public HardyBarthEvcs(@Reference ComponentManager componentManager, ComponentContext componentContext,
			@Reference ConfigurationAdmin cm, @Reference ComponentUtil componentUtil) {
		super(componentManager, componentContext, cm, componentUtil);
	}

	@Override
	protected ThrowingTriFunction<ConfigurationTarget, EnumMap<Property, JsonElement>, Language, AppConfiguration, OpenemsNamedException> appConfigurationFactory() {
		return (t, p, l) -> {
			// values the user enters
			var ip = EnumUtils.getAsOptionalString(p, Property.IP).orElse(Property.IP.getDefaultValue());
			var alias = this.getValueOrDefault(p, Property.ALIAS, this.getName(l));

			// values which are being auto generated by the appmanager
			var evcsId = this.getId(t, p, Property.EVCS_ID);
			var ctrlEvcsId = this.getId(t, p, Property.CTRL_EVCS_ID);

			var components = this.getComponents(evcsId, alias, "Evcs.HardyBarth", ip, ctrlEvcsId);

			return new AppConfiguration(components, Lists.newArrayList(ctrlEvcsId, "ctrlBalancing0"),
					ip.startsWith("192.168.25.") ? Lists.newArrayList("192.168.25.10/24") : null);
		};
	}

	@Override
	public AppAssistant getAppAssistant(Language language) {
		var bundle = AbstractOpenemsApp.getTranslationBundle(language);
		return AppAssistant.create(this.getName(language)) //
				.fields(JsonUtils.buildJsonArray() //
						.add(JsonFormlyUtil.buildInput(Property.IP) //
								.setLabel(bundle.getString("ipAddress")) //
								.setDescription(bundle.getString(this.getAppId() + ".Ip.description"))
								.setDefaultValue(Property.IP.getDefaultValue()) //
								.isRequired(true) //
								.setValidation(Validation.IP) //
								.build()) //
						.build()) //
				.build();
	}

	@Override
	public AppDescriptor getAppDescriptor() {
		return AppDescriptor.create() //
				.build();
	}

	@Override
	protected Class<Property> getPropertyClass() {
		return Property.class;
	}

	@Override
	public OpenemsAppCardinality getCardinality() {
		return OpenemsAppCardinality.MULTIPLE;
	}

}
