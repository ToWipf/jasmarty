package org.wipf.jasmarty.logic.lcd;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.jasmarty.LcdConfig;
import org.wipf.jasmarty.logic.base.WipfConfigVW;

import com.fazecast.jSerialComm.SerialPort;

/**
 * @author wipf
 *
 */
@ApplicationScoped
public class SerialConfig {

	private static final Logger LOGGER = Logger.getLogger("lcd_SerialConfig");

	@Inject
	WipfConfigVW wipfConfig;

	/**
	 * @return
	 * 
	 */
	public LcdConfig getConfig() {

		LcdConfig conf = new LcdConfig();

		conf.setPort(wipfConfig.getConfParamString("lcd_port"));
		Integer nRR = wipfConfig.getConfParamInteger("lcd_refreshrate");
		Integer nBR = wipfConfig.getConfParamInteger("lcd_baudrate");

		// Default Werte
		if (nRR == null) {
			nRR = 200;
		}
		if (nBR == null) {
			nBR = 57600;
		}

		conf.setRefreshRate(nRR);
		conf.setBaudRate(nBR);

		return conf;
	}

	/**
	 * @param conf
	 * 
	 */
	public void setConfig(LcdConfig conf) {
		wipfConfig.setConfParam("lcd_port", conf.getPort());
		wipfConfig.setConfParam("lcd_refreshrate", conf.getRefreshRate());
		wipfConfig.setConfParam("lcd_baudrate", conf.getBaudRate());

		LOGGER.info("Config speichern");
	}

	/**
	 * @param jnRoot
	 * 
	 */
	public void setConfig(String jnRoot) {
		setConfig(new LcdConfig().setByJson(jnRoot));
	}

	/**
	 * @return
	 */
	public JSONObject getPorts() {
		SerialPort[] spa = SerialPort.getCommPorts();

		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for (SerialPort item : spa) {
			JSONObject jItem = new JSONObject();

			jItem.put("name", item.getDescriptivePortName());
			ja.put(jItem);
		}
		jo.put("list", ja);
		return jo;
	}

}
