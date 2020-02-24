package io.openems.edge.core.componentmanager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.Manifest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import io.openems.common.OpenemsConstants;
import io.openems.common.channel.Level;
import io.openems.common.exceptions.OpenemsError;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;
import io.openems.common.jsonrpc.base.GenericJsonrpcResponseSuccess;
import io.openems.common.jsonrpc.base.JsonrpcRequest;
import io.openems.common.jsonrpc.base.JsonrpcResponseSuccess;
import io.openems.common.jsonrpc.request.CreateComponentConfigRequest;
import io.openems.common.jsonrpc.request.DeleteComponentConfigRequest;
import io.openems.common.jsonrpc.request.GetEdgeConfigRequest;
import io.openems.common.jsonrpc.request.UpdateComponentConfigRequest;
import io.openems.common.jsonrpc.request.UpdateComponentConfigRequest.Property;
import io.openems.common.jsonrpc.response.GetEdgeConfigResponse;
import io.openems.common.session.Role;
import io.openems.common.session.User;
import io.openems.common.types.EdgeConfig;
import io.openems.common.types.EdgeConfig.Component.Channel.ChannelDetail;
import io.openems.common.types.EdgeConfig.Component.Channel.ChannelDetailOpenemsType;
import io.openems.common.types.EdgeConfig.Component.Channel.ChannelDetailState;
import io.openems.common.types.OptionsEnum;
import io.openems.common.utils.JsonUtils;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.EnumDoc;
import io.openems.edge.common.channel.StateChannel;
import io.openems.edge.common.channel.StateChannelDoc;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.jsonapi.JsonApi;

@Component( //
		name = "Core.ComponentManager", //
		immediate = true, //
		property = { //
				"id=" + OpenemsConstants.COMPONENT_MANAGER_ID, //
				"enabled=true" //
		})
public class ComponentManagerImpl extends AbstractOpenemsComponent
		implements ComponentManager, OpenemsComponent, JsonApi, ConfigurationListener {

	private final Logger log = LoggerFactory.getLogger(ComponentManagerImpl.class);

	private final OsgiValidateWorker osgiValidateWorker;
	private final OutOfMemoryHeapDumpWorker outOfMemoryHeapDumpWorker;
	private final DefaultConfigurationWorker defaultConfigurationWorker;

	private BundleContext bundleContext;

	@Reference
	private MetaTypeService metaTypeService;

	@Reference
	protected ConfigurationAdmin cm;

	@Reference(policy = ReferencePolicy.DYNAMIC, //
			policyOption = ReferencePolicyOption.GREEDY, //
			cardinality = ReferenceCardinality.MULTIPLE, //
			target = "(&(enabled=true)(!(service.factoryPid=Core.ComponentManager)))")
	private volatile List<OpenemsComponent> enabledComponents = new CopyOnWriteArrayList<>();

	@Reference(policy = ReferencePolicy.DYNAMIC, //
			policyOption = ReferencePolicyOption.GREEDY, //
			cardinality = ReferenceCardinality.MULTIPLE, //
			target = "(!(service.factoryPid=Core.ComponentManager))")
	private volatile List<OpenemsComponent> allComponents = new CopyOnWriteArrayList<>();

	public ComponentManagerImpl() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				ComponentManager.ChannelId.values() //
		);
		this.osgiValidateWorker = new OsgiValidateWorker(this);
		this.outOfMemoryHeapDumpWorker = new OutOfMemoryHeapDumpWorker(this);
		this.defaultConfigurationWorker = new DefaultConfigurationWorker(this);
	}

	@Activate
	void activate(ComponentContext componentContext, BundleContext bundleContext) throws OpenemsException {
		super.activate(componentContext, OpenemsConstants.COMPONENT_MANAGER_ID, "Component-Manager", true);

		this.bundleContext = bundleContext;

		// Start OSGi Validate Worker
		this.osgiValidateWorker.activate(this.id());

		// Start the Out-Of-Memory Worker
		this.outOfMemoryHeapDumpWorker.activate(this.id());

		// Start the Default-Configuration Worker
		this.defaultConfigurationWorker.activate(this.id());
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();

		// Stop OSGi Validate Worker
		this.osgiValidateWorker.deactivate();

		// Stop the Out-Of-Memory Worker
		this.outOfMemoryHeapDumpWorker.deactivate();

		// Stop the Default-Configuration Worker
		this.defaultConfigurationWorker.deactivate();
	}

	@Override
	public List<OpenemsComponent> getEnabledComponents() {
		return Collections.unmodifiableList(this.enabledComponents);
	}

	@Override
	public List<OpenemsComponent> getAllComponents() {
		return Collections.unmodifiableList(this.allComponents);
	}

	protected StateChannel configNotActivatedChannel() {
		return this.channel(ComponentManager.ChannelId.CONFIG_NOT_ACTIVATED);
	}

	protected StateChannel defaultConfigurationFailed() {
		return this.channel(ComponentManager.ChannelId.DEFAULT_CONFIGURATION_FAILED);
	}

	@Override
	protected void logInfo(Logger log, String message) {
		super.logInfo(log, message);
	}

	@Override
	protected void logWarn(Logger log, String message) {
		super.logWarn(log, message);
	}

	@Override
	protected void logError(Logger log, String message) {
		super.logError(log, message);
	}

	@Override
	public CompletableFuture<JsonrpcResponseSuccess> handleJsonrpcRequest(User user, JsonrpcRequest request)
			throws OpenemsNamedException {
		user.assertRoleIsAtLeast("handleJsonrpcRequest", Role.GUEST);

		switch (request.getMethod()) {

		case GetEdgeConfigRequest.METHOD:
			return this.handleGetEdgeConfigRequest(user, GetEdgeConfigRequest.from(request));

		case CreateComponentConfigRequest.METHOD:
			return this.handleCreateComponentConfigRequest(user, CreateComponentConfigRequest.from(request));

		case UpdateComponentConfigRequest.METHOD:
			return this.handleUpdateComponentConfigRequest(user, UpdateComponentConfigRequest.from(request));

		case DeleteComponentConfigRequest.METHOD:
			return this.handleDeleteComponentConfigRequest(user, DeleteComponentConfigRequest.from(request));

		default:
			throw OpenemsError.JSONRPC_UNHANDLED_METHOD.exception(request.getMethod());
		}
	}

	/**
	 * Handles a GetEdgeConfigRequest.
	 * 
	 * @param user    the User
	 * @param request the GetEdgeConfigRequest
	 * @return the Future JSON-RPC Response
	 * @throws OpenemsNamedException on error
	 */
	private CompletableFuture<JsonrpcResponseSuccess> handleGetEdgeConfigRequest(User user,
			GetEdgeConfigRequest request) throws OpenemsNamedException {
		EdgeConfig config = this.getEdgeConfig();
		GetEdgeConfigResponse response = new GetEdgeConfigResponse(request.getId(), config);
		return CompletableFuture.completedFuture(response);
	}

	/**
	 * Handles a CreateComponentConfigRequest.
	 * 
	 * @param user    the User
	 * @param request the CreateComponentConfigRequest
	 * @return the Future JSON-RPC Response
	 * @throws OpenemsNamedException on error
	 */
	protected CompletableFuture<JsonrpcResponseSuccess> handleCreateComponentConfigRequest(User user,
			CreateComponentConfigRequest request) throws OpenemsNamedException {
		// Get Component-ID from Request
		String componentId = null;
		for (Property property : request.getProperties()) {
			if (property.getName().equals("id")) {
				componentId = JsonUtils.getAsString(property.getValue());
			}
		}
		if (componentId == null) {
			throw new OpenemsException("Component-ID is missing in " + request.toString());
		}

		// Check that there is currently no Component with the same ID.
		Configuration[] configs;
		try {
			configs = this.cm.listConfigurations("(id=" + componentId + ")");
		} catch (IOException | InvalidSyntaxException e) {
			throw OpenemsError.GENERIC.exception("Unable to list configurations for ID [" + componentId + "]. "
					+ e.getClass().getSimpleName() + ": " + e.getMessage());
		}
		if (configs != null && configs.length > 0) {
			throw new OpenemsException("A Component with id [" + componentId + "] is already existing!");
		}

		Configuration config;
		try {
			config = this.cm.createFactoryConfiguration(request.getFactoryPid(), null);
		} catch (IOException e) {
			e.printStackTrace();
			throw OpenemsError.GENERIC.exception("Unable create Configuration for Factory-ID ["
					+ request.getFactoryPid() + "]. " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}

		// Create map with configuration attributes
		Dictionary<String, Object> properties = new Hashtable<>();
		for (Property property : request.getProperties()) {
			properties.put(property.getName(), JsonUtils.getAsBestType(property.getValue()));
		}

		// Update Configuration
		try {
			this.applyConfiguration(user, config, properties);
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
			throw OpenemsError.EDGE_UNABLE_TO_CREATE_CONFIG.exception(request.getFactoryPid(), e.getMessage());
		}

		return CompletableFuture.completedFuture(new GenericJsonrpcResponseSuccess(request.getId()));
	}

	/**
	 * Handles a UpdateComponentConfigRequest.
	 * 
	 * @param user    the User
	 * @param request the UpdateComponentConfigRequest
	 * @return the Future JSON-RPC Response
	 * @throws OpenemsNamedException on error
	 */
	protected CompletableFuture<JsonrpcResponseSuccess> handleUpdateComponentConfigRequest(User user,
			UpdateComponentConfigRequest request) throws OpenemsNamedException {
		Configuration config = this.getExistingConfigForId(request.getComponentId());

		// Create map with changed configuration attributes
		Dictionary<String, Object> properties = config.getProperties();
		for (Property property : request.getProperties()) {
			// do not allow certain properties to be updated, like pid and service.pid
			if (!EdgeConfig.ignorePropertyKey(property.getName())) {
				Object value = JsonUtils.getAsBestType(property.getValue());
				if (value instanceof Object[] && ((Object[]) value).length == 0) {
					value = new String[0];
				}
				properties.put(property.getName(), value);
			}
		}

		// Update Configuration
		try {
			this.applyConfiguration(user, config, properties);
		} catch (IOException e) {
			e.printStackTrace();
			throw OpenemsError.EDGE_UNABLE_TO_APPLY_CONFIG.exception(request.getComponentId(), e.getMessage());
		}

		return CompletableFuture.completedFuture(new GenericJsonrpcResponseSuccess(request.getId()));
	}

	/**
	 * Handles a DeleteComponentConfigRequest.
	 * 
	 * @param user    the User
	 * @param request the DeleteComponentConfigRequest
	 * @return the Future JSON-RPC Response
	 * @throws OpenemsNamedException on error
	 */
	protected CompletableFuture<JsonrpcResponseSuccess> handleDeleteComponentConfigRequest(User user,
			DeleteComponentConfigRequest request) throws OpenemsNamedException {
		Configuration config = this.getExistingConfigForId(request.getComponentId());

		try {
			config.delete();
		} catch (IOException e) {
			e.printStackTrace();
			throw OpenemsError.EDGE_UNABLE_TO_DELETE_CONFIG.exception(request.getComponentId(), e.getMessage());
		}

		return CompletableFuture.completedFuture(new GenericJsonrpcResponseSuccess(request.getId()));
	}

	/**
	 * Updates the Configuration from the given Properties and adds some meta
	 * information.
	 * 
	 * @param user       the User
	 * @param config     the Configuration object
	 * @param properties the properties
	 * @throws IOException on error
	 */
	private void applyConfiguration(User user, Configuration config, Dictionary<String, Object> properties)
			throws IOException {
		String lastChangeBy;
		if (user != null) {
			lastChangeBy = user.getId() + ": " + user.getName();
		} else {
			lastChangeBy = "UNDEFINED";
		}
		properties.put(OpenemsConstants.PROPERTY_LAST_CHANGE_BY, lastChangeBy);
		properties.put(OpenemsConstants.PROPERTY_LAST_CHANGE_AT,
				LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
		config.update(properties);
	}

	/**
	 * Gets the ConfigAdmin Configuration for the OpenEMS Component with the given
	 * Component-ID.
	 * 
	 * @param componentId the Component-ID
	 * @return the Configuration
	 * @throws OpenemsNamedException on error
	 */
	protected Configuration getExistingConfigForId(String componentId) throws OpenemsNamedException {
		Configuration[] configs;
		try {
			configs = this.cm.listConfigurations("(id=" + componentId + ")");
		} catch (IOException | InvalidSyntaxException e) {
			e.printStackTrace();
			throw OpenemsError.GENERIC.exception("Unable to list configurations for ID [" + componentId + "]. "
					+ e.getClass().getSimpleName() + ": " + e.getMessage());
		}

		// Make sure we only have one config
		if (configs == null || configs.length == 0) {
			throw OpenemsError.EDGE_NO_COMPONENT_WITH_ID.exception(componentId);
		} else if (configs.length > 1) {
			throw OpenemsError.EDGE_MULTIPLE_COMPONENTS_WITH_ID.exception(componentId);
		}
		return configs[0];
	}

	@Override
	public EdgeConfig getEdgeConfig() {
		EdgeConfig result = new EdgeConfig();

		/*
		 * Read Factories
		 */
		final Bundle[] bundles = this.bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			final MetaTypeInformation mti = this.metaTypeService.getMetaTypeInformation(bundle);

			// read Bundle Manifest
			URL manifestUrl = bundle.getResource("META-INF/MANIFEST.MF");
			Manifest manifest;
			try {
				manifest = new Manifest(manifestUrl.openStream());
			} catch (IOException e) {
				// unable to read manifest
				continue;
			}

			// get Factory-PIDs in this Bundle
			String[] factoryPids = mti.getFactoryPids();
			for (String factoryPid : factoryPids) {
				switch (factoryPid) {
				case "osgi.executor.provider":
					// ignore these Factory-PIDs
					break;
				default:
					// Get ObjectClassDefinition (i.e. the main annotation on the Config class)
					ObjectClassDefinition objectClassDefinition = mti.getObjectClassDefinition(factoryPid, null);
					// Get Natures implemented by this Factory-PID
					String[] natures = this.getNatures(bundle, manifest, factoryPid);
					// Add Factory to config
					result.addFactory(factoryPid,
							EdgeConfig.Factory.create(factoryPid, objectClassDefinition, natures));
				}
			}
		}

		/*
		 * Create Components-Map with Component-ID -> Configuration
		 */
		Map<String, Map<String, JsonElement>> componentsMap = new HashMap<>();
		for (OpenemsComponent component : this.allComponents) {
			String componentId = component.id();
			TreeMap<String, JsonElement> propertyMap = new TreeMap<>();
			String factoryPid = component.serviceFactoryPid();
			Dictionary<String, Object> properties = component.getComponentContext().getProperties();

			// get configuration properties
			EdgeConfig.Factory factory = result.getFactories().get(factoryPid);
			Enumeration<String> keys = properties.keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();

				if (!EdgeConfig.ignorePropertyKey(key)) {

					JsonElement value = getPropertyAsJsonElement(properties.get(key));
					if (factory != null) {
						Optional<EdgeConfig.Factory.Property> propertyOpt = factory.getProperty(key);
						if (propertyOpt.isPresent()) {
							EdgeConfig.Factory.Property property = propertyOpt.get();
							// hide Password fields
							if (property.isPassword()) {
								value = new JsonPrimitive("xxx");
							}
						}
					}

					propertyMap.put(key, value);
				}
			}

			// get Alias and Channels
			TreeMap<String, EdgeConfig.Component.Channel> channelMap = new TreeMap<>();
			for (Channel<?> channel : component.channels()) {
				io.openems.edge.common.channel.ChannelId channelId = channel.channelId();
				Doc doc = channelId.doc();
				ChannelDetail detail = null;
				switch (doc.getChannelCategory()) {
				case ENUM: {
					Map<String, JsonElement> values = new HashMap<>();
					EnumDoc d = (EnumDoc) doc;
					for (OptionsEnum option : d.getOptions()) {
						values.put(option.getName(), new JsonPrimitive(option.getValue()));
					}
					detail = new EdgeConfig.Component.Channel.ChannelDetailEnum(values);
					break;
				}
				case OPENEMS_TYPE:
					detail = new ChannelDetailOpenemsType();
					break;
				case STATE:
					StateChannelDoc d = (StateChannelDoc) doc;
					Level level = d.getLevel();
					detail = new ChannelDetailState(level);
					break;
				}
				channelMap.put(channelId.id(), new EdgeConfig.Component.Channel(//
						channelId.id(), //
						doc.getType(), //
						doc.getAccessMode(), //
						doc.getText(), //
						doc.getUnit(), //
						detail //
				));
			}
			// Create EdgeConfig.Component and add it to Result
			result.addComponent(componentId, new EdgeConfig.Component(componentId, component.alias(),
					component.isEnabled(), factoryPid, propertyMap, channelMap));
		}
		// Add myself
		componentsMap.put(this.id(), null);

		return result;

	}

	/**
	 * Reads Natures from an XML:
	 * 
	 * <pre>
	 * <scr:component>
	 *   <service>
	 *     <provide interface="...">
	 *   </service>
	 * </scr:component>
	 * </pre>
	 * 
	 * @return
	 */
	private String[] getNatures(Bundle bundle, Manifest manifest, String factoryPid) {
		try {
			// get "Service-Component"-Entry of Manifest
			String serviceComponentsString = manifest.getMainAttributes()
					.getValue(ComponentConstants.SERVICE_COMPONENT);
			if (serviceComponentsString == null) {
				return new String[0];
			}
			String[] serviceComponents = serviceComponentsString.split(",");

			// read Service-Component XML files from OSGI-INF folder
			for (String serviceComponent : serviceComponents) {
				if (!serviceComponent.contains(factoryPid)) {
					// search for correct XML file
					continue;
				}

				URL componentUrl = bundle.getResource(serviceComponent);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(componentUrl.openStream());
				doc.getDocumentElement().normalize();

				NodeList serviceNodes = doc.getElementsByTagName("service");
				for (int i = 0; i < serviceNodes.getLength(); i++) {
					Node serviceNode = serviceNodes.item(i);
					if (serviceNode.getNodeType() == Node.ELEMENT_NODE) {
						NodeList provideNodes = serviceNode.getChildNodes();

						// Read "interface" attributes and return them
						Set<String> result = new HashSet<>();
						for (int j = 0; j < provideNodes.getLength(); j++) {
							Node provideNode = provideNodes.item(j);
							NamedNodeMap attributes = provideNode.getAttributes();
							if (attributes != null) {
								Node interfaceNode = attributes.getNamedItem("interface");
								String nature = interfaceNode.getNodeValue();
								switch (nature) {
								case "org.osgi.service.event.EventHandler":
								case "org.ops4j.pax.logging.spi.PaxAppender":
									// ignore these natures;
									break;
								default:
									result.add(nature);
								}
							}
						}
						return result.toArray(new String[result.size()]);
					}
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			this.logWarn(this.log, "Unable to get Natures. " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
		return new String[0];
	}

	@Override
	public void configurationEvent(ConfigurationEvent event) {
		// trigger immediate validation on configuration event
		this.osgiValidateWorker.triggerNextRun();
	}

	/**
	 * Gets a component Property as JsonElement. Uses some more techniques to find
	 * the proper type than {@link JsonUtils#getAsJsonElement(Object)}.
	 * 
	 * @param value the property value
	 * @return the value as JsonElement
	 */
	private static JsonElement getPropertyAsJsonElement(Object valueObj) {
		if (valueObj != null && valueObj instanceof String) {
			String value = (String) valueObj;
			// find boolean
			if (value.equalsIgnoreCase("true")) {
				return new JsonPrimitive(true);
			} else if (value.equalsIgnoreCase("false")) {
				return new JsonPrimitive(false);
			}
		}
		// falback to JsonUtils
		return JsonUtils.getAsJsonElement(valueObj);
	}
}
