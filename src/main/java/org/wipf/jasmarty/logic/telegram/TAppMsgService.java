package org.wipf.jasmarty.logic.telegram;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.telegram.TeleMsg;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppMsgService {

	@Inject
	Wipf wipf;

	/**
	 * @param t
	 * @return
	 * 
	 */
	public List<TeleMsg> getAll() {
		return TeleMsg.findAll().list();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public Telegram getMsg(Telegram t) {
		List<TeleMsg> lm = TeleMsg.findByFrage(t.getMessage()).list();
		if (lm.size() > 0)
			t.setAntwort(lm.get(wipf.getRandomInt(lm.size())).antwort);
		return t;
	}

	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public void del(Integer nId) {
		TeleMsg.findById(nId).delete();
	}

	/**
	 * @return
	 * 
	 */
	public Integer countMsg() {
		return getAll().size();
	}

	/**
	 * @param t
	 * @return
	 */
	@Transactional
	public void save(TeleMsg t) {
		t.saveOrUpdate();
	}

}
