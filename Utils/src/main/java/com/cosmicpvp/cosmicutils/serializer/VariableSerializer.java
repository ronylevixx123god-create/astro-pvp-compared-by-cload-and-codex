package com.cosmicpvp.cosmicutils.serializer;

public interface VariableSerializer<T> {

    String serialize(T obj);

    T deserialize(String s);

}
