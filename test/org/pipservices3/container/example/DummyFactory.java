package org.pipservices3.container.example;

import org.pipservices3.components.build.Factory;
import org.pipservices3.commons.refer.*;

public class DummyFactory extends Factory {
    public static Descriptor ControllerDescriptor = new Descriptor("pip-services-dummies", "controller", "*", "*", "1.0");

    public DummyFactory() {
        registerAsType(ControllerDescriptor, DummyController.class);
    }
}