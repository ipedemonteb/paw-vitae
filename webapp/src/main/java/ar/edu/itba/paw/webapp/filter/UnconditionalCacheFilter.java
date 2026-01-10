package ar.edu.itba.paw.webapp.filter;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

import static ar.edu.itba.paw.webapp.utils.CacheUtils.UNCONDITIONAL_MAX_AGE;

public class UnconditionalCacheFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(HttpMethod.GET.matches(request.getMethod())) {
            response.setHeader(HttpHeaders.CACHE_CONTROL, String.format("public, max-age=%d, immutable", UNCONDITIONAL_MAX_AGE));
        }
        filterChain.doFilter(request, response);
    }
}
