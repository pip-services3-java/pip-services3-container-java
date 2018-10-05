package org.pipservices.container.refer;

import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;
import org.pipservices.commons.run.*;

/**
 * Managed references that in addition to keeping and locating references can also 
 * manage their lifecycle.
 * <ul>
 * <li>Auto-creation of missing component using available factories
 * <li>Auto-linking newly added components
 * <li>Auto-opening newly added components
 * <li>Auto-closing removed components
 * </ul>
 * 
 * @see RunReferencesDecorator
 * @see LinkReferencesDecorator
 * @see BuildReferencesDecorator
 * @see <a href="https://raw.githubusercontent.com/pip-services-java/pip-services-commons-java/master/doc/api/org/pipservices/commons/refer/References.html">References</a>
 */
public class ManagedReferences extends ReferencesDecorator implements IOpenable, IClosable {
	protected References _references;
	protected BuildReferencesDecorator _builder;
	protected LinkReferencesDecorator _linker;
	protected RunReferencesDecorator _runner;

	/**
	 * Creates a new instance of the references
	 */
	public ManagedReferences() {
		this(null);
	}

	/**
	 * Creates a new instance of the references
	 * 
	 * @param tuples tuples where odd values are component locators (descriptors)
	 *               and even values are component references
	 */
	public ManagedReferences(Object[] tuples) {
		_references = new References(tuples);
		_builder = new BuildReferencesDecorator(_references, this);
		_linker = new LinkReferencesDecorator(_builder, this);
		_runner = new RunReferencesDecorator(_linker, this);

		setBaseReferences(_runner);
	}

	/**
	 * Checks if the component is opened.
	 * 
	 * @return true if the component has been opened and false otherwise.
	 */
	public boolean isOpen() {
		return _linker.isOpen() && _runner.isOpen();
	}

	/**
	 * Opens the component.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @throws ApplicationException when error occured.
	 */
	public void open(String correlationId) throws ApplicationException {
		_linker.open(correlationId);
		_runner.open(correlationId);
	}

	/**
	 * Closes component and frees used resources.
	 * 
	 * @param correlationId (optional) transaction id to trace execution through
	 *                      call chain.
	 * @throws ApplicationException when error occured.
	 */
	public void close(String correlationId) throws ApplicationException {
		_linker.close(correlationId);
		_runner.close(correlationId);
	}

	/**
	 * Creates a new ManagedReferences object filled with provided key-value pairs
	 * called tuples. Tuples parameters contain a sequence of locator1, component1,
	 * locator2, component2, ... pairs.
	 * 
	 * @param tuples the tuples to fill a new ManagedReferences object.
	 * @return a new ManagedReferences object.
	 * @throws ReferenceException when no found references.
	 */
	public static ManagedReferences fromTuples(Object... tuples) throws ReferenceException {
		return new ManagedReferences(tuples);
	}
}
