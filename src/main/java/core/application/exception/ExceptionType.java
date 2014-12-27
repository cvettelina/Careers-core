package core.application.exception;

public enum ExceptionType {

    INVALID_PARAMETER("Parameter %s is invalid."),
    MISSING_PARAMETER("Please enter your %s."),
    APPLICATION_ALREADY_SENT("The application is already sent."),
    UNKNOWN_USER("User %s is unknown."),
    BLOCKED_PERSON("You can't send applications."),
    UPDATE_NOT_ALLOWED("Update not allowed"),
    EMAIL_NOT_SENT("The email was not sent"),
    MISSING_EMAIL_CONFIG("Missing email configurations"),
    UNKNOWN_POSITION("The position does not exist"),
    INTERNAL_ERROR("Internal error."),
    UNKNOWN_INTERVIEW("The interview does not exist"),
    UNKNOWN_APPLICATION("The application does not exist.");

    private String parameter;

    private ExceptionType(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

}
