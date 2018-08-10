package org.pipservices.container.refer;

import java.util.*;

import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;

public class ReferencesDecorator implements IReferences {
	private IReferences _baseReferences;
	private IReferences _parentReferences;

	public ReferencesDecorator() {}
	
	public ReferencesDecorator(IReferences baseReferences) {
		_baseReferences = baseReferences;
		_parentReferences = baseReferences;
	}
	
	public ReferencesDecorator(IReferences baseReferences, IReferences parentReferences) {
        _baseReferences = baseReferences != null ? baseReferences : parentReferences;
        _parentReferences = parentReferences != null ? parentReferences : baseReferences;
    }

    public IReferences getBaseReferences() { return _baseReferences; }
    public void setBaseReferences(IReferences value) { _baseReferences = value; }
    
    public IReferences getParentReferences() { return _parentReferences; }
    public void setParentReferences(IReferences value) { _parentReferences = value; }

	public void put(Object locator, Object component) throws ApplicationException {
		_baseReferences.put(locator, component);
	}
	
	public Object remove(Object locator) throws ApplicationException {
		return _baseReferences.remove(locator);
	}

	public List<Object> removeAll(Object locator) throws ApplicationException {
		return _baseReferences.removeAll(locator);
	}

	public List<Object> getAll() {
		return _baseReferences.getAll();
	}
		
    public Object getOneOptional(Object locator) {
    	try {
	        List<Object> components = find(Object.class, locator, false);
	        return components.size() > 0 ? components.get(0) : null;
    	} catch (Exception ex) {
    		return null;
    	}
    }

    public <T> T getOneOptional(Class<T> type, Object locator) {
    	try {
	        List<T> components = find(type, locator, false);
	        return components.size() > 0 ? components.get(0) : null;
    	} catch (Exception ex) {
    		return null;
    	}
    }

    public Object getOneRequired(Object locator) throws ReferenceException {
        List<Object> components = find(Object.class, locator, true);
        return components.size() > 0 ? components.get(0) : null;
    }

    public <T> T getOneRequired(Class<T> type, Object locator) throws ReferenceException {
        List<T> components = find(type, locator, true);
        return components.size() > 0 ? components.get(0) : null;
    }

    public List<Object> getOptional(Object locator) {
    	try {
    		return find(Object.class, locator, false);
    	} catch (Exception ex) {
    		return new ArrayList<Object>();
    	}
    }

    public <T> List<T> getOptional(Class<T> type, Object locator) {
    	try {
    		return find(type, locator, false);
    	} catch (Exception ex) {
    		return new ArrayList<T>();
    	}
    }

    public List<Object> getRequired(Object locator) throws ReferenceException {
        return find(Object.class, locator, true);
    }

    public <T> List<T> getRequired(Class<T> type, Object locator) throws ReferenceException {
        return find(type, locator, true);
    }

    public List<Object> find(Object locator, boolean required) throws ReferenceException {
        return find(Object.class, locator, required);
    }
	
	public <T> List<T> find(Class<T> type, Object locator, boolean required) throws ReferenceException {
		return _baseReferences.find(type, locator, required);
    }

	public List<Object> getAllLocators() {
		return _baseReferences.getAllLocators();
	}


}
