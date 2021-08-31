package com.itmo.smartcontract.ledgerapi;

@FunctionalInterface
public interface StateDeserializer {
    State deserialize(String json);
}
