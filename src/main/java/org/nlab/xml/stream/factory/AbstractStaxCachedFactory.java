package org.nlab.xml.stream.factory;


import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author nlabrot
 *
 * Factory aware of the following issue http://java.net/jira/browse/JAXP-68
 * JAXP RI has an issue on factory in a multithread context
 */
public abstract class AbstractStaxCachedFactory<T> {

    private static final String WOODSTOX_FACTORY_PREFIX = "Wstx";

    private Map<Long, Holder<T>> factoriesMap = new ConcurrentHashMap<Long, Holder<T>>();


    protected T getOrCreateFactory(Object... properties) {
        Validate.isTrue(properties.length % 2 == 0);

        long key = createKey(properties);

        if (factoriesMap.containsKey(key) && factoriesMap.get(key).get() != null) {
            return factoriesMap.get(key).get();
        }

        T factory = createFactory(properties);

        if (factoriesMap.get(key) == null) {
            factoriesMap.put(key, createHolder(factory));
        } else {
            factoriesMap.get(key).set(factory);
        }

        return factory;

    }

    protected static long createKey(Object... properties) {
        long key = 0;
        for (Object prop : properties) {
            key = key + (long) prop.hashCode();
        }
        return key;
    }


    protected abstract T createFactory(Object[] properties);


    private Holder<T> createHolder(T value) {
        if (value.getClass().getSimpleName().startsWith(WOODSTOX_FACTORY_PREFIX)) {
            return new GlobalHolder<>(value);
        } else {
            return new ThreadLocalHolder<>(value);
        }
    }


    //Holder
    public interface Holder<T> {
        void set(T t);

        T get();
    }

    /**
     * Holder bounded to a thread
     */
    private final class ThreadLocalHolder<T> extends ThreadLocal<T> implements Holder<T> {
        public ThreadLocalHolder(T value) {
            this.set(value);
        }
    }

    /**
     * Holder bounded to the application
     */
    private final class GlobalHolder<T> implements Holder<T> {
        protected volatile T value;

        private GlobalHolder(T value) {
            this.value = value;
        }

        @Override
        public void set(T t) {
            this.value = t;
        }

        @Override
        public T get() {
            return value;
        }
    }
}
