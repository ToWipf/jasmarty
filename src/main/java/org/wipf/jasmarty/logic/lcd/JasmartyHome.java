package org.wipf.jasmarty.logic.lcd;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

/**
 * @author wipf
 *
 */
@RequestScoped
public class JasmartyHome {

	@Inject
	LcdConnect lcdConnect;
	@Inject
	LcdRefreshLoop lcdRefreshLoop;
	@Inject
	SerialConfig serialConfig;
	@Inject
	Lcd12864 lcd12864;

	private static final Logger LOGGER = Logger.getLogger("JasmartyHome");

	/**
	 * 
	 * 
	 */
	public void jasmartyStart() {
		LOGGER.info("Starten");
		lcdConnect.setConfig(serialConfig.getConfig());

		lcd12864.startLCD();
		lcdRefreshLoop.startRefresh12864();

		LOGGER.info("jasmarty init ende");
	}

	/**
	 * 
	 */
	public void jasmartyStop() {
		LOGGER.info("Stoppe");

		if (lcdConnect.isLcdOk()) {
			lcdRefreshLoop.stop();
			lcdConnect.closeSerialLcdPort();
		}
		LOGGER.info("Gestoppt");
	}

}
