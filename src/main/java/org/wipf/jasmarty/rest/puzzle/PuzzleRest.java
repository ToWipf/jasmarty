package org.wipf.jasmarty.rest.puzzle;

import java.io.File;
import java.io.IOException;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

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
