package org.pipservices.container.example;

import org.pipservices.container.ProcessContainer;

public class DummyProcess extends ProcessContainer{

	public DummyProcess() {
		super("dummy", "Sample dummy process");
		
		this._configPath = "./config/dummy.yml";
        this._factories.add(new DummyFactory());
	}

}
