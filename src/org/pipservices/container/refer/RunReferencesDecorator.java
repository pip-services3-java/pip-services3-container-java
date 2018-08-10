package org.pipservices.container.refer;

import java.util.*;

import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;
import org.pipservices.commons.run.*;

public class RunReferencesDecorator extends ReferencesDecorator {
	private boolean _openEnabled = true;
	private boolean _closeEnabled = true;

	public RunReferencesDecorator() {
		super();
	}

	public RunReferencesDecorator(IReferences baseReferences) {
		super(baseReferences);
	}
	
    public RunReferencesDecorator(IReferences baseReferences, IReferences parentReferences) {
    	super(baseReferences, parentReferences);
    }

    public boolean getOpenEnabled() { return _openEnabled; }
    public void setOpenEnabled(boolean value) { _openEnabled = value; }
    
    public boolean getCloseEnabled() { return _closeEnabled; }
    public void setCloseEnabled(boolean value) { _closeEnabled = value; }

    @Override
    public void put(Object locator, Object component) throws ApplicationException {
        super.put(locator, component);

        if (_openEnabled)
            Opener.openOne(null, component);
    }

	@Override
    public Object remove(Object locator) throws ApplicationException {
        Object component = super.remove(locator);

        if (_closeEnabled)
            Closer.closeOne(null, component);

        return component;
    }

	@Override
    public List<Object> removeAll(Object locator) throws ApplicationException {
        List<Object> components = super.removeAll(locator);

        if (_closeEnabled)
            Closer.close(null, components);

        return components;
    }

}
