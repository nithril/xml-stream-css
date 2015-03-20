StAX CSS Matcher
==================

The Streaming API for XML (StAX) is fast but at the cost of lake of context (previous node / attribute...) and matcher. 
Matching an xml path barely more complex than `parent/child` is cumbersome and imply to implement context saving and matching. 


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
