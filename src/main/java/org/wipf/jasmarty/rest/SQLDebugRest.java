package org.wipf.jasmarty.rest;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wipf.jasmarty.logic.base.SqlLite;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@Path("sql")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SQLDebugRest {

	@Inject
	SqlLite sqlLite;
	@Inject
	Wipf wipf;

	@POST
	@Path("query/{q}")
	public Response query(@PathParam("q") String sQuery) {
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		try {
			rs = sqlLite.getDbJasmarty().prepareStatement(sQuery).executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();

			int cNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= cNumber; i++) {
					if (i > 1) {
						sb.append(",  ");
					}
					// Spaltenname und Inhalt
					sb.append(wipf.encodeUrlString(rsmd.getColumnName(i)) + ": '"
							+ wipf.encodeUrlString(rs.getString(i)) + "'");
				}
				sb.append("<br>");
			}
		} catch (SQLException e) {
			sb.append(wipf.encodeUrlString(e.toString()));
		}

		return Response.ok("{\"res\":\"" + sb.toString() + "\"}").build();
	}

}
