package org.nlab.xml.stream.context;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;


public class UserContext {

    private final Deque<Map<String, Object>> hierarchicalProperties = new LinkedList<>();

    public <T> Optional<T> findProperty(String name) {
        return (Optional<T>)hierarchicalProperties.stream().filter(m -> m.containsKey(name))
                .map(m -> m.get(name))
                .findFirst();
    }

    public <T> Optional<T> findProperty(String name, Class<T> clazz) {
        return hierarchicalProperties.stream().filter(m -> m.containsKey(name)).findFirst()
                .map(m -> m.get(name))
                .map(clazz::cast);
    }

    public UserContext setProperty(String name, Object value) {
        hierarchicalProperties.getFirst().put(name , value);
        return this;
    }

    public UserContext setRootProperty(String name, Object value) {
        hierarchicalProperties.getLast().put(name , value);
        return this;
    }

    public Map<String,Object> flatten(){
        return hierarchicalProperties.stream().reduce((l, r) -> {
            Map<String, Object> result = new HashMap<>(r);
            result.putAll(l);
            return result;
        }).get();
    }



    public void push() {
        hierarchicalProperties.addFirst(new HashMap<>());
    }

    public void pop() {
        hierarchicalProperties.removeFirst();
    }
}
