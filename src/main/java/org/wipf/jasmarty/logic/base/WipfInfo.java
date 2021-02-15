package org.wipf.jasmarty.logic.base;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WipfInfo {

	/**
	 * 
	 */
	public String getThreadInfo() {
		StringBuilder sb = new StringBuilder();
		Thread.getAllStackTraces().keySet().forEach(
				(t) -> sb.append(t.getName() + "\nIs Daemon " + t.isDaemon() + "\nIs Alive " + t.isAlive() + "\n\n"));
		return sb.toString();
	}
}
