package com.ibashkimi.provider.utils;

import androidx.annotation.NonNull;


public class ValueProducerImpl<T> implements ValueProducer<T> {
    @NonNull
    protected T[] values; // Note: must be at least 1 element long.
    int index;

    public ValueProducerImpl(@NonNull T[] values) {
        this.values = values;
        this.index = -1; // get() starts with incrementing
    }

    @Override
    public T get() {
        index = (index == values.length - 1) ? 0 : index + 1;
        return values[index];
    }

    @Override
    public T[] getValues() {
        return values;
    }

    @Override
    public void setValues(@NonNull T[] values) {
        this.values = values;
    }

    @Override
    public void reset() {
        this.index = 0;
    }

}
