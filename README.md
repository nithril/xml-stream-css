# XML Streaming CSS Matcher


The Streaming API for XML (StAX) is fast but at the cost of lack of context (previous node / attribute...) and matcher. 
Matching an xml path barely more complex than `parent/child` is cumbersome and imply to implement context saving and matching. 


# Requirements

Java 1.7

# Maven dependency

Search for the latest version on [Maven central](http://search.maven.org/#search|ga|1|g%3A%22com.github.nithril%22%20a%3A%22xml-stream-css%22):

eg.:

```xml
<dependency>
    <groupId>com.github.nithril</groupId>
    <artifactId>xml-stream-css</artifactId>
    <version>1.0.0</version>
</dependency>
```

# How to use it

## Element matcher


The following code:

```java

try(InputStream is = new FileInputStream('foo.xml')){
    XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
    XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(is);
    while(reader.hasNext()){
        if (START_ELEMENT == reader.getEventType() && "foo".equals(reader.getLocalName())){
            //Do something
        }
        reader.next();
    }
}
```

Can be replaced with a more friendly lambda based push approach:

```java
XmlStreams.newConsumer("foo.xml")
        .matchElement("foo" , c -> {})
        .consume();
```


## CSS matcher


Things can get tough when the path is not limited to one element and include attributes `/foo/bar[attr='value']`

Pseudo java code:

```java
try(InputStream is = new FileInputStream('foo.xml')){
    XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
    XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(is);
    XmlPath xmlPath = new XmlPath();
    while(reader.hasNext()){
        if (START_ELEMENT == reader.getEventType()){
            xmlPath.push(reader.getLocalName());
            if ("/foo/bar".equals(xmlPath.toString()) && "value".equals(reader.getAttributeValue(null, "attr"))) {
                //Do something
            }
        }
        reader.next();
    }
}
```

The CSS matcher allows to keep the code clean and focused:

```java
XmlStreams.newConsumer("foo.xml")
        .matchCss("foo > bar[attr='value']" , c -> {})
        .consume();
```



## Java 8 Stream


XML can be streamed using the Java 8 stream:

```java
try (Stream<StaxContext> stream = XmlStreams.stream("foo.xml")) {
    String value = stream
            .css("foo")
            .map(c -> c.getText())
            .findFirst().get();
}
```


## Predicates


All matchers are Java 8 [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html) that can be combined.


```java
XmlStreams.newConsumerAndClose(new FileInputStream('foo.xml'))
        .match(css("foo > bar[attr='value']").or(css("foo > bar2[attr='value']")), c -> {})
        .consume();
```

See the [Predicates helper](org/nlab/xml/stream/predicate/Predicates.java) for the list of supported predicates.




## Nested Consumer and Streamer


Consumer and Streamer can be nested.

In the following example, a first stream match all the wikipedia page tag. Starting from this tag, 
a nester consumer extract the title, id, timestamp and contributor name:

```java
    try (InputStream fis = new FileInputStream("src/test/resources/enwiki-latest-pages-articles2.gz");
         XmlStream stream = XmlStreams.streamAndClose(new GZIPInputStream(fis))) {

        stream.css("page")
                .map(context -> {
                    Page page = new Page();
                    context.partialConsumer()
                            .matchCss("page > title", c -> page.title = getElementText(c.getStreamReader()))
                            .matchCss("page > id", c -> page.id = getElementText(c.getStreamReader()))
                            .matchCss("revision > timestamp", c -> page.lastRevision = getElementText(c.getStreamReader()))
                            .matchCss("revision > contributor > username", c -> page.lastContributor = getElementText(c.getStreamReader()))
                            .consume();
                    return page;
                })
                .forEach(p -> p.toString());
    }
```

## Supported selectors


CSS implementation comes from the [Jodd CSSelly project](http://jodd.org/doc/csselly/). 
Only forward selectors are supported, for obvious reason backward selectors cannot be supported.


* `*` any element
* `E` an element of type E
* `E[foo]` an E element with a "foo" attribute
* `E[foo="bar"]` an E element whose "foo" attribute value is exactly equal to "bar"
* `E[foo~="bar"]` an E element whose "foo" attribute value is a list of whitespace-separated values, one of which is exactly equal to "bar"
* `E[foo^="bar"]` an E element whose "foo" attribute value begins exactly with the string "bar"
* `E[foo$="bar"]` an E element whose "foo" attribute value ends exactly with the string "bar"
* `E[foo*="bar"]` an E element whose "foo" attribute value contains the substring "bar"
* `E[foo|="en"]` an E element whose "foo" attribute has a hyphen-separated list of values beginning (from the left) with "en"
* `E:root` an E element, root of the document
* `E:nth-child(n)` an E element, the n-th child of its parent
* NOT SUPPORTED `E:nth-last-child(n)` an E element, the n-th child of its parent, counting from the last one
* `E:nth-of-type(n)` an E element, the n-th sibling of its type
* NOT SUPPORTED `E:nth-last-of-type(n)` an E element, the n-th sibling of its type, counting from the last one
* `E:first-child` an E element, first child of its parent
* NOT SUPPORTED `E:last-child` an E element, last child of its parent
* `E:first-of-type` an E element, first sibling of its type
* NOT SUPPORTED `E:last-of-type` an E element, last sibling of its type
* NOT SUPPORTED `E:only-child` an E element, only child of its parent
* NOT SUPPORTED `E:only-of-type` an E element, only sibling of its type
* `E:empty` an E element that has no children (including text nodes)
* `E#myid` an E element with ID equal to “myid”.
* `E F` an F element descendant of an E element
* `E > F` an F element child of an E element
* `E + F` an F element immediately preceded by an E element
* `E ~ F` an F element preceded by an E element

