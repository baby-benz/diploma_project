package com.itmo.smartcontract.ledgerapi;

import com.itmo.smartcontract.LocalDateConverter;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class State {
    protected String key;
    public final static Genson GENSON = new GensonBuilder().withConverter(new LocalDateConverter(), LocalDate.class).create();

    public String[] getSplitKey() {
        return State.splitKey(this.key);
    }

    /*public static String serialize(Object object) {
        return new JSONObject(object).toString();
    }*/

    public static String makeKey(String[] keyParts) {
        return String.join(":", keyParts);
    }

    public static String[] splitKey(String key) {
        return key.split(":");
    }
}
