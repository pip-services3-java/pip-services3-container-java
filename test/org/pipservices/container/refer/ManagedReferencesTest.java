package org.pipservices.container.refer;

import static org.junit.Assert.*;

import org.junit.*;
import org.pipservices.commons.errors.*;
import org.pipservices.components.log.*;
import org.pipservices.commons.refer.*;

public class ManagedReferencesTest {
    @Test
    public void testAutoCreateComponent() throws ApplicationException {
    	ManagedReferences refs = new ManagedReferences();

    	DefaultLoggerFactory factory = new DefaultLoggerFactory();
        refs.put(DefaultLoggerFactory.Descriptor, factory);

        ILogger logger = refs.getOneRequired(ILogger.class, new Descriptor("*", "logger", "*", "*", "*"));
        assertNotNull(logger);
    }

    @Test
    public void testStringLocator() throws ApplicationException {
    	ManagedReferences refs = new ManagedReferences();

    	DefaultLoggerFactory factory = new DefaultLoggerFactory();
        refs.put(new Object(), factory);

        Object component = refs.getOneOptional("ABC");
        assertNull(component);
    }

    @Test
    public void testNullLocator() throws ApplicationException {
    	ManagedReferences refs = new ManagedReferences();

    	DefaultLoggerFactory factory = new DefaultLoggerFactory();
        refs.put(new Object(), factory);

        Object component = refs.getOneOptional(null);
        assertNull(component);
    }
}
