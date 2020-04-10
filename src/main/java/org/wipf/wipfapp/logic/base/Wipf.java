package org.wipf.wipfapp.logic.base;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class Wipf {

	/**
	 * @param ms
	 */
	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
