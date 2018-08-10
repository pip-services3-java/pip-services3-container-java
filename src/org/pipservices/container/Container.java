package org.pipservices.container;

import java.util.*;

import org.pipservices.commons.errors.*;
import org.pipservices.components.info.*;
import org.pipservices.components.log.*;
import org.pipservices.commons.refer.*;
import org.pipservices.commons.run.*;
import org.pipservices.container.build.*;
import org.pipservices.container.config.*;
import org.pipservices.container.refer.*;

public class Container {
	protected ILogger _logger = new NullLogger();
	protected DefaultContainerFactory _factories = new DefaultContainerFactory();
    protected ContextInfo _info;
    protected ContainerConfig _config;
    protected ContainerReferences _references;
    
    public Container( String name, String description ) {
    	_info = new ContextInfo(name, description);
    }

	public Container(ContainerConfig config) {
        _config = config;
    }

	public ContextInfo getInfo() { return _info; }
	
	public ContainerConfig getConfig() { return _config; }	
    public void setConfig(ContainerConfig value) { _config = value; }

    public IReferences getReferences() { return _references; }
    
    public void readConfigFromFile(String correlationId, String path) throws ApplicationException {
    	_config = ContainerConfigReader.readFromFile(correlationId, path);
    }    
        
    protected void initReferences(IReferences references) throws ApplicationException {
    	// Override in base classes			
		ContextInfo existingInfo = (ContextInfo)references.getOneOptional(DefaultInfoFactory.ContextInfoDescriptor);
		if (existingInfo == null)
            references.put(DefaultInfoFactory.ContextInfoDescriptor, _info);
        else _info = existingInfo;

        references.put(DefaultContainerFactory.Descriptor, _factories);
    }
    
    public void start(String correlationId) throws Exception {
    	if (_config == null)
    		throw new InvalidStateException(correlationId, "NO_CONFIG", "Container was not configured");
    	        
        try {
            _logger.trace(correlationId, "Starting container.");

            // Create references with configured components
            _references = new ContainerReferences();
            initReferences(_references);
            _references.putFromConfig(_config);
                		
    		// Reference and open components
    		List<Object> components = _references.getAll();
    		Referencer.setReferences(_references, components);
        	Opener.open(correlationId, _references.getAll());

    		// Get reference to logger
    		_logger = new CompositeLogger(_references);
        	
            // Get reference to container info
    		Descriptor infoDescriptor = new Descriptor("*", "container-info", "*", "*", "*");
    		_info = (ContextInfo) _references.getOneRequired(infoDescriptor);
        	
            _logger.info(correlationId, "Container %s started.", _info.getName());
    	} catch (Exception ex) {
    		_references = null;
        	_logger.error(correlationId, ex, "Failed to start container");
        	throw ex;
        }
    }

    public void stop(String correlationId) throws Exception {
    	if (_references == null)
    		throw new InvalidStateException(correlationId, "NO_STARTED", "Container was not started");
    	        
        try {
            _logger.trace(correlationId, "Stopping %s container", _info.getName());

            // Close and deference components
    		List<Object> components = _references.getAll();
            Closer.close(correlationId, components);
    		Referencer.unsetReferences(components);

            _logger.info(correlationId, "Container %s stopped", _info.getName());
    	} catch (Exception ex) {
        	_logger.error(correlationId, ex, "Failed to stop container");
        	throw ex;
        }
    }
}
