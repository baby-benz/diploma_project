package com.itmo.inspection.equipment;

public enum EquipmentState {
    ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ, НОРМАЛЬНАЯ_РАБОТА, РЕЖИМ_ОГРАНИЧЕННОЙ_ФУНКЦИОНАЛЬНОСТИ, НЕИСПРАВНО, ОТКЛЮЧЕНО, ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ;

    @Override
    public String toString() {
        switch(this) {
            case ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ: return "ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ";
            case НОРМАЛЬНАЯ_РАБОТА: return "НОРМАЛЬНАЯ_РАБОТА";
            case РЕЖИМ_ОГРАНИЧЕННОЙ_ФУНКЦИОНАЛЬНОСТИ: return "РЕЖИМ_ОГРАНИЧЕННОЙ_ФУНКЦИОНАЛЬНОСТИ";
            case НЕИСПРАВНО: return "НЕИСПРАВНО";
            case ОТКЛЮЧЕНО: return "ОТКЛЮЧЕНО";
            case ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ: return "ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ";
            default: throw new IllegalArgumentException();
        }
    }
}
