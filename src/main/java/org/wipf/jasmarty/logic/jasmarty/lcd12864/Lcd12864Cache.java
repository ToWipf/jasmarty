package org.wipf.jasmarty.logic.jasmarty.lcd12864;

import javax.enterprise.context.ApplicationScoped;

import org.wipf.jasmarty.datatypes.jasmarty.Lcd12864Page;

/**
 * @author Wipf
 *
 */
@ApplicationScoped
public class Lcd12864Cache {

	private boolean bChanged = true;
	private Lcd12864Page page = new Lcd12864Page();

	/**
	 * @return
	 */
	public Lcd12864Page getPage() {
		return page;
	}

	/**
	 * Beim Schreiben changed true setzen
	 * 
	 * @param baScreen
	 */
	public void setScreen(Lcd12864Page pScreen) {
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
