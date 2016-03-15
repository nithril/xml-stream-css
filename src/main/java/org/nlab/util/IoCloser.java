package org.nlab.util;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jooq.lambda.fi.lang.CheckedRunnable;


public class IoCloser {

    private final List<CompletableFuture> completableFutures = new ArrayList<>();

    public IoCloser close(Closeable... streams) {
        for (Closeable stream : streams) {
            silentClose(stream::close);
        }
        return this;
    }

    public IoCloser close(XMLStreamReader... streams) {
        for (XMLStreamReader stream : streams) {
            silentClose(stream::close);
        }
        return this;
    }

    public IoCloser close(XMLStreamWriter... streams) {
        for (XMLStreamWriter stream : streams) {
            silentClose(stream::close);
        }
        return this;
    }

    public IoCloser close(Object... streams) {
        for (Object object : streams) {
            if (object instanceof Closeable){
                this.close((Closeable)object);
            }
            else if (object instanceof XMLStreamReader){
                this.close((XMLStreamReader)object);
            }
            else if (object instanceof XMLStreamWriter){
                this.close((XMLStreamWriter)object);
            }
        }
        return this;
    }

    /**
     * Get the result or throw exception if any occurs
     *
     * @throws Exception
     */
    public boolean get() throws Exception {
        try {
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[]{})).get();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public static IoCloser ioCloser() {
        return new IoCloser();
    }

    public static Runnable promiseIoCloser(Object...objects){
        return () -> IoCloser.ioCloser().close(objects);
    }



    private void silentClose(CheckedRunnable c) {
        CompletableFuture completableFuture = new CompletableFuture();
        try {
            c.run();
            completableFuture.complete(true);
        } catch (Throwable e) {
            completableFuture.completeExceptionally(e);
        }
        completableFutures.add(completableFuture);
    }

}
