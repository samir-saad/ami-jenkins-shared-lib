package org.ssaad.ami.pipeline.config.objects

class Stage implements Serializable{

    boolean enable = true
    boolean requiresConfirmation = false
    String id
    String name

    String engine = ""
    String goals = ""
    String params = ""
    String configDir = ""
    String credentials = ""

    Stage(){

    }
}
