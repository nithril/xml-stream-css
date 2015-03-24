StAX CSS Matcher
==================

The Streaming API for XML (StAX) is fast but at the cost of lake of context (previous node / attribute...) and matcher. 
Matching an xml path barely more complex than `parent/child` is cumbersome and imply to implement context saving and matching. 



Element matcher
------------------

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
XmlMatcherConsumer.newConsumeAndClose(new FileInputStream('foo.xml'))
        .matchElement("foo" , c -> {})
        .consume();
```


CSS matcher
------------------

Thinks get tough when the path is not limited to one element and include attributes `/foo/bar[attr='value']`


```java
try(InputStream is = new FileInputStream('foo.xml')){
    XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
    XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(is);
    XmlPath xmlPath = new XmlPath();
    while(reader.hasNext()){
        if (START_ELEMENT == reader.getEventType()){
            xmlPath.push(reader.getLocalName());
            if ("/foo/bar".equals(xmlPath.toString()) && "value".equals(reader.getAttributeValue(null, "attr"))) {

            }
        }
        reader.next();
    }
}
```

The CSS matcher allows to keep the code focused 

```java
XmlMatcherConsumer.newConsumeAndClose(new FileInputStream('foo.xml'))
        .matchCss("foo > bar[attr='value']" , c -> {})
        .consume();
```


Predicates
------------------


But in fact all matchers are Java 8 [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html) that can be combined.
 
 
```java
XmlMatcherConsumer.newConsumeAndClose(new FileInputStream('foo.xml'))
        .match(css("foo > bar[attr='value']").or(css("foo > bar2[attr='value']")), c -> {})
        .consume();
```


Supported selectors (from Jodd documentation)
=========================

Only forward selectors are supported. For obvious reason backward selectors cannot be supported.


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
* `E:only-child` an E element, only child of its parent
* `E:only-of-type` an E element, only sibling of its type
* `E:empty` an E element that has no children (including text nodes)
* `E#myid` an E element with ID equal to “myid”.
* `E F` an F element descendant of an E element
* `E > F` an F element child of an E element
* `E + F` an F element immediately preceded by an E element
* `E ~ F` an F element preceded by an E element

