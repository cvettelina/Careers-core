package core.application;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.PathParam;

import core.application.builders.ResponseBuilder;
import core.application.entities.UserEntity;
import core.application.validations.UserValidation;
import api.application.Users;
import api.application.request.ChangePasswordRequest;
import api.application.request.User;
import api.application.response.UserResponse;

@Stateless
public class UsersService implements Users {

    @EJB
    UserValidation userValidation;

    @EJB
    UsersComponent usersComponent;

    @Override
    public void create(User request) {
        userValidation.validate(request);
        usersComponent.createUser(request);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        usersComponent.changePassword(userId, request);
    }

    @Override
    public void changeStatus(Long userId) {

    }

    @Override
    public UserResponse getUser(@PathParam("userId") Long userId) {
        UserEntity entity = usersComponent.getUserEntityById(userId);
        if (entity == null) {
            return null;
        }
        ResponseBuilder builder = new ResponseBuilder();
        return builder.buildUserResponse(entity);
    }

}
