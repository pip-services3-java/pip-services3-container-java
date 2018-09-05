package org.pipservices.container.refer;

import java.util.*;

import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;
import org.pipservices.commons.run.IOpenable;

public class LinkReferencesDecorator extends ReferencesDecorator implements IOpenable{
	private boolean _opened = false;
	
	public LinkReferencesDecorator() {
		super();
	}

	public LinkReferencesDecorator(IReferences baseReferences) {
		super(baseReferences);
	}
	
    public LinkReferencesDecorator(IReferences baseReferences, IReferences parentReferences) {
    	super(baseReferences, parentReferences);
    }

    @Override
	public boolean isOpen() {
		return _opened;
	}

	@Override
	public void open(String correlationId) throws ApplicationException {
		if (!_opened)
        {
            _opened = true;
            List<Object> components = super.getAll();
            Referencer.setReferences(getParentReferences(), components);
        }		
	}
	
	@Override
	public void close(String correlationId) throws ApplicationException {
		if (_opened)
        {
            _opened = false;
            List<Object> components = super.getAll();
            Referencer.unsetReferences(components);
        }
	}
    
    @Override
    public void put(Object locator, Object component) throws ApplicationException {
        super.put(locator, component);

        if (_opened)
            Referencer.setReferencesForOne(getParentReferences(), component);
    }

    @Override
    public Object remove(Object locator) throws ApplicationException {
        Object component = super.remove(locator);

        if (_opened)
            Referencer.unsetReferencesForOne(component);

        return component;
    }

    @Override
    public List<Object> removeAll(Object locator) throws ApplicationException {
        List<Object> components = super.removeAll(locator);

        if (_opened)
            Referencer.unsetReferences(components);

        return components;
    }
}
