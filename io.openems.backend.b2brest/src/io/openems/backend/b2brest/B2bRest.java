package io.openems.backend.b2brest;

import io.openems.common.accesscontrol.AccessControl;
import org.eclipse.jetty.server.Server;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.backend.common.component.AbstractOpenemsBackendComponent;
import io.openems.backend.edgewebsocket.api.EdgeWebsocket;
import io.openems.backend.metadata.api.Metadata;
import io.openems.backend.timedata.api.Timedata;
import io.openems.common.exceptions.OpenemsException;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Backend2Backend.Rest", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE //
)
public class B2bRest extends AbstractOpenemsBackendComponent {

	public static final int DEFAULT_PORT = 8075;

	private final Logger log = LoggerFactory.getLogger(B2bRest.class);

	private Server server = null;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	protected volatile EdgeWebsocket edgeWebsocket;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	protected volatile Metadata metadata;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	protected volatile Timedata timeData;

	@Reference
	protected volatile AccessControl accessControl;

	public B2bRest() {
		super("Backend2Backend.Rest");
	}

	@Activate
	void activate(Config config) throws OpenemsException {
		this.startServer(config.port());
	}

	@Deactivate
	void deactivate() {
		this.stopServer();
	}

	/**
	 * Create and start new server.
	 * 
	 * @param port the port
	 * @throws OpenemsException on error
	 */
	private synchronized void startServer(int port) throws OpenemsException {
		try {
			this.server = new Server(port);
			this.server.setHandler(new RestHandler(this));
			this.server.start();
			this.logInfo(this.log, "Backend2Backend.Rest started on port [" + port + "].");
		} catch (Exception e) {
			throw new OpenemsException("Backend2Backend.Rest failed on port [" + port + "].", e);
		}
	}

	/**
	 * Stop existing server.
	 */
	private synchronized void stopServer() {
		if (this.server != null) {
			try {
				this.server.stop();
			} catch (Exception e) {
				this.logWarn(this.log, "Backend2Backend.Rest failed to stop: " + e.getMessage());
			}
		}
	}

	@Override
	protected void logInfo(Logger log, String message) {
		super.logInfo(log, message);
	}

	@Override
	protected void logWarn(Logger log, String message) {
		super.logWarn(log, message);
	}

}
