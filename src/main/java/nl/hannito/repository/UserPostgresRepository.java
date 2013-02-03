package nl.hannito.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.hannito.DAO.LoginDAO;
import nl.hannito.entity.User;

@Stateless
public class UserPostgresRepository extends RESTFacade<User> {
    @PersistenceContext private EntityManager em;
    
    public UserPostgresRepository() {
        super(User.class);
    }

    /**
     * Finds a user with the given identifier
     * @param identifier email of username string
     * @return the found user
     */
    public User find(String identifier) {
        List<User> resultList = getEntityManager().createNamedQuery("User.findByIdentifier", User.class).setParameter("identifier", identifier).getResultList();
        if(resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }
    
    /**
     * Finds a user with given username
     * @param username userame to search for
     * @return search result user
     */
    public User findByUsername(String username) {
        List<User> resultList = getEntityManager().createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getResultList();
        if(resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }
    
    /**
     * Finds a user with the given email
     * @param email email to search for
     * @return search result user
     */
    public User findByEmail(String email) {
        List<User> resultList = getEntityManager().createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getResultList();
        if(resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }
    
    /**
     * Login as the given user
     * @param user LoginDAO with the identifier and the password
     * @return the user if all went well
     */
    public User login(LoginDAO user) {
        List<User> resultList = getEntityManager().createNamedQuery("User.findByIdentifierAndPassword", User.class)
                                        .setParameter("identifier", user.getIdentifier())
                                        .setParameter("password", user.getPassword()).getResultList();
        if(resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }
    
    /**
     * Creates a user in the database
     * @param entity User
     * @return created user
     */
    @Override
    public User create(User entity) {
        return super.create(entity);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
