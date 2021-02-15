package org.wipf.jasmarty.logic.base;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WipfInfo {

	/**
	 * 
	 */
	public String getThreadInfo() {
		StringBuilder sb = new StringBuilder();
		Thread.getAllStackTraces().keySet().forEach((t) -> {
			sb.append("\nNr: " + t.getId());
			sb.append("\nName: " + t.getName());
			sb.append("\nIs Daemon " + t.isDaemon());
			sb.append("\nIs Alive " + t.isAlive());
			sb.append("\nState " + t.getState());
			sb.append("\nPriority " + t.getPriority());
			sb.append("\nThreadGroup" + t.getThreadGroup());
			sb.append("\nContextClassLoader" + t.getContextClassLoader());

			sb.append("\n");
		});
		return sb.toString();
	}
}
