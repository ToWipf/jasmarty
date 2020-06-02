package org.wipf.jasmarty.logic.base;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 * @author wipf
 *
 */
@Provider
@ApplicationScoped
public class CORSFilter implements ContainerResponseFilter {

	// private static final Logger LOGGER = Logger.getLogger("CORSFilter");

	public CORSFilter() {
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		// LOGGER.info("Modifing response with CORSFIlter: {}" +
		// responseContext.getHeaders());
		MultivaluedMap<String, Object> headers = responseContext.getHeaders();
		headers.putSingle("Access-Control-Allow-Origin", "*");
		// LOGGER.info("Modified to add the required header: {}" +
		// responseContext.getHeaders());
	}
}