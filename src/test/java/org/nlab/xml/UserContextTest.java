package org.nlab.xml;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicInteger;

import static org.nlab.xml.stream.XmlStreams.newConsumerAndClose;
import static org.nlab.xml.stream.predicate.Predicates.css;

/**
 * Created by nlabrot on 12/03/15.
 */
public class UserContextTest {


  @Test
    public void testCss_UserContext() throws Exception {
        AtomicInteger integer = new AtomicInteger();

        newConsumerAndClose(new FileInputStream("src/test/resources/usercontext/usercontext.xml"))
                .matchCss("ancestor1", c -> {
                    c.getUserContext().setProperty("foo", "bar");
                    integer.incrementAndGet();
                })
                .matchCss("ancestor1 child4", c -> {
                    Assert.assertEquals("bar", c.getUserContext().findProperty("foo", String.class).get());
                    integer.incrementAndGet();
                })
                .consume();


        Assert.assertEquals(2, integer.get());
    }





}
