package io.openems.edge.bridge.communication.remote.rest.api;

import org.osgi.service.cm.ConfigurationException;

import java.util.Map;

public interface RestBridge {
    /**
     * Adds the RestRequest to the tasks map.
     *
     * @param id      identifier == remote device Id usually from Remote Device config
     * @param request the RestRequest created by the Remote Device.
     * @throws ConfigurationException if the id is already in the Map.
     */

    void addRestRequest(String id, RestRequest request) throws ConfigurationException;

    /**
     * removes a Remote device from the Bridge.
     * Usually called by RestRemote Component on deactivation or when the Bridge itself deactivates.
     *
     * @param deviceId the deviceId to Remove.
     */
    void removeRestRemoteDevice(String deviceId);

    /**
     * Is the Connection OK (Test Get request) Not ideal but it works.
     *
     * @return a boolean if connection is Ok.
     */

    boolean connectionOk();
}
