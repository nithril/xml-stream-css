package org.nlab.util.with;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

import static org.nlab.util.with.With.with;
import static org.nlab.util.with.WithResource.withResource;

/**
 * Created by nlabrot on 10/03/15.
 */
public class WithResourceTest {

    @Test
    public void testWithResource() throws Exception {

        //Proposal 1
        String result1 = withResource(getClass().getResourceAsStream("/foo.txt")).uncheckedApply(IOUtils::toString);

        //Proposal 2
        String result2 = withResource(getClass().getResourceAsStream("/foo.txt"), (InputStream s) -> IOUtils.toString(s));


        //Usual pattern
        String result3;
        try (InputStream stream = getClass().getResourceAsStream("/foo.txt")) {
            result2 = IOUtils.toString(stream);
        }


    }

    @Test
    public void testWith() {

        int value = ThreadLocalRandom.current().nextInt();

        //Proposal 1
        boolean result1 = with(value).uncheckedApply(v -> {
            if (v == 0) {
                return true;
            } else {
                return false;
            }
        });

        //Proposal 2
        boolean result2 = with(value, v -> {
            if (v == 0) {
                return true;
            } else {
                return false;
            }
        });

        //Proposal 3
        boolean result3 = false;
        if (value == 0) {
            result3 = true;
        }

        System.out.println(result3);
    }
}
