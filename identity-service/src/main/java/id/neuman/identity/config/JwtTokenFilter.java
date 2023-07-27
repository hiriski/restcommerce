package id.neuman.identity.config;

import id.neuman.identity.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final HandlerExceptionResolver handlerExceptionResolver;

    // Spring Security will call this method during filter chain execution
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException, JwtException {

        // trying to find Authorization header
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // if Authorization header does not exist, then skip this filter
            // and continue to execute next filter class
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        final String token = authorizationHeader.split(" ")[1].trim();

        try {
            // initializing UsernamePasswordAuthenticationToken with its 3 parameter constructor
            // because it sets super.setAuthenticated(true); in that constructor.
            UsernamePasswordAuthenticationToken principal = getPrincipal(token);
            principal.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            // finally, give the authentication token to Spring Security Context
            SecurityContextHolder.getContext().setAuthentication(principal);
            // end of the method, so go for next filter class
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (JwtException e) {
            handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, e);
        }
    }

    public UsernamePasswordAuthenticationToken getPrincipal(String token) {
        String username = jwtUtil.getUsername(token);
        return new UsernamePasswordAuthenticationToken(
                username, null, Collections.emptyList());
    }
}
