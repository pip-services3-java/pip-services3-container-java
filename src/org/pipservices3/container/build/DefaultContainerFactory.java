package org.pipservices3.container.build;

import org.pipservices3.components.auth.*;
import org.pipservices3.components.build.*;
import org.pipservices3.components.cache.*;
import org.pipservices3.components.config.*;
import org.pipservices3.components.connect.*;
import org.pipservices3.components.count.*;
import org.pipservices3.components.info.*;
import org.pipservices3.components.log.*;
import org.pipservices3.commons.refer.*;
import org.pipservices3.components.test.DefaultTestFactory;
import org.pipservices3.components.trace.DefaultTracerFactory;

/**
 * Creates default container components (loggers, counters, caches, locks, etc.) by their descriptors.
 *
 * @see <a href="https://pip-services3-java.github.io/pip-services3-components-java/org/pipservices3/components/build/Factory.html">Factory</a>
 * @see <a href="https://pip-services3-java.github.io/pip-services3-components-java/org/pipservices3/components/info/DefaultInfoFactory.html">DefaultInfoFactory</a>
 * @see <a href="https://pip-services3-java.github.io/pip-services3-components-java/org/pipservices3/components/log/DefaultLoggerFactory.html">DefaultLoggerFactory</a>
 * @see <a href="https://pip-services3-java.github.io/pip-services3-components-java/org/pipservices3/components/count/DefaultCountersFactory.html">DefaultCountersFactory</a>
 * @see <a href="https://pip-services3-java.github.io/pip-services3-components-java/org/pipservices3/components/config/DefaultConfigReaderFactory.html">DefaultConfigReaderFactory</a>
 * @see <a href="https://pip-services3-java.github.io/pip-services3-components-java/org/pipservices3/components/cache/DefaultCacheFactory.html">DefaultCacheFactory</a>
 * @see <a href="https://pip-services3-java.github.io/pip-services3-components-java/org/pipservices3/components/auth/DefaultCredentialStoreFactory.html">DefaultCredentialStoreFactory</a>
 * @see <a href="https://pip-services3-java.github.io/pip-services3-components-java/org/pipservices3/components/connect/DefaultDiscoveryFactory.html">DefaultDiscoveryFactory</a>
 */
public class DefaultContainerFactory extends CompositeFactory {
    public final static Descriptor Descriptor = new Descriptor("pip-services", "factory", "container", "default",
            "1.0");

    /**
     * Create a new instance of the factory and sets nested factories.
     *
     * @param factories a list of nested factories
     */
    public DefaultContainerFactory(IFactory... factories) {
        super(factories);
        add(new DefaultInfoFactory());
        add(new DefaultLoggerFactory());
        add(new DefaultCountersFactory());
        add(new DefaultTracerFactory());
        add(new DefaultConfigReaderFactory());
        add(new DefaultCacheFactory());
        add(new DefaultCredentialStoreFactory());
        add(new DefaultDiscoveryFactory());
        add(new DefaultTestFactory());
    }
}
