package org.wipf.jasmarty.logic.eisenbahn;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.eisenbahn.ArduinoConfig;
import org.wipf.jasmarty.logic.base.WipfConfig;

@ApplicationScoped
public class MitlesenHome {

	@Inject
	MitlesenConnect mitlesenConnect;
	@Inject
	WipfConfig wipfConfig;
	@Inject
	Mitlesen mitlesen;

	private static final String PORT = "arduino_Port";
	private static final String BAUDRATE = "arduino_BaudRate";

	private static final Logger LOGGER = Logger.getLogger("Eisenbahn Mitlesen Home");

	/**
	 * @throws SQLException
	 * 
	 */
	public void start() throws SQLException {
		ArduinoConfig ac = new ArduinoConfig();
		ac.setPort(wipfConfig.getConfParamString(PORT));
		ac.setBaudRate(wipfConfig.getConfParamInteger(BAUDRATE));
		if (ac.isValid()) {
			LOGGER.info("Config ok");
			mitlesenConnect.setConfig(ac);
			mitlesenConnect.startSerialLcdPort();
			mitlesen.doStartMitlesen();
		} else {
			LOGGER.error("Problem mit der Config");
			wipfConfig.setConfParam(PORT, "");
			wipfConfig.setConfParam(BAUDRATE, 0);
		}

	}

}
