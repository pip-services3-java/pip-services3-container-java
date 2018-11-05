package org.pipservices3.container.example;

import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IReconfigurable;
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.commons.refer.IReferenceable;
import org.pipservices3.commons.refer.IReferences;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.commons.run.*;
import org.pipservices3.components.log.CompositeLogger;

public class DummyController implements  IReferenceable, IReconfigurable, IOpenable, INotifiable{
	private final FixedRateTimer _timer;
    private final CompositeLogger _logger = new CompositeLogger();
    private String _message = "Hello World!";
	private long _counter = 0;
    	
	public DummyController() {
        _timer = new FixedRateTimer(
            (String, Parameters) -> { notify(null, new Parameters()); }, 
            1000, 1000
        );
    }
	
	@Override
	public void configure(ConfigParams config) throws ConfigException {
		_message = config.getAsStringWithDefault("message", _message);		
	}

	@Override
	public void setReferences(IReferences references) throws ReferenceException, ConfigException {
		_logger.setReferences(references);
	}
	
	@Override
	public void notify(String correlationId, Parameters args) throws ApplicationException {
		 _logger.info(correlationId, "%s - %s", _counter++, _message);
	}

	@Override
	public boolean isOpen() {
		return _timer.isStarted();
	}

	@Override
	public void open(String correlationId) throws ApplicationException {
		_timer.start();
        _logger.trace(correlationId, "Dummy controller opened");		
	}
	
	@Override
	public void close(String correlationId) throws ApplicationException {
		 _timer.stop();
         _logger.trace(correlationId, "Dummy controller closed");		
	}

}