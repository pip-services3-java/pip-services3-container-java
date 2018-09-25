package org.pipservices.container.refer;

import java.util.*;

import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;

/**
 * Chainable decorator for IReferences that allows to inject additional capabilities
 * such as automatic component creation, automatic registration and opening.
 * 
 * @see IReferences
 */
public class ReferencesDecorator implements IReferences {
	/**
	 * The next references or decorator in the chain.
	 */
	private IReferences _baseReferences;
	/**
	 * The decorator at the top of the chain.
	 */
	private IReferences _parentReferences;

	/**
	 * Creates a new instance of the decorator.
	 */
	public ReferencesDecorator() {
	}

	/**
	 * Creates a new instance of the decorator.
	 * 
	 * @param baseReferences the next references or decorator in the chain.
	 */
	public ReferencesDecorator(IReferences baseReferences) {
		_baseReferences = baseReferences;
		_parentReferences = baseReferences;
	}

	/**
	 * Creates a new instance of the decorator.
	 * 
	 * @param baseReferences   the next references or decorator in the chain.
	 * @param parentReferences the decorator at the top of the chain.
	 */
	public ReferencesDecorator(IReferences baseReferences, IReferences parentReferences) {
		_baseReferences = baseReferences != null ? baseReferences : parentReferences;
		_parentReferences = parentReferences != null ? parentReferences : baseReferences;
	}

	public IReferences getBaseReferences() {
		return _baseReferences;
	}

	public void setBaseReferences(IReferences value) {
		_baseReferences = value;
	}

	public IReferences getParentReferences() {
		return _parentReferences;
	}

	public void setParentReferences(IReferences value) {
		_parentReferences = value;
	}

	/**
	 * Puts a new reference into this reference map.
	 * 
	 * @param locator   a locator to find the reference by.
	 * @param component a component reference to be added.
	 * @throws ApplicationException when error occured.
	 */
	public void put(Object locator, Object component) throws ApplicationException {
		_baseReferences.put(locator, component);
	}

	/**
	 * Removes a previously added reference that matches specified locator. If many
	 * references match the locator, it removes only the first one. When all
	 * references shall be removed, use [[removeAll]] method instead.
	 * 
	 * @param locator a locator to remove reference
	 * @return the removed component reference.
	 * 
	 * @see #removeAll(Object)
	 */
	public Object remove(Object locator) throws ApplicationException {
		return _baseReferences.remove(locator);
	}

	/**
	 * Removes all component references that match the specified locator.
	 * 
	 * @param locator the locator to remove references by.
	 * @return a list, containing all removed references.
	 */
	public List<Object> removeAll(Object locator) throws ApplicationException {
		return _baseReferences.removeAll(locator);
	}

	/**
	 * Gets all component references registered in this reference map.
	 * 
	 * @return a list with component references.
	 */
	public List<Object> getAll() {
		return _baseReferences.getAll();
	}

	/**
	 * Gets locators for all registered component references in this reference map.
	 * 
	 * @return a list with component locators.
	 */
	public List<Object> getAllLocators() {
		return _baseReferences.getAllLocators();
	}

	/**
	 * Gets an optional component reference that matches specified locator.
	 * 
	 * @param locator the locator to find references by.
	 * @return a matching component reference or null if nothing was found.
	 */
	public Object getOneOptional(Object locator) {
		try {
			List<Object> components = find(Object.class, locator, false);
			return components.size() > 0 ? components.get(0) : null;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Gets an optional component reference that matches specified locator.
	 * 
	 * @param type    the class type.
	 * @param locator the locator to find references by.
	 * @return a matching component reference or null if nothing was found.
	 */
	public <T> T getOneOptional(Class<T> type, Object locator) {
		try {
			List<T> components = find(type, locator, false);
			return components.size() > 0 ? components.get(0) : null;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Gets a required component reference that matches specified locator.
	 * 
	 * @param locator the locator to find a reference by.
	 * @return a matching component reference.
	 * @throws ReferenceException when no references found.
	 */
	public Object getOneRequired(Object locator) throws ReferenceException {
		List<Object> components = find(Object.class, locator, true);
		return components.size() > 0 ? components.get(0) : null;
	}

	/**
	 * Gets a required component reference that matches specified locator.
	 * 
	 * @param type    the class type.
	 * @param locator the locator to find a reference by.
	 * @return a matching component reference.
	 * @throws ReferenceException when no references found.
	 */
	public <T> T getOneRequired(Class<T> type, Object locator) throws ReferenceException {
		List<T> components = find(type, locator, true);
		return components.size() > 0 ? components.get(0) : null;
	}

	/**
	 * Gets all component references that match specified locator.
	 * 
	 * @param locator the locator to find references by.
	 * @return a list with matching component references or empty list if nothing
	 *         was found.
	 */
	public List<Object> getOptional(Object locator) {
		try {
			return find(Object.class, locator, false);
		} catch (Exception ex) {
			return new ArrayList<Object>();
		}
	}

	/**
	 * Gets all component references that match specified locator.
	 * 
	 * @param type    the class type
	 * @param locator the locator to find references by.
	 * @return a list with matching component references or empty list if nothing
	 *         was found.
	 */
	public <T> List<T> getOptional(Class<T> type, Object locator) {
		try {
			return find(type, locator, false);
		} catch (Exception ex) {
			return new ArrayList<T>();
		}
	}

	/**
	 * Gets all component references that match specified locator. At least one
	 * component reference must be present. If it doesn't the method throws an
	 * error.
	 * 
	 * @param locator the locator to find references by.
	 * @return a list with matching component references.
	 * 
	 * @throws ReferenceException when no references found.
	 */
	public List<Object> getRequired(Object locator) throws ReferenceException {
		return find(Object.class, locator, true);
	}

	/**
	 * Gets all component references that match specified locator. At least one
	 * component reference must be present. If it doesn't the method throws an
	 * error.
	 * 
	 * @param type    the class type.
	 * @param locator the locator to find references by.
	 * @return a list with matching component references.
	 * 
	 * @throws ReferenceException when no references found.
	 */
	public <T> List<T> getRequired(Class<T> type, Object locator) throws ReferenceException {
		return find(type, locator, true);
	}

	/**
	 * Gets all component references that match specified locator.
	 * 
	 * @param locator  the locator to find a reference by.
	 * @param required forces to raise an exception if no reference is found.
	 * @return a list with matching component references.
	 * 
	 * @throws ReferenceException when required is set to true but no references
	 *                            found.
	 */
	public List<Object> find(Object locator, boolean required) throws ReferenceException {
		return find(Object.class, locator, required);
	}

	/**
	 * Gets all component references that match specified locator.
	 * 
	 * @param type     the class type.
	 * @param locator  the locator to find a reference by.
	 * @param required forces to raise an exception if no reference is found.
	 * @return a list with matching component references.
	 * 
	 * @throws ReferenceException when required is set to true but no references
	 *                            found.
	 */
	public <T> List<T> find(Class<T> type, Object locator, boolean required) throws ReferenceException {
		return _baseReferences.find(type, locator, required);
	}
}
