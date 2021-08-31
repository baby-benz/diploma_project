package com.itmo.smartcontract.entity.equipment;

public enum EquipmentType {
    ЦЕНТРОБЕЖНЫЙ_НАСОС, ПОРШНЕВОЙ_НАСОС, КОМПРЕССОР, ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ, СЕПАРАТОР, УЧАСТОК_ТРУБОПРОВОДА, РВС;

    @Override
    public String toString() {
        switch(this) {
            case ЦЕНТРОБЕЖНЫЙ_НАСОС: return "ЦЕНТРОБЕЖНЫЙ_НАСОС";
            case ПОРШНЕВОЙ_НАСОС: return "ПОРШНЕВОЙ_НАСОС";
            case КОМПРЕССОР: return "КОМПРЕССОР";
            case ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ: return "ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ";
            case СЕПАРАТОР: return "СЕПАРАТОР";
            case УЧАСТОК_ТРУБОПРОВОДА: return "УЧАСТОК_ТРУБОПРОВОДА";
            case РВС: return "РВС";
            default: throw new IllegalArgumentException();
        }
    }
}
