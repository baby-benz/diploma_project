package com.itmo.service.equipment;

public enum SensorType {
    ДАТЧИК_ДАВЛЕНИЯ, ДАТЧИК_ТЕМПЕРАТУРЫ, ДАТЧИК_ОБОРОТОВ;

    @Override
    public String toString() {
        switch(this) {
            case ДАТЧИК_ДАВЛЕНИЯ: return "ДАТЧИК_ДАВЛЕНИЯ";
            case ДАТЧИК_ТЕМПЕРАТУРЫ: return "ДАТЧИК_ТЕМПЕРАТУРЫ";
            case ДАТЧИК_ОБОРОТОВ: return "ДАТЧИК_ОБОРОТОВ";
            default: throw new IllegalArgumentException();
        }
    }
}
