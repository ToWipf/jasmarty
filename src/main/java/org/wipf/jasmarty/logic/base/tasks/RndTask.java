package org.wipf.jasmarty.logic.base.tasks;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.wipf.jasmarty.logic.base.Wipf;
import org.wipf.jasmarty.logic.base.WipfConfig;
import org.wipf.jasmarty.logic.telegram.TSendAndReceive;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class RndTask {

	@Inject
	TSendAndReceive sendAndReceive;
	@Inject
	WipfConfig wipfConfig;
	@Inject
	Wipf wipf;

	/**
	 * 
	 */
	public void startRndTask() {
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.submit(new Runnable() {

			@Override
			public void run() {

				try {
					while (wipfConfig.isAppActive("rndEventTask")) {
						sendAndReceive.sendRndEventToAdmin();
						wipf.sleep((wipf.getRandomInt(22) + 2) * 1000 * 60 * 10);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

//	
//	/**
//	 * 
//	 */
//	public void startRndTask() {
//		if (!bTaskRuns) {
//			LOGGER.info("Starte Daily Task");
//			bTaskRuns = true;
//			Timer t = new Timer();
//			TimerTask taskdaily = rndRun;
//			LocalDateTime localDateTime = LocalDateTime.now();
//
//			long nSekundenBisMitternacht = (86400
//					- (localDateTime.getHour() * 60 * 60 + localDateTime.getMinute() * 60 + localDateTime.getSecond()));
//
//			// Starte jeden Tag um 00:00 Uhr
//			t.scheduleAtFixedRate(taskdaily, nSekundenBisMitternacht * 1000, 86400000);
//			// TODO t.cancel(); -> stop task
//		} else {
//			LOGGER.error("Daily Task ist gerade aktiv!");
//		}
//	extends TimerTask 
//	}
//

}
