package com.itmo.smartcontract.entity.equipment;

public enum MaintenanceStatus {
    КОРРЕКТИРУЮЩЕЕ_ОБСЛУЖИВАНИЕ, ЗАПЛАНИРОВАННОЕ_ОБСЛУЖИВАНИЕ, ОБСЛУЖИВАНИЕ_ПО_СОСТОЯНИЮ;

    @Override
    public String toString() {
        switch(this) {
            case КОРРЕКТИРУЮЩЕЕ_ОБСЛУЖИВАНИЕ: return "КОРРЕКТИРУЮЩЕЕ_ОБСЛУЖИВАНИЕ";
            case ЗАПЛАНИРОВАННОЕ_ОБСЛУЖИВАНИЕ: return "ЗАПЛАНИРОВАННОЕ_ОБСЛУЖИВАНИЕ";
            case ОБСЛУЖИВАНИЕ_ПО_СОСТОЯНИЮ: return "ОБСЛУЖИВАНИЕ_ПО_СОСТОЯНИЮ";
            default: throw new IllegalArgumentException();
        }
    }
}
