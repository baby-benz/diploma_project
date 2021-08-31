package com.itmo.accounting.configuration;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.annotation.HandleNull;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

@HandleNull
public class StringConverter implements Converter<String> {
    @Override
    public void serialize(String string, ObjectWriter objectWriter, Context context) {
        if(string == null) {
            objectWriter.writeValue("");
            return;
        }
        objectWriter.writeValue(string);
    }

    @Override
    public String deserialize(ObjectReader objectReader, Context context) {
        return objectReader.valueAsString();
    }
}
