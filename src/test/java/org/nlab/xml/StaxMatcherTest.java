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
public class StaxMatcherTest {


    @Test
    public void testCss_Root() throws Exception {

        AtomicInteger integer = new AtomicInteger();

        newConsumerAndClose(new FileInputStream("src/test/resources/root/root.xml"))
                .matchCss(":root", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("*:root", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("rootElement:root", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("foo:root", c -> {
                    throw new IllegalStateException();
                })
                .consume();

        Assert.assertEquals(3, integer.get());
    }

    @Test
    public void testCss_Attributes() throws Exception {
        AtomicInteger integer = new AtomicInteger();

        newConsumerAndClose(new FileInputStream("src/test/resources/attribute/attribute.xml"))
                .matchCss("a[testExist]", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("a[testNotExist]", c -> {
                    throw new IllegalStateException();
                })
                .matchCss("a[testEquals=\"ok\"]", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("a[testEquals=\"okok\"]", c -> {
                    throw new IllegalStateException();
                })
                .matchCss("a[testContains*=\"ips\"]", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("a[testContains*=\"ipsd\"]", c -> {
                    throw new IllegalStateException();
                })
                .consume();

        Assert.assertEquals(3, integer.get());
    }


    @Test
    public void testCss_Ancestor() throws Exception {
        AtomicInteger integer = new AtomicInteger();

        newConsumerAndClose(new FileInputStream("src/test/resources/children/children.xml"))
                .matchCss("child4", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("ancestor1 child4", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("ancestor1 child4", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("ancestor1 > parent1 > child4", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("xml parent1 > child4", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("xml parent1 > child4 + child5", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("xml parent1 > child4 ~ child5", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("xml parent1 > child4 ~ child6", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("xml parent1 > child4 ~ child6 grandchild1", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("xml parent1 > child4 + child6", c -> {
                    throw new IllegalStateException();
                })

                .matchCss("xml parent1 > child4 ~ grandchild1", c -> {
                    throw new IllegalStateException();
                })
                .matchCss("ancestor1 > child4", c -> {
                    throw new IllegalStateException();
                })
                .consume();


        Assert.assertEquals(9, integer.get());
    }

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





    @Test
    public void testCss_PseudoClass() throws Exception {
        AtomicInteger integer = new AtomicInteger();

        newConsumerAndClose(new FileInputStream("src/test/resources/pseudoclass/pseudoclass.xml"))
                .matchCss("child3:nth-child(3)", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("child5:nth-of-type(2)", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("child1:first-child", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("child5:first-of-type", c -> {
                    Assert.assertEquals(5, c.getNode().getSiblingIndex());
                    integer.incrementAndGet();
                })
                .matchCss("grandchild1:only-child", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("id#foo", c -> {
                    integer.incrementAndGet();
                })
                .matchCss("child2:first-child", c -> {
                    throw new IllegalStateException();
                })

                .matchCss("ancestor1 > child4", c -> {
                    throw new IllegalStateException();
                })
                .match(css("foo > bar[attr='value']").or(css("foo > bar2[attr='value']")), c -> {

                })

                .consume();


        Assert.assertEquals(6, integer.get());
    }



}
