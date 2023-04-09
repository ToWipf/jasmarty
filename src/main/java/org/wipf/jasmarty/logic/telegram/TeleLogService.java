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
		tl.chatid = t.getChatID();
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
	@Transactional
	public void cleanLog() {
		getAll().forEach(tl -> {
			switch (tl.chatid.toString()) {
			case "798200105":
			case "-385659721":
			case "-387871959":
			case "-387712260":
			case "0":
			case "-1":
				tl.delete();
				break;
			default:
				break;
			}
		});

	}

}
