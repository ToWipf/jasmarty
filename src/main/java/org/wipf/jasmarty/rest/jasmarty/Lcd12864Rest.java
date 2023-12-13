package org.wipf.jasmarty.rest.jasmarty;

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

import org.wipf.jasmarty.databasetypes.lcd.LcdPageDescription;
import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864PageBase;
import org.wipf.jasmarty.logic.lcd.Lcd12864Cache;
import org.wipf.jasmarty.logic.lcd.Lcd12864PageVerwaltung;

/**
 * @author wipf
 *
 */
@Path("lcd12864")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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
	@Path("get/{id}")
	public LcdPageDescription getPage(@PathParam("id") int nId) {
		return lcd12864PageVerwaltung.load(nId);
	}

	/**
	 * Gibt die ID der Seite zurück
	 * 
	 * @param jnRoot
	 * @return
	 */
	@POST
	@Path("save")
	public Integer savePage(String jnRoot) {
		return lcd12864PageVerwaltung.save(jnRoot);
	}

	@DELETE
	@Path("deletePage/{id}")
	public Response delete(@PathParam("id") int nId) {
		lcd12864PageVerwaltung.del(nId);
		return Response.ok().build();
	}

	@GET
	@POST
	@Path("selectPage/{id}")
	public Response selectPage(@PathParam("id") int nId) {
		lcd12864PageVerwaltung.select(nId);
		return Response.ok().build();
	}

	@GET
	@POST
	@Path("refreshNow")
	public Response refreshNow() {
		lcd12864Cache.setChanged(true);
		return Response.ok().build();
	}

}
