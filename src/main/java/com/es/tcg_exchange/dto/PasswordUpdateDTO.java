package com.es.tcg_exchange.dto;

public class PasswordUpdateDTO {
    private String passwordActual;
    private String passwordNueva;
    private String passwordNueva2;

    public PasswordUpdateDTO() {
    }

    public PasswordUpdateDTO(String passwordActual, String passwordNueva, String passwordNueva2) {
        this.passwordActual = passwordActual;
        this.passwordNueva = passwordNueva;
        this.passwordNueva2 = passwordNueva2;
    }

    public String getPasswordActual() {
        return passwordActual;
    }

    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    public String getPasswordNueva() {
        return passwordNueva;
    }

    public void setPasswordNueva(String passwordNueva) {
        this.passwordNueva = passwordNueva;
    }

    public String getPasswordNueva2() {
        return passwordNueva2;
    }

    public void setPasswordNueva2(String passwordNueva2) {
        this.passwordNueva2 = passwordNueva2;
    }
}
