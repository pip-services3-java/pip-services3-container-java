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
    private String message = "Hello World!";
	public String getMessage() { return message; }
	private void setMessage(String message) { this.message = message; }
    private long counter = 0;
	public long getCounter() { return counter; }
	private void setCounter(long counter) { this.counter = counter; }
	
	public DummyController() {
        _timer = new FixedRateTimer(
            (String, Parameters) -> { notify(null, new Parameters()); }, 
            1000, 1000
        );
    }
	
	@Override
	public void configure(ConfigParams config) throws ConfigException {
		message = config.getAsStringWithDefault("message", message);
		
	}

	@Override
	public void setReferences(IReferences references) throws ReferenceException, ConfigException {
		_logger.setReferences(references);
		
	}
	
	@Override
	public void close(String correlationId) throws ApplicationException {
		 _timer.stop();
         _logger.trace(correlationId, "Dummy controller closed");		
	}

	@Override
	public void notify(String correlationId, Parameters args) throws ApplicationException {
		 _logger.info(correlationId, "{0} - {1}", counter++, message);
		
	}

	@Override
	public void open(String correlationId) throws ApplicationException {
		_timer.start();
        _logger.trace(correlationId, "Dummy controller opened");		
	}
	@Override
	public boolean isOpen() {
		return _timer.isStarted();
	}
}