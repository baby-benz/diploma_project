package com.itmo.smartcontract.ledgerapi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@NoArgsConstructor
public class State {
    protected String key;

    public String[] getSplitKey() {
        return State.splitKey(this.key);
    }

    public static byte[] serialize(Object object) {
        String jsonStr = new JSONObject(object).toString();
        return jsonStr.getBytes(UTF_8);
    }

    public static String makeKey(String[] keyParts) {
        return String.join(":", keyParts);
    }

    public static String[] splitKey(String key) {
        return key.split(":");
    }
}
