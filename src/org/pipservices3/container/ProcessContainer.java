package org.pipservices3.container;

import java.util.Objects;
import java.util.concurrent.*;

import org.pipservices3.commons.config.*;
import org.pipservices3.components.log.*;

/**
 * Inversion of control (IoC) container that runs as a system process.
 * It processes command line arguments and handles unhandled exceptions and Ctrl-C signal
 * to gracefully shutdown the container.
 * <p>
 * ### Command line arguments ###
 * <ul>
 * <li><code>--config / -c</code>             path to JSON or YAML file with container configuration (default: "./config/config.yml")
 * <li><code>--param / --params / -p</code>   value(s) to parameterize the container configuration
 * <li><code>--help / -h</code>               prints the container usage help
 * </ul>
 * <p>
 * ### Example ###
 * <pre>
 * {@code
 * ProcessContainer container = new ProcessContainer();
 * container.addFactory(new MyComponentFactory());
 *
 * container.run(process.getArgs());
 * }
 * </pre>
 *
 * @see Container
 */
public class ProcessContainer extends Container {

    protected String _configPath = "./config/config.yml";
    private final Semaphore _exitEvent = new Semaphore(0);

    /**
     * Creates a new instance of the container.
     *
     * @param name        (optional) a container name (accessible via ContextInfo)
     * @param description (optional) a container description (accessible via
     *                    ContextInfo)
     */
    public ProcessContainer(String name, String description) {
        super(name, description);
        _logger = new ConsoleLogger();
    }

    private String getConfigPath(String[] args) {
        for (int index = 0; index < args.length; index++) {
            String arg = args[index];
            String nextArg = index < args.length - 1 ? args[index + 1] : null;
            nextArg = nextArg != null && nextArg.startsWith("-") ? null : nextArg;
            if (nextArg != null) {
                if (Objects.equals(arg, "--config") || Objects.equals(arg, "-c")) {
                    return nextArg;
                }
            }
        }
        return _configPath;
    }

    private ConfigParams getParameters(String[] args) {
        // Process command line parameters
        StringBuilder line = new StringBuilder();
        for (int index = 0; index < args.length; index++) {
            String arg = args[index];
            String nextArg = index < args.length - 1 ? args[index + 1] : null;
            nextArg = nextArg != null && nextArg.startsWith("-") ? null : nextArg;
            if (nextArg != null) {
                if (Objects.equals(arg, "--param") || Objects.equals(arg, "--params") || Objects.equals(arg, "-p")) {
                    if (line.length() > 0)
                        line.append(';');
                    line.append(nextArg);
                    index++;
                }
            }
        }

        ConfigParams parameters = ConfigParams.fromString(line.toString());

        // Process environmental variables
        for (Object key : System.getProperties().keySet()) {
            String name = key.toString();
            String value = System.getProperty(name);
            parameters.put(name, value);
        }

        for (Object key : System.getenv().keySet()) {
            String name = key.toString();
            String value = System.getProperty(name);
            parameters.put(name, value);
        }

        return parameters;
    }

    private boolean showHelp(String[] args) {
        for (String arg : args) {
            if ("--help".equals(arg) || "-h".equals(arg))
                return true;
        }
        return false;
    }

    private void printHelp() {
        System.out.println("Pip.Services process container - http://www.github.com/pip-services/pip-services");
        System.out.println("run [-h] [-c <config file>] [-p <param>=<value>]*");
    }

    private void captureErrors(String correlationId) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                if (ex instanceof Exception)
                    _logger.fatal(correlationId, (Exception) ex, "Process is terminated");
                else
                    _logger.fatal(correlationId, "Process is terminated");

                _exitEvent.release();
            }
        });
    }

    private void captureExit(String correlationId) {
        _logger.info(null, "Press Control-C to stop the microservice...");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                _logger.info(correlationId, "Goodbye!");

                _exitEvent.release();

                // Runtime.getRuntime().exit(1);
            }
        });

        // Wait and close
        try {
            _exitEvent.acquire();
        } catch (InterruptedException ex) {
            // Ignore...
        }
    }

    /**
     * Runs the container by instantiating and running components inside the
     * container.
     * <p>
     * It reads the container configuration, creates, configures, references and
     * opens components. On process exit it closes, unreferences and destroys
     * components to gracefully shutdown.
     *
     * @param args command line arguments
     * @throws Exception when error occured.
     */
    public void run(String[] args) throws Exception {
        if (showHelp(args)) {
            printHelp();
            return;
        }

        String correlationId = _info.getName();
        String path = getConfigPath(args);
        ConfigParams parameters = getParameters(args);
        readConfigFromFile(correlationId, path, parameters);

        captureErrors(correlationId);
        open(correlationId);
        captureExit(correlationId);
        close(correlationId);
    }

}
