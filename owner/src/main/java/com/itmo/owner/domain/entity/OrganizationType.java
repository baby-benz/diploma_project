package com.itmo.owner.domain.entity;

public enum OrganizationType {
    ВЛАДЕЛЕЦ, НАДЗОР, СЕРВИС;

    @Override
    public String toString() {
        switch(this) {
            case ВЛАДЕЛЕЦ: return "ВЛАДЕЛЕЦ";
            case НАДЗОР: return "НАДЗОР";
            case СЕРВИС: return "СЕРВИС";
            default: throw new IllegalArgumentException();
        }
    }
}
