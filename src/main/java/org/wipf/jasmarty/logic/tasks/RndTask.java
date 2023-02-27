package org.wipf.jasmarty.logic.tasks;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfigVW;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class RndTask {

	private boolean bRun = false;

	@Inject
	TSendAndReceive sendAndReceive;
	@Inject
	WipfConfigVW wipfConfig;
	@Inject
	Wipf wipf;

	private static final Logger LOGGER = Logger.getLogger("RndTask");

	/**
	 * @throws Exception
	 * 
	 */
	public boolean startRndTask() throws SQLException {
		if (wipfConfig.isAppActive("rndEventTask") && !this.bRun) {

			LOGGER.info("start");

			this.bRun = true;
			ExecutorService service = Executors.newFixedThreadPool(1);
			service.submit(new Runnable() {

				@Override
				public void run() {

					while (bRun) {
						Integer nHour = Integer.valueOf(wipf.getTime("HH"));
						if (nHour >= 7 && nHour <= 20) {
							// Nur Tagsüber seinden
							sendAndReceive.sendRndEventToAdmin();
						}
						// Alt
						// wipf.sleep((wipf.getRandomInt(15) + 2) * 950 * 60 * 10 * 3);
						wipf.sleep((wipf.getRandomInt(8) + 4) * 950 * 60 * 10 * 3);
					}
				}
			});
		}
		return this.bRun;
	}

	/**
	 * @return
	 * 
	 */
	public boolean stopRndTask() {
		LOGGER.info("stop");
		this.bRun = false;
		return this.bRun;
	}

}
