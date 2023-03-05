package org.wipf.jasmarty.logic.telegram;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.wipf.jasmarty.databasetypes.telegram.TeleMsg;
import org.wipf.jasmarty.datatypes.telegram.Telegram;
import org.wipf.jasmarty.logic.base.Wipf;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class TAppMsg {

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
