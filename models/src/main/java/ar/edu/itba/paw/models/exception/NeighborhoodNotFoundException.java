package ar.edu.itba.paw.models.exception;

public class NeighborhoodNotFoundException extends RuntimeException {

    public NeighborhoodNotFoundException(String message) {
        super(message);
    }

    public NeighborhoodNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NeighborhoodNotFoundException(final Throwable cause) {
        super(cause);
    }

    public NeighborhoodNotFoundException() {
        super("Neighborhood not found");
    }
}
