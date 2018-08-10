package org.pipservices.container.refer;

import java.util.*;

import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;

public class LinkReferencesDecorator extends ReferencesDecorator {
	private boolean _linkEnabled = true;
	
	public LinkReferencesDecorator() {
		super();
	}

	public LinkReferencesDecorator(IReferences baseReferences) {
		super(baseReferences);
	}
	
    public LinkReferencesDecorator(IReferences baseReferences, IReferences parentReferences) {
    	super(baseReferences, parentReferences);
    }

    public boolean getLinkEnabled() { return _linkEnabled; }
    public void setLinkEnabled(boolean value) { _linkEnabled = value; }

    @Override
    public void put(Object locator, Object component) throws ApplicationException {
        super.put(locator, component);

        if (_linkEnabled)
            Referencer.setReferencesForOne(getParentReferences(), component);
    }

    @Override
    public Object remove(Object locator) throws ApplicationException {
        Object component = super.remove(locator);

        if (_linkEnabled)
            Referencer.unsetReferencesForOne(component);

        return component;
    }

    @Override
    public List<Object> removeAll(Object locator) throws ApplicationException {
        List<Object> components = super.removeAll(locator);

        if (_linkEnabled)
            Referencer.unsetReferences(components);

        return components;
    }

}
