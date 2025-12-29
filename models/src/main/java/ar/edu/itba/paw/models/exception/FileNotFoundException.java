package ar.edu.itba.paw.models.exception;

public class FileNotFoundException extends CustomRuntimeException{



    public FileNotFoundException() {
        super("exception.fileNotFound",ExceptionUtils.NOT_FOUND_STATUS_CODE);
    }


}
