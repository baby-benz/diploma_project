package com.itmo.accounting.configuration;
import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.annotation.HandleNull;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

@HandleNull
public class DoubleConverter implements Converter<Double> {
    @Override
    public void serialize(Double number, ObjectWriter objectWriter, Context context) {
        if(number == null) {
            objectWriter.writeNull();
            return;
        }
        objectWriter.writeValue(number);
    }

    @Override
    public Double deserialize(ObjectReader objectReader, Context context) {
        return objectReader.valueAsDouble();
    }
}
