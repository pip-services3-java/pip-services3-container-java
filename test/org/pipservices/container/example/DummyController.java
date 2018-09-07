package org.pipservices.container.example;

import org.pipservices.commons.config.ConfigParams;
import org.pipservices.commons.config.IReconfigurable;
import org.pipservices.commons.errors.ApplicationException;
import org.pipservices.commons.errors.ConfigException;
import org.pipservices.commons.refer.IReferenceable;
import org.pipservices.commons.refer.IReferences;
import org.pipservices.commons.refer.ReferenceException;
import org.pipservices.commons.run.*;
import org.pipservices.components.log.CompositeLogger;

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