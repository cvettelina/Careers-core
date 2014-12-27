package core.application.validations;

import java.io.InputStream;

import javax.ejb.Stateless;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;

@Stateless
public class CommonValidation {

    public static final Integer NAME_MAX_LENGTH = 50;
    public static final String NAME_PATTERN = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    public static final String NAME_WITH_DIGITS_PATTERN = "^[a-zA-Z0-9]+(?:[\\s-][a-zA-Z0-9]+)*$";
    public static final Integer EMAIL_MAX_LENGTH = 100;
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public void validateNotNull(Object parameter, String name) throws ApplicationException {
        if (parameter == null) {
            throw new ApplicationException(ExceptionType.MISSING_PARAMETER, name);
        }
        if (parameter instanceof String) {
            if ("".equals(parameter)) {
                throw new ApplicationException(ExceptionType.MISSING_PARAMETER, name);
            }
        }
    }

    public void validateName(String name, String paramName) {
        validateNotNull(name, paramName);
        if (!name.matches(NAME_PATTERN) || name.length() > NAME_MAX_LENGTH) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, paramName);
        }

    }
    

    public void validateNameWithDigits(String name, String paramName) {
        validateNotNull(name, paramName);
        if (!name.matches(NAME_WITH_DIGITS_PATTERN) || name.length() > NAME_MAX_LENGTH) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, paramName);
        }

    }

    public String toSafeHtml(String html) {
        Policy policy;
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("antisamy-ebay-1.4.4.xml");
            policy = Policy.getInstance(is);
        } catch (PolicyException e) {
            return null;
        }
        AntiSamy antiSamy = new AntiSamy();
        CleanResults cleanResults;
        try {
            cleanResults = antiSamy.scan(html, policy);
        } catch (ScanException e) {
            return null;
        } catch (PolicyException e) {
            return null;
        }
        return cleanResults.getCleanHTML().trim();
    }

    public void validateEmail(String email) {
        validateNotNull(email, "email");
        if (!email.matches(EMAIL_PATTERN) || email.length() > EMAIL_MAX_LENGTH) {
            throw new ApplicationException(ExceptionType.INVALID_PARAMETER, "email");
        }
    }

}
