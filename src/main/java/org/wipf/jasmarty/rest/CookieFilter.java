package org.wipf.jasmarty.rest;

import java.io.IOException;

import org.wipf.jasmarty.logic.base.AuthKeyService;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CookieFilter implements ContainerRequestFilter {

	@Inject
	AuthKeyService aks;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Überprüfe den Cookie
		String cookie = requestContext.getCookies().get("authKey").getValue();

		if (cookie == null || !aks.isKeyInCache(cookie)) {
			// Wenn der Cookie nicht vorhanden oder ungültig ist, Antwort abbrechen
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}

}
