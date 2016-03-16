package org.nlab.xml.stream.factory;

import javax.xml.stream.XMLInputFactory;

/**
 * @author nlabrot
 *
 * Factory aware of the following issue http://java.net/jira/browse/JAXP-68
 * JAXP RI has an issue on factory in a multithread context
 */
public class StaxCachedInputFactory extends AbstractStaxCachedFactory<XMLInputFactory> {

    private static final class SingletonHolder{
        public static final StaxCachedInputFactory INSTANCE = new StaxCachedInputFactory();
    }

    /**
     * Get an input factory according to properties
     * @param properties properties
     * @return XMLInputFactory
     */
    public static XMLInputFactory getFactory(Object... properties) {
        return SingletonHolder.INSTANCE.getOrCreateFactory(properties);
    }

    protected synchronized XMLInputFactory createFactory(Object[] properties) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        for (int ite = 0; ite < properties.length; ite += 2) {
            factory.setProperty(properties[ite].toString(), properties[ite + 1]);
        }
        return factory;
    }


}
