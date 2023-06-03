package org.wipf.jasmarty.rest.base;

import java.io.File;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.FileVW;

/**
 * @author wipf
 *
 */
@Path("file")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@ApplicationScoped
public class FileRest {

	@Inject
	FileVW fileVw;

	@GET
	@Path("getAll")
	public Response getAll() {
		return Response.ok(fileVw.getAllFiles()).build();
	}

	@GET
	@Produces(MediaType.MULTIPART_FORM_DATA)
	@Path("download/{name}")
	public Response download(@PathParam("name") String sName) {
		return Response.ok(fileVw.getFile(sName)).build();
	}

	@POST
	@Path("upload/{filename}")
	@Consumes(MediaType.WILDCARD)
	public Response upload(@PathParam("filename") String sName, File f) {
		fileVw.saveFile(sName, f);
		return Response.ok().build();
	}

	@DELETE
	@Path("del/{name}")
	public Response del(@PathParam("name") String sName) {
		return Response.ok("{\"del\":\"" + fileVw.delFile(sName) + "\"}").build();
	}

}
