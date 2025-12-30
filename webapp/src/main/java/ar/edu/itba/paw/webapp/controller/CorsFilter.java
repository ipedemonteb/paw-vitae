package ar.edu.itba.paw.webapp.controller;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
        String origin = req.getHeaderString("Origin");

        // Dev: allow Vite
        if ("http://localhost:5173".equals(origin)) {
            res.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
            res.getHeaders().putSingle("Vary", "Origin");
            res.getHeaders().putSingle("Access-Control-Allow-Credentials", "true"); // if you use cookies
            res.getHeaders().putSingle("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
            res.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            res.getHeaders().putSingle("Access-Control-Expose-Headers", "Link, X-Total-Count, Location");
        }
    }
}

