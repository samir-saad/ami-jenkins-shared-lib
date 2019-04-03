package org.ssaad.ami.pipeline.utils

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class ObjectUtils {

    public static Object clone(Object object){
        return new JsonSlurper().parseText(new JsonBuilder(object).toPrettyString())
    }
}
