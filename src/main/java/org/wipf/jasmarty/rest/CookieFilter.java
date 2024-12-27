package org.wipf.jasmarty.rest;

import java.io.IOException;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.MainHome;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CookieFilter implements ContainerRequestFilter {

	@Inject
	AuthKeyService aks;

	private static final Logger LOGGER = Logger.getLogger("CookieFilter");

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Überprüfe Cookie
		String path = requestContext.getUriInfo().getPath();
		Cookie cookies = requestContext.getCookies().get(MainHome.AUTH_KEY_NAME);
		String authCookie = "";
		if (cookies != null) {
			authCookie = cookies.getValue();
		}

		if (path.equals("/wipf/up") || path.equals("/wipf/ver")) {
			return;
		}

		if (authCookie.isEmpty() || !aks.isKeyInCache(authCookie)) {
			// Wenn Cookie nicht vorhanden oder ungültig ist, Antwort abbrechen
			LOGGER.warn("Kein Zugriff: " + authCookie);
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}

}
