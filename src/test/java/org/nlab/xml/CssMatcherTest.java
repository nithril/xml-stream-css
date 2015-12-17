package org.nlab.xml;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.nlab.xml.stream.consumer.XmlConsumer;
import org.nlab.xml.stream.context.StreamContext;
import org.nlab.xml.stream.predicate.Predicates;

import com.google.common.collect.ImmutableList;
import javaslang.Tuple;
import javaslang.Tuple2;

import static org.nlab.xml.stream.XmlStreams.newConsumer;
import static org.nlab.xml.stream.XmlStreams.newConsumerAndClose;
import static org.nlab.xml.stream.predicate.Predicates.css;

/**
 * Created by nlabrot on 12/03/15.
 */
public class CssMatcherTest extends AbstractTest {

    @Test
    public void testCss_Root() throws Exception {

        List<Tuple2<Predicate<StreamContext>,Integer>> list = ImmutableList.of(
                Tuple.of(Predicates.css(":root") , 1),
                Tuple.of(Predicates.css("*:root" ), 1),
                Tuple.of(Predicates.css("rootElement:root" ), 1),
                Tuple.of(Predicates.css("foo:root" ), 0)
        );

        test(list , "src/test/resources/css/root/root.xml");
    }

    @Test
    public void testCss_Attributes() throws Exception {
        List<Tuple2<Predicate<StreamContext>,Integer>> list = ImmutableList.of(
                Tuple.of(Predicates.css("a[testExist]" ), 1),
                Tuple.of(Predicates.css("a[testNotExist]" ), 0),
                Tuple.of(Predicates.css("a[testEquals=\"ok\"]" ), 1),
                Tuple.of(Predicates.css("a[testEquals=\"okok\"]" ), 0),
                Tuple.of(Predicates.css("a[testContains*=\"ips\"]" ), 1),
                Tuple.of(Predicates.css("a[testContains*=\"ipsd\"]" ), 0)
        );
        test(list , "src/test/resources/css/attribute/attribute.xml");
    }


    @Test
    public void testCss_Ancestor() throws Exception {

        List<Tuple2<Predicate<StreamContext>,Integer>> list = ImmutableList.of(
                Tuple.of(Predicates.css("child4" ), 2),
                Tuple.of(Predicates.css("ancestor1 child4" ), 1),
                Tuple.of(Predicates.css("ancestor1 > parent1 > child4" ), 1),
                Tuple.of(Predicates.css("xml parent1 > child4" ), 1),
                Tuple.of(Predicates.css("xml parent1 > child4 + child5" ), 1),
                Tuple.of(Predicates.css("xml parent1 > child4 + child5 + child6" ), 1),
                Tuple.of(Predicates.css("xml parent1 > child4 ~ child5" ), 1),
                Tuple.of(Predicates.css("xml parent1 > child4 ~ child6" ), 1),
                Tuple.of(Predicates.css("xml parent1 > child1 ~ child3 ~ child5" ), 1),
                Tuple.of(Predicates.css("xml parent1 > child4 ~ child6 grandchild1" ), 1),
                Tuple.of(Predicates.css("xml parent1 > child4 + child6" ), 0),
                Tuple.of(Predicates.css("xml parent1 > child6 ~ child4" ), 0),
                Tuple.of(Predicates.css("xml parent1 > child4 ~ grandchild1" ), 0),
                Tuple.of(Predicates.css("xml parent1 > child6 ~ grandchild1" ), 0),
                Tuple.of(Predicates.css("ancestor1 > child4" ), 0),


                Tuple.of(Predicates.css("child4 ~ child6" ), 2),
                Tuple.of(Predicates.css("xml grandchild1" ), 2)
        );
        test(list , "src/test/resources/css/children/children.xml");
    }


    @Test
    public void testCss_PseudoClass() throws Exception {

        List<Tuple2<Predicate<StreamContext>,Integer>> list = ImmutableList.of(
                Tuple.of(Predicates.css("child3:nth-child(2)" ), 0),
                Tuple.of(Predicates.css("child3:nth-child(3)" ), 1),
                Tuple.of(Predicates.css("child3:nth-child(4)" ), 1),
                Tuple.of(Predicates.css("child3:nth-child(5)" ), 0),

                Tuple.of(Predicates.css("child5:nth-of-type(2)" ), 1),

                Tuple.of(Predicates.css("child1:first-child" ), 1),
                Tuple.of(Predicates.css("child2:first-child" ), 0),

                Tuple.of(Predicates.css("child5:first-of-type" ), 1),

                Tuple.of(Predicates.css("grandchild2" ), 1),

                Tuple.of(Predicates.css("id#foo" ), 1)
        );

        test(list , "src/test/resources/css/pseudoclass/pseudoclass.xml");
    }

}
