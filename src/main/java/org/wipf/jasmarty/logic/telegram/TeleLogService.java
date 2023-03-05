package org.wipf.jasmarty.logic.telegram;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.telegram.TeleLog;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TeleLogService {

	@Inject
	Wipf wipf;

	/**
	 * @param t
	 */
	@Transactional
	public void saveTelegramToLog(Telegram t) {
		TeleLog tl = new TeleLog();
		tl.msgid = t.getMid();
		tl.frage = t.getMessage();
		tl.chatid = t.getChatID().toString();
		tl.antwort = t.getAntwort();
		tl.date = t.getDate();
		tl.msgfrom = t.getFrom();
		tl.persist();
	}

	/**
	 * @param nId
	 * @return
	 */
	@Transactional
	public void delItem(Integer nId) {
		TeleLog.findById(nId).delete();
	}

	/**
	 * @return
	 */
	public List<TeleLog> getAll() {
		return TeleLog.findAll().list();
	}

	/**
	 * @return
	 */
	public Integer countMsg() {
		return getAll().size();
	}

	/**
	 * 
	 */
	public void cleanLog() {

//		List<Long> lIds = new ArrayList<>();
//		lIds.add(798200105l);
//		lIds.add(-385659721l);
//		lIds.add(-387871959l);
//		lIds.add(-387712260l);
//		lIds.add(0l);
//		lIds.add(-1l);
//
//		lIds.forEach((nCid) -> {
//
//			String sUpdate = "DELETE FROM telegramlog WHERE chatid LIKE ?;";
//			try (PreparedStatement statement = sqlLite.getDbApp().prepareStatement(sUpdate)) {
//				statement.setLong(1, nCid);
//				statement.executeUpdate();
//			}
//		});
//
//		// Systemmeldungen löschen
//		delItem(-1l);
	}

}
