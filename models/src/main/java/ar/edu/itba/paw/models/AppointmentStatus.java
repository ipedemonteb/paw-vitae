package ar.edu.itba.paw.models;


public enum AppointmentStatus {
    COMPLETO("completo"),
    CONFIRMADO("confirmado"),
    CANCELADO("cancelado");

    private final String value;

    AppointmentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
