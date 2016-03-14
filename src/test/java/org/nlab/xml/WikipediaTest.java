package org.nlab.xml;

import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

import org.junit.Test;
import org.nlab.util.Tries;
import org.nlab.xml.stream.XmlStream;
import org.nlab.xml.stream.XmlStreams;

import static org.nlab.xml.stream.XmlStreamSpec.with;
import static org.nlab.xml.stream.util.Transformers.getElementText;

/**
 * Created by nlabrot on 19/12/15.
 */
public class WikipediaTest {

    public static class Page {
        String id;
        String title;
        String lastRevision;
        String lastContributor;

        @Override
        public String toString() {
            return "Page{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", lastRevision='" + lastRevision + '\'' +
                    ", lastContributor='" + lastContributor + '\'' +
                    '}';
        }
    }

    @Test
    public void testWiki() throws Exception {

        try (XmlStream stream = XmlStreams.streamAndClose(new GZIPInputStream(new FileInputStream("src/test/resources/enwiki-latest-pages-articles2.gz")))) {
            stream.css("page")
                    .map(cPage -> {
                        Page page = new Page();
                        with(cPage).uncheckedConsumer()
                                .matchCss("page > title", c -> page.title = getElementText(c.getStreamReader()))
                                .matchCss("page > id", c -> page.id = getElementText(c.getStreamReader()))
                                .matchCss("revision > timestamp", c -> page.lastRevision = getElementText(c.getStreamReader()))
                                .matchCss("revision > contributor > username", c -> page.lastContributor = getElementText(c.getStreamReader()))
                                .consume();
                        return page;
                    })
                    .forEach(p->{});

        }
    }

}
