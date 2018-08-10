package org.pipservices.container.refer;

import org.pipservices.components.build.*;
import org.pipservices.commons.config.*;
import org.pipservices.commons.refer.*;
import org.pipservices.commons.reflect.*;
import org.pipservices.container.config.*;

public class ContainerReferences extends ManagedReferences {
	public ContainerReferences() { }

	private Object createStatically(Object locator) throws ReferenceException {
		Object component = _builder.create(locator);
		if (component == null)
			throw (ReferenceException) new ReferenceException(null, locator);
		return component;
	}
	
	public void putFromConfig(ContainerConfig config) throws ReferenceException {
		for (ComponentConfig componentConfig : config) {
			Object component = null;
			Object locator = null;

			try {				
				// Create component dynamically
				if (componentConfig.getType() != null) {
					locator = componentConfig.getType();
					component = TypeReflector.createInstanceByDescriptor(componentConfig.getType());				
				}
				// Or create component statically
				else if (componentConfig.getDescriptor() != null) {
					locator = componentConfig.getDescriptor();
					component = createStatically(componentConfig.getDescriptor());				
				}
				
				// Check that component was created
				if (component == null) {
					throw (CreateException)new CreateException(
						"CANNOT_CREATE_COMPONENT", "Cannot create component"
					)
					.withDetails("config", config);
				}
	
				// Add component to the list				
				_references.put(locator, component);
				
				// Configure component
				if (component instanceof IConfigurable) 
					((IConfigurable)component).configure(componentConfig.getConfig());
			} catch (Exception ex) {
				throw (ReferenceException) new ReferenceException(null, locator)
					.withCause(ex);
			}
		}
	}
}
