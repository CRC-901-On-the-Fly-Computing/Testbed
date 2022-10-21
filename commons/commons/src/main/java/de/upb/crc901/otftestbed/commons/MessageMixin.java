package de.upb.crc901.otftestbed.commons;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public class MessageMixin {

     @ConstructorProperties(value= {"parameters"})
    public MessageMixin(Object...parameters){
        //Intentionally empty
    }

}

