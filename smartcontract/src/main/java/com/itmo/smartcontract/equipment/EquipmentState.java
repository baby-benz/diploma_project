package com.itmo.smartcontract.equipment;

public enum EquipmentState {
    ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ, НОРМАЛЬНАЯ_РАБОТА, DEGRADED_РАБОТА, СЛОМАНО, ОТКЛЮЧЕНО, ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ;

    @Override
    public String toString() {
        switch(this) {
            case ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ: return "ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ";
            case НОРМАЛЬНАЯ_РАБОТА: return "НОРМАЛЬНАЯ_РАБОТА";
            case DEGRADED_РАБОТА: return "DEGRADED_РАБОТА";
            case СЛОМАНО: return "СЛОМАНО";
            case ОТКЛЮЧЕНО: return "ОТКЛЮЧЕНО";
            case ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ: return "ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ";
            default: throw new IllegalArgumentException();
        }
    }
}
