package ar.edu.itba.paw.models.exception; // O tu paquete de excepciones base




public abstract class CustomRuntimeException extends RuntimeException {

    private final String messageKey;
    private final int status;

    public CustomRuntimeException(String messageKey, int status) {
        super(messageKey);
        this.messageKey = messageKey;
        this.status = status;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public int getStatus() {
        return status;
    }
}