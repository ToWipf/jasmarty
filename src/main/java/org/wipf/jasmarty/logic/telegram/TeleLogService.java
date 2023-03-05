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
	public void saveToLog(TeleLog t) {
		t.saveOrUpdate();
	}

	/**
	 * @param t
	 */
	@Transactional
	public void saveToLog(Telegram t) {
		// TODO wird probleme machen da nicht unic
//		if (t.msgid == 0 && t.type == null) {
//			t.msgid = -1l;
//			t.type = "system";
//		}

		TeleLog tl = new TeleLog();
		tl.msg = t.getMessage();
		tl.chatid = t.getChatID().toString();
		tl.antw = t.getAntwort();
		tl.msgdate = t.getDate();
		tl.msgfrom = t.getFrom();
		tl.type = t.getType();
		tl.saveOrUpdate();
	}

	/**
	 * @param nId
	 * @return
	 */
	@Transactional
	public void delItem(Long nId) {
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
