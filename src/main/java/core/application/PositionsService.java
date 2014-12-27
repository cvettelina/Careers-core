package core.application;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import core.application.entities.PositionEntity;
import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;
import core.application.validations.CommonValidation;

import api.application.Positions;
import api.application.request.Position;
import api.application.response.PositionResponse;

@Stateless
public class PositionsService implements Positions {

    @PersistenceContext
    EntityManager em;

    @EJB
    CommonValidation commonValidation;

    @Override
    public void create(Position request) {
        commonValidation.validateNotNull(request, "request");
        commonValidation.validateName(request.getTitle(), "title");
        commonValidation.validateNotNull(request.getDescription(), "description");
        String safedesc = commonValidation.toSafeHtml(request.getDescription());
        PositionEntity entity = new PositionEntity(request.getTitle(), safedesc);
        em.persist(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<PositionResponse> getAll() {
        Collection<PositionEntity> entities;
        Query query = em.createNamedQuery(PositionEntity.GET_ALL);
        entities = query.getResultList();
        Collection<PositionResponse> response = new ArrayList<PositionResponse>();
        for (PositionEntity entity : entities) {
            response.add(new PositionResponse(entity.getTitle(), entity.getDescription(), entity.getId()));
        }
        return response;
    }

    @Override
    public PositionResponse getById(Long positionId) {
        PositionEntity entity = em.find(PositionEntity.class, positionId);
        if (entity == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_POSITION);
        }
        return new PositionResponse(entity.getTitle(), entity.getDescription(), entity.getId());
    }

    @Override
    public void delete(Long positionId) {
        PositionEntity entity = em.find(PositionEntity.class, positionId);
        if (entity == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_POSITION);
        }
        em.remove(entity);
    }

    @Override
    public void edit(Long positionId, Position position) {
        PositionEntity entity = em.find(PositionEntity.class, positionId);
        if (entity == null) {
            throw new ApplicationException(ExceptionType.UNKNOWN_POSITION);
        }

        boolean hasChanges = false;
        if (position.getTitle() != null) {
            commonValidation.validateName(position.getTitle(), "title");
            entity.setTitle(position.getTitle());
            hasChanges = true;
        }
        if (position.getDescription() != null) {
            String safedesc = commonValidation.toSafeHtml(position.getDescription());
            entity.setDescription(safedesc);
            hasChanges = true;
        }
        if (hasChanges) {
            em.merge(entity);
        }
    }

}
