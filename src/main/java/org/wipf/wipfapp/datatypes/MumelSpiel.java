package org.wipf.wipfapp.datatypes;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
public class MumelSpiel extends Game {

	private static final Logger LOGGER = Logger.getLogger("MumelSpiel");

	private List<MumelSpieler> lmsp = new ArrayList<>();
	private Integer nWerIstDran;

	public List<MumelSpieler> getMumelSpielerliste() {
		return lmsp;
	}

	public void setMumelSpielerliste(List<MumelSpieler> lmsp) {
		this.lmsp = lmsp;
	}

	public void setMumelSpielerliste(String sImportString) {
		try {

			// for (String part : sImportString.split(" ")) {

			// TODO find by id und add to list
//				MumelSpieler msp = new MumelSpieler();
//				this.lmsp.add(msp);

			// }
		} catch (Exception e) {
			LOGGER.warn("sgetMessageWord " + e);
		}

	}

	public Integer getWerIstDran() {
		return nWerIstDran;
	}

	public void setWerIstDran(Integer nWerIstDran) {
		this.nWerIstDran = nWerIstDran;
	}

}
