package org.nlab.xml.stream.factory;

import javax.xml.stream.XMLOutputFactory;


/**
 * @author nlabrot
 *
 * Factory aware of the following issue http://java.net/jira/browse/JAXP-68
 * JAXP RI has an issue on factory in a multithread context
 */
public class StaxCachedOutputFactory extends AbstractStaxCachedFactory<XMLOutputFactory> {

    private static final class SingletonHolder{
        public static final StaxCachedOutputFactory INSTANCE = new StaxCachedOutputFactory();
    }

    /**
     * Get an output factory according to properties
     * @param properties
     * @return
     */
    public static XMLOutputFactory getFactory(Object... properties) {
        return SingletonHolder.INSTANCE.getOrCreateFactory(properties);
    }

    protected synchronized XMLOutputFactory createFactory(Object[] properties) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        for (int ite = 0; ite < properties.length; ite += 2) {
            factory.setProperty(properties[ite].toString(), properties[ite + 1]);
        }
        return factory;
    }
}
