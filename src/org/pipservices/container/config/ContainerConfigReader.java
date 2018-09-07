package org.pipservices.container.config;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.components.config.*;

public class ContainerConfigReader {

	public static ContainerConfig readFromFile(String correlationId, String path, ConfigParams parameters) throws ApplicationException {
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

    public static ContainerConfig readFromJsonFile(String correlationId, String path, ConfigParams parameters) throws ApplicationException {
    	ConfigParams config = JsonConfigReader.readConfig(correlationId, path, parameters);
    	return ContainerConfig.fromConfig(config);
    }

    public static ContainerConfig readFromYamlFile(String correlationId, String path, ConfigParams parameters) throws ApplicationException {
    	ConfigParams config = YamlConfigReader.readConfig(correlationId, path, parameters);
    	return ContainerConfig.fromConfig(config);
    }
}
