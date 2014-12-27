package core.application.exception;

import java.io.Serializable;

public class ApplicationException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -9212400747742462774L;

    private String message;

    public ApplicationException(ExceptionType type, String param) {
        message = String.format(type.getParameter(), param);
    }
    
    public ApplicationException(ExceptionType type) {
        message = type.getParameter();
    }

    public String getMessage() {
        return message;
    }
    
    
}
