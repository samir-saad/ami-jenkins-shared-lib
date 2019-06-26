package org.ssaad.ami.pipeline;

public interface AbstractFactory<T> {

    T create(String type) ;
}