package org.pipservices.container.config;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;
import org.pipservices.commons.reflect.*;

public class ComponentConfig {
	private Descriptor _descriptor;
	private TypeDescriptor _type;
	private ConfigParams _config;
	
	public ComponentConfig() {}
	
	public ComponentConfig(Descriptor descriptor, TypeDescriptor type, ConfigParams config) {
		_descriptor = descriptor;
		_type = type;
		_config = config;
	}
	
	public Descriptor getDescriptor() { return _descriptor; }
	public void setDescriptor(Descriptor value) { _descriptor = value; }
	
	public TypeDescriptor getType() { return _type; }
	public void setType(TypeDescriptor value) { _type = value; }
	
	public ConfigParams getConfig() { return _config; }
	public void setConfig(ConfigParams value) { _config = value; }
			
	public static ComponentConfig fromConfig(ConfigParams config) throws ConfigException {
		Descriptor descriptor = Descriptor.fromString(config.getAsNullableString("descriptor"));  
		TypeDescriptor type = TypeDescriptor.fromString(config.getAsNullableString("type")); 
		
		if (descriptor == null && type == null)
			throw new ConfigException(null, "BAD_CONFIG", "Component configuration must have descriptor or type");
		
		return new ComponentConfig(descriptor, type, config);
	}
}
