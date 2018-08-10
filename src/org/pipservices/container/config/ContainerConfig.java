package org.pipservices.container.config;

import java.util.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;

public class ContainerConfig extends ArrayList<ComponentConfig> {
	private static final long serialVersionUID = -1686520964953606299L;

	public ContainerConfig() {}
	
	public ContainerConfig(Collection<ComponentConfig> components) {
		if (components != null)
			super.addAll(components);
	}
	
	public static ContainerConfig fromValue(Object value) throws ConfigException {
		ConfigParams config = ConfigParams.fromValue(value);
		return fromConfig(config);
	}

	public static ContainerConfig fromConfig(ConfigParams config) throws ConfigException {
		ContainerConfig result = new ContainerConfig();
		if (config == null) return result;
		
		for (String section : config.getSectionNames()) {
			ConfigParams componentConfig = config.getSection(section);
			result.add(ComponentConfig.fromConfig(componentConfig));
		}
		
		return result;
	}
}
