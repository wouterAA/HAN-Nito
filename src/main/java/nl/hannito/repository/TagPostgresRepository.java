package nl.hannito.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.hannito.entity.Tag;

@Stateless
public class TagPostgresRepository extends RESTFacade<Tag> {

    @PersistenceContext private EntityManager em;

    public TagPostgresRepository() {
        super(Tag.class);
    }

    /**
     * Finds a tag with the given id
     * @param id the tagname you're looking for
     * @return Tag objects search result
     */
    @Override
    public Tag find(Object id) {
        List<Tag> foundTags = getEntityManager().createNamedQuery("Tag.findLowercase", Tag.class).setParameter("tagname", id).getResultList();
        if (foundTags.size() > 0) {
            return foundTags.get(0);
        }
        return null;
    }

    /**
     * Edit a tag, to correct spelling mistakes
     * @param entity the old tag
     * @param newTag the new tag
     * @return the new tag
     */
    public Tag edit(Tag entity, Tag newTag) {
        if (entity != null) {
            super.remove(entity);
            getEntityManager().flush();
            return super.create(newTag);
        }
        return null;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
