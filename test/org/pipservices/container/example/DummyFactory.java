package org.pipservices.container.example;

import org.pipservices.components.build.Factory;
import org.pipservices.commons.refer.*;

public class DummyFactory extends Factory{
	
	public static Descriptor Descriptor = new Descriptor("pip-services-dummies", "factory", "default", "default", "1.0");
    public static Descriptor ControllerDescriptor = new Descriptor("pip-services-dummies", "controller", "*", "*", "1.0");

    public DummyFactory()
    {
        registerAsType(ControllerDescriptor, DummyController.class);
    }
}
