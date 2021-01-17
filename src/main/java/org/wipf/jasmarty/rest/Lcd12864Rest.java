package org.wipf.jasmarty.rest;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864PageBase;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864Cache;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864PageVerwaltung;

/**
 * @author wipf
 *
 */
@Path("lcd12864")
//@RolesAllowed("admin") TODO!
@Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class Lcd12864Rest {

	@Inject
	Lcd12864Cache lcd12864Cache;
	@Inject
	Lcd12864PageVerwaltung lcd12864PageVerwaltung;

	@POST
	@Path("setScreen")
	public Response setScreen(String jnRoot) {
		lcd12864Cache.setScreen(new Lcd12864PageBase(jnRoot));
		return Response.ok().build();
	}

	@GET
	@Path("getScreen")
	public Response setScreen() {
		return Response.ok(lcd12864Cache.getPage().getScreenAsJsonArray().toString()).build();
	}

// TODO
//	@GET
//	@Path("getCurrentPage")
//	public Response getCurrent() {
//		return Response.ok(lcd12864PageVerwaltung.CurrentSite().toJson()).build();
//	}

	@GET
	@Path("getPage/{id}")
	public Response getPage(@PathParam("id") int nId) {
		return Response.ok(lcd12864PageVerwaltung.load(nId).toJson().toString()).build();
	}

	@POST
	@Path("savePage")
	public Response save(String jnRoot) {
		try {
			lcd12864PageVerwaltung.save(jnRoot);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("deletePage/{id}")
	public Response delete(@PathParam("id") int nId) {
		try {
			lcd12864PageVerwaltung.del(nId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@GET
	@POST
	@Path("selectPage/{id}")
	public Response selectPage(@PathParam("id") int nId) {
		lcd12864PageVerwaltung.select(nId);
		return Response.ok().build();
	}

}
