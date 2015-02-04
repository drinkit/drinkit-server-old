package guru.drinkit.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Copy-pasted from https://spring.io/guides/gs/rest-service-cors/
 */
public class SimpleCORSFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Expose-Headers", "WWW-Authenticate");
        response.setHeader("Access-Control-Max-Age", "3600");
//        if (request.getHeader("Access-Control-Request-Method") != null
//                && "OPTIONS".equals(request.getMethod())) {
//            // CORS "pre-flight" request
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type");
//        }
    }
}
