package ar.edu.itba.paw.models.exception;

public class NeighborhoodNotFoundException extends CustomRuntimeException {


    public NeighborhoodNotFoundException() {
        super("exception.neighborhoodNotFound",ExceptionUtils.NOT_FOUND_STATUS_CODE);    }
}
