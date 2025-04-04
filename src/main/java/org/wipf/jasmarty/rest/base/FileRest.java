package org.wipf.jasmarty.rest.base;

import java.io.File;

import org.wipf.jasmarty.logic.base.FileVW;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
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

/**
 * @author wipf
 *
 */
@Path("file")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
@RequestScoped
public class FileRest {

	@Inject
	FileVW fileVw;

	@GET
	@PermitAll
	@Path("getAll")
	public Response getAll() {
		return Response.ok(fileVw.listAllFiles()).build();
	}

	@GET
	// @PermitAll
	@Produces(MediaType.MULTIPART_FORM_DATA)
	@Path("download/{name}")
	public Response download(@PathParam("name") String sName) {
		return Response.ok(fileVw.getFile(sName)).build();
	}

	@GET
	// @PermitAll
	@Produces(MediaType.MULTIPART_FORM_DATA)
	@Path("downloadScale/{size}/{name}")
	public File downloadScale(@PathParam("size") Integer size, @PathParam("name") String sName) {
		return fileVw.getImageBySize(size, sName);
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
