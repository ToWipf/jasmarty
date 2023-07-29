package org.wipf.jasmarty.rest.mttt;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.datatypes.mttt.mtttData;
import org.wipf.jasmarty.logic.mttt.MtttCache;
import org.wipf.jasmarty.logic.mttt.MtttService;

/**
 * @author Wipf
 *
 */
@Path("mttt")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//@RolesAllowed("admin")
@PermitAll
@ApplicationScoped
public class MtttRest {

	@Inject
	MtttService mttt;
	@Inject
	MtttCache cache;

	@GET
	@Path("do")
	@Produces(MediaType.TEXT_PLAIN)
	public Response doMttt() {
		return Response.ok(mttt.getFullScreen()).build();
	}

	@GET
	@Path("testset")
	@Produces(MediaType.TEXT_PLAIN)
	public Response testset() {
		mttt.doSetTest();
		return Response.ok().build();
	}

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public Response doMtttTest() {
		return Response.ok(mttt.getTestdata()).build();
	}

	@GET
	@Path("size")
	@Produces(MediaType.TEXT_PLAIN)
	public Integer size() {
		return MtttService.SIZE;
	}

	@GET
	@Path("getCache")
	@Produces(MediaType.APPLICATION_JSON)
	public mtttData[][] getCache() {
		return cache.getCache();
	}

}
