package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.wipf.jasmarty.databasetypes.jasmarty.ButtonAction;
import org.wipf.jasmarty.logic.jasmarty.lcd12864.Lcd12864PageVerwaltung;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class ActionVerwaltung {

	@Inject
	LcdConnect lcdConnect;
	@Inject
	Lcd12864PageVerwaltung pageVerwaltung;

	private static final Logger LOGGER = Logger.getLogger("ActionVerwaltung");
	private Integer currentPressed;

	/**
	 * @param ba
	 * @throws SQLException
	 */
	@Transactional
	public void save(ButtonAction ba) {
		ba.saveOrUpdate();
	}

	/**
	 * @param nId
	 * @throws SQLException
	 */
	@Transactional
	public void delete(Integer nId) {
		ButtonAction.findById(nId).delete();
	}

	/**
	 * @param nId
	 * @return
	 */
	public ButtonAction getActionFromDbById(int nId) {
		return ButtonAction.findById(nId);
	}

	/**
	 * @return
	 */
	public List<ButtonAction> getAll() {
		return ButtonAction.findAll().list();
	}

	/**
	 * @param nId
	 * @return
	 */
	public ButtonAction getActionByButton(int nButton) {
		return ButtonAction.findByButton(nButton).firstResult();
	}

	/**
	 * @return
	 */
	public Integer getCurrentPressed() {
		return currentPressed;
	}

	/**
	 * @param nButton
	 * @throws Exception
	 */
	public void doActionByButtonNr(Integer nButton) {
		this.currentPressed = nButton;
		if (nButton != null) {
			ButtonAction ba = getActionByButton(nButton);
			doActionByButton(ba);
		}
	}

	/**
	 * @param nButtonId
	 * @throws Exception
	 */
	public void doActionById(Integer nButtonId) {
		if (nButtonId != null) {
			ButtonAction ba = getActionFromDbById(nButtonId);
			doActionByButton(ba);
		}
	}

	/**
	 * @param ba
	 * @throws Exception
	 */
	private void doActionByButton(ButtonAction ba) {
		if (ba.action != null) {

			Integer nTrennlineFirst = ba.action.indexOf('|');
			Integer nTrennlineLast = ba.action.lastIndexOf('|');

			String sParameter1 = ba.action.substring(0, nTrennlineFirst);
			String sParameter2 = null;
			String sParameter3 = null;

			if (nTrennlineFirst != nTrennlineLast) {
				// es gibt einen 3. Parameter
				sParameter2 = ba.action.substring(nTrennlineFirst + 1, nTrennlineLast);
				sParameter3 = ba.action.substring(nTrennlineLast + 1);
			} else {
				// Es gibt nur 2 Parameter
				sParameter2 = ba.action.substring(nTrennlineFirst + 1);
			}

			switch (sParameter1) {
			case "led":
				switch (sParameter2) {
				case "on":
					lcdConnect.ledOn();
					return;
				case "off":
					lcdConnect.ledOff();
					return;
				case "toggle":
					lcdConnect.ledToggle();
					return;
				}
				return;
			case "page":
				switch (sParameter2) {
				case "next":
					pageVerwaltung.nextPage();
					return;
				case "last":
					pageVerwaltung.lastPage();
					return;
				case "select":
					pageVerwaltung.select(Integer.valueOf(sParameter3));
					return;
				}
				return;
			case "volume":
				switch (sParameter2) {
				case "up":
					lcdConnect.commandVolUp();
					return;
				case "down":
					lcdConnect.commandVolDown();
					return;
				case "mute":
					lcdConnect.commandVolMute();
					return;
				}
				return;
			case "exec":
			case "system":

			default:
				LOGGER.warn("Aktion nicht verfügbar: " + sParameter1);
				return;
			}
		} else {
			LOGGER.warn("Eingang nicht definert: " + currentPressed);
		}
	}

}
