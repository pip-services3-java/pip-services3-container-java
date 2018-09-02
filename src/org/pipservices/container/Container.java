package org.pipservices.container;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.components.info.*;
import org.pipservices.components.log.*;
import org.pipservices.commons.refer.*;
import org.pipservices.commons.run.*;
import org.pipservices.container.build.*;
import org.pipservices.container.config.*;
import org.pipservices.container.refer.*;

public class Container implements IConfigurable, IReferenceable, IUnreferenceable, IOpenable {
	protected ILogger _logger = new NullLogger();
	protected DefaultContainerFactory _factories = new DefaultContainerFactory();
    protected ContextInfo _info;
    protected ContainerConfig _config;
    protected ContainerReferences _references;
    
    public Container(String name, String description) {
    	_info = new ContextInfo(name, description);
    }

	public Container(ContainerConfig config) {
        _config = config;
    }

	public void configure(ConfigParams config) throws ConfigException {
		_config = ContainerConfig.fromConfig(config);
	}
	
    public void readConfigFromFile(String correlationId, String path, ConfigParams parameters) throws ApplicationException {
    	_config = ContainerConfigReader.readFromFile(correlationId, path, parameters);
    }    
        
    public void setReferences(IReferences references) {
    	// Override in child class
    }
    
    public void unsetReferences() {
    	// Override in child class
    }
    
    protected void initReferences(IReferences references) throws ApplicationException {
    	// Override in base classes			
		ContextInfo existingInfo = (ContextInfo)references.getOneOptional(DefaultInfoFactory.ContextInfoDescriptor);
		if (existingInfo == null)
            references.put(DefaultInfoFactory.ContextInfoDescriptor, _info);
        else _info = existingInfo;

        references.put(DefaultContainerFactory.Descriptor, _factories);
    }
    
    public boolean isOpen() {
    	return _references != null;
    }
    
    public void open(String correlationId) throws ApplicationException {
    	if (_references != null)
    		throw new InvalidStateException(correlationId, "ALREADY_OPENED", "Container was already opened");
    	
//    	if (_config == null)
//    		throw new InvalidStateException(correlationId, "NO_CONFIG", "Container was not configured");
    	        
        try {
            _logger.trace(correlationId, "Starting container.");

            // Create references with configured components
            _references = new ContainerReferences();
            initReferences(_references);
            _references.putFromConfig(_config);
            setReferences(_references);
            
            // Get reference to container info
    		Descriptor infoDescriptor = new Descriptor("*", "context-info", "*", "*", "*");
    		_info = (ContextInfo) _references.getOneRequired(infoDescriptor);
            
    		_references.open(correlationId);
    		
    		// Get reference to logger
    		_logger = new CompositeLogger(_references);
        	        	
            _logger.info(correlationId, "Container %s started.", _info.getName());
    	} catch (Exception ex) {
    		_references = null;
        	_logger.error(correlationId, ex, "Failed to start container");
        	throw ex;
        }
    }

    public void close(String correlationId) throws ApplicationException {
    	if (_references == null)
    		return;
    	        
        try {
            _logger.trace(correlationId, "Stopping %s container", _info.getName());

            // Close and deference components
            _references.close(correlationId);
            _references = null;

            _logger.info(correlationId, "Container %s stopped", _info.getName());
    	} catch (Exception ex) {
        	_logger.error(correlationId, ex, "Failed to stop container");
        	throw ex;
        }
    }
}
