package com.itmo.accounting.domain.entity;

public enum OrganizationType {
    ВЛАДЕЛЕЦ, ЭКСПЛУАТАНТ, НАДЗОР, СЕРВИС;

    @Override
    public String toString() {
        switch(this) {
            case ВЛАДЕЛЕЦ: return "ВЛАДЕЛЕЦ";
            case ЭКСПЛУАТАНТ: return "ЭКСПЛУАТАНТ";
            case НАДЗОР: return "НАДЗОР";
            case СЕРВИС: return "СЕРВИС";
            default: throw new IllegalArgumentException();
        }
    }
}
