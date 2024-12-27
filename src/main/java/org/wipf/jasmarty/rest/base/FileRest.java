package org.wipf.jasmarty.rest.base;

import java.io.File;

import org.wipf.jasmarty.logic.base.AuthKeyService;
import org.wipf.jasmarty.logic.base.FileVW;
import org.wipf.jasmarty.logic.base.MainHome;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
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
	@Inject
	AuthKeyService aks;

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
	public Response download(@PathParam("name") String sName, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok(fileVw.getFile(sName)).build();
		}
		return null;
	}

	@GET
	// @PermitAll
	@Produces(MediaType.MULTIPART_FORM_DATA)
	@Path("downloadScale/{size}/{name}")
	public File downloadScale(@PathParam("size") Integer size, @PathParam("name") String sName, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return fileVw.getImageBySize(size, sName);
		}
		return null;
	}

	@POST
	@Path("upload/{filename}")
	@Consumes(MediaType.WILDCARD)
	public Response upload(@PathParam("filename") String sName, File f, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			fileVw.saveFile(sName, f);
			return Response.ok().build();
		}
		return null;
	}

	@DELETE
	@Path("del/{name}")
	public Response del(@PathParam("name") String sName, @CookieParam(MainHome.AUTH_KEY_NAME) String key) {
		if (aks.isKeyInCache(key)) {
			return Response.ok("{\"del\":\"" + fileVw.delFile(sName) + "\"}").build();
		}
		return null;
	}

}
