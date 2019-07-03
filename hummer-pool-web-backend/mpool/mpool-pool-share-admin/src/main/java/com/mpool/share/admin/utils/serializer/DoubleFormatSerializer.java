package com.mpool.share.admin.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class DoubleFormatSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        double value = (double) o;
        BigDecimal bg = new BigDecimal(Double.toString(value));
        double result = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        jsonGenerator.writeNumber(result);
    }
}
