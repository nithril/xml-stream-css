package org.nlab.xml.stream.factory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

/**
 * @author nlabrot
 * Cached factory for input/output
 */
public class StaxCachedFactory  {

    protected StaxCachedFactory()
    {
    }

    /**
     * Get an input factory according to properties
     * @param properties
     * @return
     */
    public static XMLInputFactory getInputFactory(Object... properties) {
        return StaxCachedInputFactory.getFactory(properties);
    }

    /**
     * Get an output factory according to properties
     * @param properties
     * @return
     */
    public static XMLOutputFactory getOutputFactory(Object... properties) {
        return StaxCachedOutputFactory.getFactory(properties);
    }

}
