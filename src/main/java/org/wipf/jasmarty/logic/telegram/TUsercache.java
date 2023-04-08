package org.wipf.jasmarty.logic.telegram;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.telegram.Usercache;
import org.wipf.jasmarty.datatypes.telegram.Telegram;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class TUsercache {

	@Inject
	TSendAndReceive sendAndReceive;

	/**
	 * Usercache überschreiben
	 * 
	 * @param o
	 * 
	 */
	@Transactional
	public void save(Usercache o) {
		o.saveOrUpdate();
	}

	/**
	 * Speichern ohne den Usercache zu ändern
	 * 
	 * @param o
	 * 
	 */
	@Transactional
	public void saveOhneUsercache(Usercache o) {
		// Usercache vom letzten mal uebertragen
		Usercache last = getLastMessage(o.chatid);
		if (last != null) {
			// Daten nur übertagen
			o.usercache = (last.usercache);
			o.counter = (last.counter);
		} else {
			// Neuer User
			o.counter = (1);
			// Bei einen neuen Nutzer -> Info an Admin
			sendAndReceive.sendMsgToAdmin("Neuer Telegram Nutzer\n" + o.toString());
		}

		save(o);
	}

	/**
	 * Speichern ohne den Usercache anzufassen
	 * 
	 * @param t
	 */
	@Transactional
	public void saveByTelegramOhneUsercache(Telegram t) {
		Usercache lmsg = new Usercache();
		lmsg.chatid = (t.getChatID());
		lmsg.msg = (t.getMessage());
		saveOhneUsercache(lmsg);
	}

	/**
	 * @param nChatid
	 * @return
	 */
	public Usercache getLastMessage(Long nChatId) {
		try {
			Usercache u = get(nChatId);
			// Bei jeden Laden den Counter hochzählen um beim speichern aktuell zu sein
			if (u.counter != null) {
				u.counter = (u.counter + 1);
			} else {
				// neuer User
				u.counter = (1);
			}

			return u;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * @param sDate
	 * @param nUserId
	 * @return
	 */
	@Transactional
	public Usercache get(Long nChatid) {
		return Usercache.findById(nChatid);
	}

	/**
	 * @param sDate
	 */
	@Transactional
	public void del(Long nId) {
		Usercache.findById(nId).delete();
	}

	/**
	 * @param nUserId
	 * @return
	 */
	public List<Usercache> getAll() {
		return Usercache.findAll().list();
	}

	/**
	 * @return
	 */
	public Integer getAnzahl() {
		return (int) Usercache.findAll().count();

	}

	/**
	 * @return
	 */
	public String getAllAsText() {
		StringBuilder sb = new StringBuilder();
		Integer n = 0;

		for (Usercache uc : getAll()) {
			if (sb.length() > 1) {
				sb.append("\n\n");
			}
			sb.append(uc.toString());

			// Chatid zu Name
			// TODOsb.append("\n" + telelog.infoZuId(uc.chatid.toString()));
			n++;
		}

		return "Anzahl der Benutzer: " + n + "\n\n" + sb.toString();
	}

}
