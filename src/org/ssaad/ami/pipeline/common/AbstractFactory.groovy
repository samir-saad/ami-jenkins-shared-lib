package org.ssaad.ami.pipeline.common

interface AbstractFactory<T> {

    T create(String type)
}
