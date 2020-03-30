package org.wipf.wipfapp.logic.elcd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.RequestScoped;

import org.jboss.logging.Logger;
import org.wipf.wipfapp.logic.base.MWipf;
import org.wipf.wipfapp.logic.base.Wipfapp;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author wipf
 *
 */
@RequestScoped
public class MelcdRun {

	private static final Logger LOGGER = Logger.getLogger("MelcdRun");

	/**
	 * Start
	 */
	public String startElcd() {
		if (Wipfapp.RunLock) {
			LOGGER.info("Runlock is on");
			return "F";
		} else {
			LOGGER.info("Set Runlock on");
			Wipfapp.RunLock = true;
		}
		ExecutorService service = Executors.newFixedThreadPool(4);
		service.submit(new Runnable() {
			@Override
			public void run() {
				Integer sendCounter = 0;
				LOGGER.info("Start send to Lcd");
				MWipf.sleep(1000);
				Wipfapp.FailCountElcd = 0;
				clear();
				displayLoopRare();
				if (Wipfapp.FailCountElcd > 0) {
					MWipf.sleep(500);
				}

				while (Wipfapp.FailCountElcd < 3) {
					displayLoop();
					if (sendCounter > 100) {
						displayLoopRare();
						sendCounter = 0;
					}
					MWipf.sleep(500);
					sendCounter++;
				}
				LOGGER.info("Set Runlock off");
				Wipfapp.RunLock = false;
			}
		});
		return "K";
	}

	/**
	 * 
	 */
	private void displayLoopRare() {
		String sDayname = MWipf.dayName();
		String sDate = MWipf.date();

		write(1, ((20 - sDayname.length()) / 2), sDayname);
		write(2, ((20 - sDate.length()) / 2), sDate);
	}

	/**
	 * 
	 */
	private void displayLoop() {
		write(0, 6, MWipf.uhr());
	}

	/**
	 * @param sMsg
	 * @return
	 */
	public Boolean sendMsg(String sMsg) {
		try {
			write(3, 0, sMsg);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @return
	 * 
	 */
	public boolean clear() {
		try {
			restLcd("cls");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param nCol
	 * @param nRow
	 * @param Text
	 */
	public void write(Integer nRow, Integer nCol, String sText) {
		if (nCol > 20 || nCol < 0 || nRow < 0 || nRow > 4 || sText.length() > 20 || sText.indexOf(' ') == 0) {
			return;
		}

		String sCol;
		if (nCol < 10) {
			sCol = '0' + nCol.toString();
		} else {
			sCol = nCol.toString();
		}
		restLcd(nRow + sCol + sText);
	}

	/**
	 * @param sCall
	 */
	private void restLcd(String sCall) {
		HttpResponse<String> response;
		try {
			response = Unirest.put(Wipfapp.ELCD_PATH + sCall).asString();
			if (response.getBody().indexOf("0") == -1) {
				LOGGER.warn(response.getBody());
			}
			// return (response.getBody().equals("{}"));
			// TODO: setze taster
			Wipfapp.FailCountElcd = 0;

		} catch (UnirestException e) {
			LOGGER.warn("Sendefehler");
			Wipfapp.FailCountElcd++;
		}
	}

}
