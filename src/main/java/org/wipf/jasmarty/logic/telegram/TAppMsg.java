package org.wipf.jasmarty.logic.telegram;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
	public Telegram getMsg(Telegram t) {
		List<TeleMsg> lm = TeleMsg.findByRequest(t.getMessage()).list();
		t.setAntwort(lm.get(wipf.getRandomInt(lm.size())).response);
		return t;
	}

	/**
	 * @param id
	 * @return
	 */
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
	public void save(TeleMsg t) {
		t.saveOrUpdate();
	}

}
