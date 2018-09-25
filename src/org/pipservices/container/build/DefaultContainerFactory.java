package org.pipservices.container.build;

import org.pipservices.components.auth.*;
import org.pipservices.components.build.*;
import org.pipservices.components.cache.*;
import org.pipservices.components.config.*;
import org.pipservices.components.connect.*;
import org.pipservices.components.count.*;
import org.pipservices.components.info.*;
import org.pipservices.components.log.*;
import org.pipservices.commons.refer.*;

/**
 * Creates default container components (loggers, counters, caches, locks, etc.) by their descriptors.
 * 
 * @see Factory
 * @see DefaultInfoFactory
 * @see DefaultLoggerFactory
 * @see DefaultCountersFactory
 * @see DefaultConfigReaderFactory
 * @see DefaultCacheFactory
 * @see DefaultCredentialStoreFactory
 * @see DefaultDiscoveryFactory
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
		add(new DefaultConfigReaderFactory());
		add(new DefaultCacheFactory());
		add(new DefaultCredentialStoreFactory());
		add(new DefaultDiscoveryFactory());
	}
}
