package org.ssaad.ami.pipeline.test

import com.fasterxml.jackson.databind.ObjectMapper

class Test {
    void test(){
        ObjectMapper objectMapper = new ObjectMapper()

        Animal myDog = new Dog("ruffus","english shepherd")

        Animal myCat = new Cat("goya", "mice")

        try {
            String dogJson = objectMapper.writeValueAsString(myDog)

            System.out.println(dogJson)

            Animal deserializedDog = objectMapper.readValue(dogJson, Animal.class)

            System.out.println("Deserialized dogJson Class: " + deserializedDog.getClass().getSimpleName())

            String catJson = objectMapper.writeValueAsString(myCat)

            Animal deseriliazedCat = objectMapper.readValue(catJson, Animal.class)

            System.out.println("Deserialized catJson Class: " + deseriliazedCat.getClass().getSimpleName())



        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    public static void main(String[] args) {

        new Test().test()

    }
}
