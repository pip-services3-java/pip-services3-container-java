package org.pipservices.container.refer;

import java.util.*;

import org.pipservices.components.build.*;
import org.pipservices.commons.refer.*;

public class BuildReferencesDecorator extends ReferencesDecorator {
	private boolean _buildEnabled = true;
	
	public BuildReferencesDecorator() {
		super();
	}

	public BuildReferencesDecorator(IReferences baseReferences) {
		super(baseReferences);
	}
	
    public BuildReferencesDecorator(IReferences baseReferences, IReferences parentReferences) {
    	super(baseReferences, parentReferences);
    }
	
    public boolean getBuildEnabled() { return _buildEnabled; }
    public void setBuildEnabled(boolean value) { _buildEnabled = value; }

    public IFactory findFactory(Object locator) {
        for (Object component : getAll()) {
            if (component instanceof IFactory) {
            	IFactory factory = (IFactory)component;
                if (factory.canCreate(locator) != null )
                    return factory;
            }
        }

        return null;
    }

    public Object create(Object locator) {
        // Find factory
        IFactory factory = findFactory(locator);
        if (factory == null) return null;

        try {
            // Create component
            return factory.create(locator);
        } catch (Exception ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> List<T> find(Class<T> type, Object locator, boolean required) throws ReferenceException {
        List<T> components = super.find(type, locator, false);

        // Try to create component
        if (components.size() == 0 && _buildEnabled) {
            Object component = create(locator);
            if (type.isInstance(component)) {
                try {
	                getParentReferences().put(locator, component);
	                components.add((T)component);
                } catch (Exception ex) {
                	// Ignore exception
                }
            }
        }

        // Throw exception is no required components found
        if (required && components.size() == 0)
            throw new ReferenceException(locator);

        return components;
    }
}
