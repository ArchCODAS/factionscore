package com.fyzermc.factionscore.util.contracts;

public interface Provider<T> {

    void prepare();

    T provide();

    void shut();
}