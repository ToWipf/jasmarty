package org.wipf.jasmarty.logic.eisenbahn;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.datatypes.eisenbahn.ArduinoConfig;
import org.wipf.jasmarty.logic.base.WipfConfig;

/**
 * @author Wipf
 *
 */
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
	private static final String LINELENGTH = "arduino_LineLength";

	private static final Logger LOGGER = Logger.getLogger("Eisenbahn Mitlesen Home");

	/**
	 * @throws SQLException
	 * 
	 */
	public void start() {
		try {
			ArduinoConfig ac = new ArduinoConfig();
			ac.setPort(wipfConfig.getConfParamString(PORT));
			ac.setBaudRate(wipfConfig.getConfParamInteger(BAUDRATE));
			ac.setLineLength(wipfConfig.getConfParamInteger(LINELENGTH));
			if (ac.isValid()) {
				LOGGER.info("Config ok");
				mitlesenConnect.setConfig(ac);
				// mitlesenConnect.startSerialPort();
				// mitlesen.doStartMitlesen();
			} else {
				LOGGER.error("Problem mit der Config");
				wipfConfig.setConfParam(PORT, "COM3");
				wipfConfig.setConfParam(BAUDRATE, 57600);
				wipfConfig.setConfParam(LINELENGTH, 50);
			}

			ac.setPort(wipfConfig.getConfParamString(PORT));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
