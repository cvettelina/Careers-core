package core.application;

import java.security.InvalidParameterException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;

import core.application.validations.ApplicationValidationComponent;
import core.application.validations.CommonValidation;

import api.application.Applications;
import api.application.request.Application;
import api.application.request.ChangeStatusRequest;
import api.application.request.Degree;
import api.application.request.Status;
import api.application.response.ApplicationResponse;

@Stateless
public class ApplicationsService implements Applications {

    private static final String BASE_QUERY = "SELECT pe FROM PersonEntity pe WHERE 1=1 ";

    @EJB
    private ApplicationValidationComponent applicationValidation;

    @EJB
    private ApplicationsComponent applicationComponent;
    
    @EJB
    private CommonValidation commonValidation;

    @Override
    public void apply(Application request, HttpServletRequest httpReqest) {
        applicationValidation.validate(request);
        String ipAddress = httpReqest.getRemoteAddr();
        applicationComponent.saveApplication(request, ipAddress);
    }

    @Override
    public ApplicationResponse getById(Long applicationId) {
        commonValidation.validateNotNull(applicationId, "applicationId");
        return applicationComponent.getById(applicationId);
    }

    @Override
    public List<ApplicationResponse> getByFilter(Long position, Integer period, Status status, Degree degree) {
        StringBuilder stringBuilder = new StringBuilder();

        if (status != null) {
            stringBuilder.append("AND pe.status = :status ");
        }
        if (degree != null) {
            stringBuilder.append("AND pe.qualification = :degree ");
        }
        if (period != null) {
            stringBuilder.append("AND pe.totalExperience >= :period ");
        }
        String finalQuery = BASE_QUERY.concat(stringBuilder.toString());
        return applicationComponent.getApplications(finalQuery, position, period, status, degree);
    }

    @Override
    public void changeStatus(Long applicationId, ChangeStatusRequest request) {
        if (Status.PENDING.equals(request.getStatus())) {
            throw new InvalidParameterException();
        }
        applicationComponent.changeStatus(applicationId, request.getStatus(), request.getUserId());
    }

}
