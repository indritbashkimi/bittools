package com.ibashkimi.provider.utils;


public interface ValueProducer<T> {
    T get();

    T[] getValues();

    void setValues(T[] values);

    void reset();
}
