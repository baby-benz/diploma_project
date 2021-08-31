package com.itmo.owner.configuration;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.annotation.HandleNull;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

import java.time.LocalDate;

@HandleNull
public class LocalDateConverter implements Converter<LocalDate> {
    @Override
    public void serialize(LocalDate localDate, ObjectWriter objectWriter, Context context) {
        if(localDate == null) {
            objectWriter.writeValue("");
            return;
        }
        objectWriter.writeValue(localDate.toString());
    }

    @Override
    public LocalDate deserialize(ObjectReader objectReader, Context context) {
        String value = objectReader.valueAsString();
        if(value.isEmpty()) {
            return LocalDate.MAX;
        } else {
            return LocalDate.parse(value);
        }
    }
}
