package nl.hannito.filters;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public class RestAuthenticationFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest req;
    
    /**
     * Checks if the incoming request is authorized to access the webservice
     * @param cr Request
     * @return ContainerRequest
     */
    @Override
    public ContainerRequest filter(ContainerRequest cr) {
        HttpSession session = req.getSession(false);
        String[] unauthorizedUrls = {"/login", "/logout", "/register", "/ping", "/images"};
        
        if(getUsername(session) == null &&
            Arrays.asList(unauthorizedUrls).contains(getAPIUrl(req.getRequestURI())) == false) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return cr;
    }
    
    /**
     * Cuts front of api-url
     * @param url The url to cut
     * @return Cut url
     */
    private String getAPIUrl(String url) {
        url = url.split("/api")[1];
        return url;
    }
    
    /**
     * Returns username for this session
     * @param session HttpSession
     * @return Username
     */
    private String getUsername(HttpSession session) {
        if(session != null) {
            if(session.getAttribute("username") != null) {
                return session.getAttribute("username").toString();
            }
        }
        return null;
    }
}
