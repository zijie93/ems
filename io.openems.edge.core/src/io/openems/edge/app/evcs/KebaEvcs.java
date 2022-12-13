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
import io.openems.common.utils.JsonUtils;
import io.openems.edge.app.evcs.KebaEvcs.Property;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.core.appmanager.AbstractOpenemsApp;
import io.openems.edge.core.appmanager.AppAssistant;
import io.openems.edge.core.appmanager.AppConfiguration;
import io.openems.edge.core.appmanager.AppDescriptor;
import io.openems.edge.core.appmanager.ComponentUtil;
import io.openems.edge.core.appmanager.ConfigurationTarget;
import io.openems.edge.core.appmanager.DefaultEnum;
import io.openems.edge.core.appmanager.InterfaceConfiguration;
import io.openems.edge.core.appmanager.JsonFormlyUtil;
import io.openems.edge.core.appmanager.JsonFormlyUtil.InputBuilder.Validation;
import io.openems.edge.core.appmanager.OpenemsApp;
import io.openems.edge.core.appmanager.OpenemsAppCardinality;
import io.openems.edge.core.appmanager.TranslationUtil;

/**
 * Describes a Keba evcs App.
 *
 * <pre>
  {
    "appId":"App.Evcs.Keba",
    "alias":"KEBA Ladestation",
    "instanceId": UUID,
    "image": base64,
    "properties":{
      "EVCS_ID": "evcs0",
      "CTRL_EVCS_ID": "ctrlEvcs0",
      "IP":"192.168.25.11"
    },
    "appDescriptor": {
    	"websiteUrl": {@link AppDescriptor#getWebsiteUrl()}
    }
  }
 * </pre>
 */
@Component(name = "App.Evcs.Keba")
public class KebaEvcs extends AbstractEvcsApp<Property> implements OpenemsApp {

	public enum Property implements DefaultEnum {
		// Component-IDs
		EVCS_ID("evcs0"), //
		CTRL_EVCS_ID("ctrlEvcs0"), //
		// Properties
		ALIAS("KEBA Ladestation"), //
		IP("192.168.25.11") //
		;

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
	public KebaEvcs(@Reference ComponentManager componentManager, ComponentContext componentContext,
			@Reference ConfigurationAdmin cm, @Reference ComponentUtil componentUtil) {
		super(componentManager, componentContext, cm, componentUtil);
	}

	@Override
	protected ThrowingTriFunction<ConfigurationTarget, EnumMap<Property, JsonElement>, Language, AppConfiguration, OpenemsNamedException> appConfigurationFactory() {
		return (t, p, l) -> {
			// values the user enters
			var ip = this.getValueOrDefault(p, Property.IP);
			var alias = this.getValueOrDefault(p, Property.ALIAS, this.getName(l));

			// values which are being auto generated by the appmanager
			var evcsId = this.getId(t, p, Property.EVCS_ID);
			var ctrlEvcsId = this.getId(t, p, Property.CTRL_EVCS_ID);

			var components = this.getComponents(evcsId, alias, "Evcs.Keba.KeContact", ip, ctrlEvcsId);

			var ips = Lists.newArrayList(//
					new InterfaceConfiguration("eth0") //
							.addIp("Evcs", "192.168.25.10/24") //
			);

			return new AppConfiguration(//
					components, //
					Lists.newArrayList(ctrlEvcsId, "ctrlBalancing0"), //
					ip.startsWith("192.168.25.") ? ips : null //
			);
		};
	}

	@Override
	public AppAssistant getAppAssistant(Language language) {
		var bundle = AbstractOpenemsApp.getTranslationBundle(language);
		return AppAssistant.create(this.getName(language)) //
				.fields(JsonUtils.buildJsonArray() //
						.add(JsonFormlyUtil.buildInput(Property.IP) //
								.setLabel(TranslationUtil.getTranslation(bundle, "ipAddress")) //
								.setDescription(
										TranslationUtil.getTranslation(bundle, this.getAppId() + ".ip.description"))
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
