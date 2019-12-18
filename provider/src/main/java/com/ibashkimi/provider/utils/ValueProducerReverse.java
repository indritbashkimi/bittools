package com.ibashkimi.provider.utils;


public class ValueProducerReverse<T> extends ValueProducerImpl<T> {
    private boolean reversing;

    public ValueProducerReverse(T[] values) {
        super(values);
        reversing = false;
        index = 0;
    }

    @Override
    public T get() {
        return values[nextIndex()];
    }

    private int nextIndex() {
        if (reversing) {
            if (index == 0) {
                index = 1;
                reversing = false;
            } else {
                index--;
            }
        } else {
            if (index == values.length - 1) {
                index = values.length - 1;
                reversing = true;
            } else {
                index++;
            }
        }
        return index;
    }
}
