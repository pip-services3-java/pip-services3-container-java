package org.pipservices.container.refer;

import java.util.*;

import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;
import org.pipservices.commons.run.*;

public class RunReferencesDecorator extends ReferencesDecorator implements IOpenable {
	private boolean _opened = false;

	public RunReferencesDecorator() {
		super();
	}

	public RunReferencesDecorator(IReferences baseReferences) {
		super(baseReferences);
	}
	
    public RunReferencesDecorator(IReferences baseReferences, IReferences parentReferences) {
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
			List<Object> components = super.getAll();
            Opener.open(correlationId, components);
            _opened = true;
        }		
	}
	
	@Override
	public void close(String correlationId) throws ApplicationException {
		if (_opened)
        {
			List<Object> components = super.getAll();
            Closer.close(correlationId, components);
            _opened = false;
        }		
	}
	
    @Override
    public void put(Object locator, Object component) throws ApplicationException {
        super.put(locator, component);

        if (_opened)
            Opener.openOne(null, component);
    }

	@Override
    public Object remove(Object locator) throws ApplicationException {
        Object component = super.remove(locator);

        if (_opened)
            Closer.closeOne(null, component);

        return component;
    }

	@Override
    public List<Object> removeAll(Object locator) throws ApplicationException {
        List<Object> components = super.removeAll(locator);

        if (_opened)
            Closer.close(null, components);

        return components;
    }
}
