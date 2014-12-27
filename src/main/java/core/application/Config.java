package core.application;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import core.application.entities.ConfigEntity;

@Stateless
public class Config {

    private static final Long ONE_HOUR = new Long(1000 * 60 * 60);
    private static Long time = 0L;

    @PersistenceContext
    EntityManager em;

    private static volatile Map<String, String> configurations;

    public String getValue(String key) {
        Long currentTime = (new Date()).getTime();
        if (configurations == null || (currentTime - time) > ONE_HOUR) {
            loadCache();
        }
        return configurations.get(key);
    }

    @SuppressWarnings("unchecked")
    private void loadCache() {
        Query query = em.createNamedQuery(ConfigEntity.GET_ALL);
        List<ConfigEntity> result = query.getResultList();
        if (!result.isEmpty()) {
            configurations = new HashMap<String, String>();
            for (ConfigEntity entity : result) {
                configurations.put(entity.getName(), entity.getValue());
            }
        }
        time = (new Date()).getTime();
    }

}
