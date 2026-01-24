package ar.edu.itba.paw.models;

public enum SlotStatus {
    AVAILABLE("disponible"),
    UNAVAILABLE("ocupado");
    private final String value;
    SlotStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

}