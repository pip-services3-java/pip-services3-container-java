package org.pipservices.container;

import java.util.concurrent.*;

import org.pipservices.commons.config.*;
import org.pipservices.commons.errors.*;
import org.pipservices.components.log.*;
import org.pipservices.container.config.*;

public class ProcessContainer extends Container {
	
	protected String _configPath = "../config/config.yml";
    private Semaphore _exitEvent = new Semaphore(0);

    public ProcessContainer(String name, String description) {
    	super(name, description);
    	_logger = new ConsoleLogger();
    }
    
    private String getConfigPath(String[] args)
    {
        for (int index = 0; index < args.length; index++) {
            String arg = args[index];
            String nextArg = index < args.length - 1 ? args[index + 1] : null;
            nextArg = nextArg != null && nextArg.startsWith("-") ? null : nextArg;
            if (nextArg != null) 
            {
                if (arg == "--config" || arg == "-c")
                {
                    return nextArg;
                }
            }
        }
        return _configPath;
    }
    
    
    private ConfigParams getParameters(String[] args)
    {
        // Process command line parameters
        String line = "";
        for (int index = 0; index < args.length; index++)
        {
            String arg = args[index];
            String nextArg = index < args.length - 1 ? args[index + 1] : null;
            nextArg = nextArg != null && nextArg.startsWith("-") ? null : nextArg;
            if (nextArg != null)
            {
                if (arg == "--param" || arg == "--params" || arg == "-p")
                {
                    if (line.length() > 0)
                        line = line + ';';
                    line = line + nextArg;
                    index++;
                }
            }
        }
        ConfigParams parameters = ConfigParams.fromString(line);

//         Process environmental variables
        for (Object key : System.getProperties().keySet())
        {
            String name = key.toString();
            String value = System.getProperty(name);
            parameters.put(name, value);
        }
        
        for (Object key : System.getenv().keySet())
        {
            String name = key.toString();
            String value = System.getProperty(name);
            parameters.put(name, value);
        }

        return parameters;
    }
    

    public void readConfigFromFile(String correlationId, String[] args, String defaultPath) 
		throws ApplicationException {
    	
    	String path = args.length > 0 ? args[0] : defaultPath;
    	readConfigFromFile(correlationId, path);
    }
    
    private void captureErrors(String correlationId) {
        Thread.setDefaultUncaughtExceptionHandler(
    		new Thread.UncaughtExceptionHandler() {				
				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
					if (ex instanceof Exception)
						_logger.fatal(correlationId, (Exception)ex, "Process is terminated");
					else
						_logger.fatal(correlationId, "Process is terminated");
					
		            _exitEvent.release();
				}
			}
		);
    }

    private void captureExit(String correlationId) {
        _logger.info(null, "Press Control-C to stop the microservice...");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	_logger.info(correlationId, "Goodbye!");
            	
            	_exitEvent.release();
            	
            	//Runtime.getRuntime().exit(1);
            }
         });
        
        // Wait and close
        try {
        	_exitEvent.acquire();
        } catch (InterruptedException ex) {
        	// Ignore...
        }        
    }

    public void run(String correlationId) throws Exception {
        captureErrors(correlationId);
    	open(correlationId);
        captureExit(correlationId);
        close(correlationId);
    }

    public void runWithConfig(String correlationId, ContainerConfig config) throws Exception {
    	setConfig(config);
    	run(correlationId);
    }

    public void runWithConfigFile(String correlationId, String path) throws Exception {
    	readConfigFromFile(correlationId, path);
    	run(correlationId);
    }

    public void runWithArgumentsOrConfigFile(String correlationId, String[] args, String defaultPath) throws Exception {
    	readConfigFromFile(correlationId, args, defaultPath);
    	run(correlationId);
    }        
}
