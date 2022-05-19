package org.pipservices3.container.config;

import org.pipservices3.commons.config.*;
import org.pipservices3.commons.errors.*;
import org.pipservices3.components.config.*;

/**
 * Helper class that reads container configuration from JSON or YAML file.
 */
public class ContainerConfigReader {

    /**
     * Reads container configuration from JSON or YAML file. The type of the file is
     * determined by file extension.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param path          a path to component configuration file.
     * @param parameters    values to parameters the configuration or null to skip
     *                      parameterization.
     * @return the read container configuration
     * @throws ApplicationException when error occured.
     */
    public static ContainerConfig readFromFile(String correlationId, String path, ConfigParams parameters)
            throws ApplicationException {
        if (path == null)
            throw new ConfigException(correlationId, "NO_PATH", "Missing config file path");

        int index = path.lastIndexOf('.');
        String ext = index > 0 ? path.substring(index + 1).toLowerCase() : "";

        if (ext.equals("json"))
            return readFromJsonFile(correlationId, path, parameters);
        else if (ext.equals("yaml") || ext.equals("yml"))
            return readFromYamlFile(correlationId, path, parameters);

        // By default read as JSON
        return readFromJsonFile(correlationId, path, parameters);
    }

    /**
     * Reads container configuration from JSON file.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param path          a path to component configuration file.
     * @param parameters    values to parameters the configuration or null to skip
     *                      parameterization.
     * @return the read container configuration.
     * @throws ApplicationException when error occured.
     */
    public static ContainerConfig readFromJsonFile(String correlationId, String path, ConfigParams parameters)
            throws ApplicationException {
        ConfigParams config = JsonConfigReader.readConfig(correlationId, path, parameters);
        return ContainerConfig.fromConfig(config);
    }

    /**
     * Reads container configuration from YAML file.
     *
     * @param correlationId (optional) transaction id to trace execution through
     *                      call chain.
     * @param path          a path to component configuration file.
     * @param parameters    values to parameters the configuration or null to skip
     *                      parameterization.
     * @return the read container configuration.
     * @throws ApplicationException when error occured.
     */
    public static ContainerConfig readFromYamlFile(String correlationId, String path, ConfigParams parameters)
            throws ApplicationException {
        ConfigParams config = YamlConfigReader.readConfig(correlationId, path, parameters);
        return ContainerConfig.fromConfig(config);
    }
}
