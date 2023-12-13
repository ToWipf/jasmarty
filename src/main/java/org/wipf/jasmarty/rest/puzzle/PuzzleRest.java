package org.wipf.jasmarty.rest.puzzle;

import java.io.File;
import java.io.IOException;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.wipf.jasmarty.logic.puzzle.PuzzleMain;

/**
 * @author wipf
 *
 */
@Path("puzzle")
//@RolesAllowed("admin")
@PermitAll
//@Produces(MediaType.APPLICATION_SVG_XML)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PuzzleRest {

	@Inject
	PuzzleMain pm;

	@GET
	@Path("test")
	public File test() throws IOException {
		return pm.doTest();
	}

}
