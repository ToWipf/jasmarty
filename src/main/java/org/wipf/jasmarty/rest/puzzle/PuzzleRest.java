package org.wipf.jasmarty.rest.puzzle;

import java.io.File;
import java.io.IOException;

import org.wipf.jasmarty.logic.puzzle.PuzzleMain;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

/**
 * @author wipf
 *
 */
@Path("puzzle")
//@RolesAllowed("admin")
@PermitAll
//@Produces(MediaType.APPLICATION_SVG_XML)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class PuzzleRest {

	@Inject
	PuzzleMain pm;

	@GET
	@Path("test")
	public File test() throws IOException {
		return pm.doTest();
	}

}
