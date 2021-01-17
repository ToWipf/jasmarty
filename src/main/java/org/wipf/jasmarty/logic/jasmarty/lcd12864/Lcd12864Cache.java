package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;

import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864PageBase;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class Lcd12864Cache {

	private boolean bChanged = true;
	private Lcd12864PageBase page = new Lcd12864PageBase();

	/**
	 * @return
	 */
	public Lcd12864PageBase getPage() {
		return page;
	}

	/**
	 * Beim Schreiben changed true setzen
	 * 
	 * @param baScreen
	 */
	public void setScreen(Lcd12864PageBase pScreen) {
		this.bChanged = true;
		this.page = pScreen;
	}

	/**
	 * @return
	 */
	public boolean isChanged() {
		return bChanged;
	}

	/**
	 * @param bChanged
	 */
	public void setChanged(boolean bChanged) {
		this.bChanged = bChanged;
	}

}
