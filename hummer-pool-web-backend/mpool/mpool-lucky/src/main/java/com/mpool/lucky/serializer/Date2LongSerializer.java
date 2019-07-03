package com.mpool.lucky.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/8 12:14
 */
public class Date2LongSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Date date = (Date)o;
        if(date != null){
            Long time = date.getTime() / 1000;
            jsonGenerator.writeNumber(time);
        }
    }
}
