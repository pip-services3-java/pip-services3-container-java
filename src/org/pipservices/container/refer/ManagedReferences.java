package org.pipservices.container.refer;


import org.pipservices.commons.errors.*;
import org.pipservices.commons.refer.*;
import org.pipservices.commons.run.*;

public class ManagedReferences extends ReferencesDecorator implements IOpenable, IClosable {
    protected References _references;
    protected BuildReferencesDecorator _builder;
    protected LinkReferencesDecorator _linker;
    protected RunReferencesDecorator _runner;

    public ManagedReferences() {
		this(null);
    }

    public ManagedReferences(Object[] tuples) {
        _references = new References(tuples);
        _builder = new BuildReferencesDecorator(_references, this);
        _linker = new LinkReferencesDecorator(_builder, this);
        _runner = new RunReferencesDecorator(_linker, this);

        setBaseReferences(_runner);
    }

    public boolean isOpen() {
    	return _linker.isOpen() && _runner.isOpen();
    }
    
    public void open(String correlationId) throws ApplicationException {
    	_linker.open(correlationId);
        _runner.open(correlationId);
    }

    public void close(String correlationId) throws ApplicationException {
    	_linker.close(correlationId);
        _runner.close(correlationId);
    }

	public static ManagedReferences fromTuples(Object... tuples) throws ReferenceException {
		return new ManagedReferences(tuples);
	}
}
