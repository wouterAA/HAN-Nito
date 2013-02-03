package nl.hannito.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.hannito.entity.DifficultyLevel;

@Stateless
public class DifficultyLevelPostgresRepository extends RESTFacade<DifficultyLevel> {

    @PersistenceContext private EntityManager em;

    public DifficultyLevelPostgresRepository() {
        super(DifficultyLevel.class);
    }

    @Override
    public DifficultyLevel find(Object id) {
        List<DifficultyLevel> foundDifficultyLevels = getEntityManager()
                .createNamedQuery("DifficultyLevel.findLowercase", DifficultyLevel.class)
                .setParameter("difficultylevel", id).getResultList();
        if (foundDifficultyLevels.size() > 0) {
            return foundDifficultyLevels.get(0);
        }
        return null;
    }
    
    public DifficultyLevel edit(DifficultyLevel oldDifficultyLevel, DifficultyLevel newDifficultyLevel) {
        if (oldDifficultyLevel != null) {
            super.remove(oldDifficultyLevel);
            getEntityManager().flush();
            return super.create(newDifficultyLevel);
        }
        return null;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
