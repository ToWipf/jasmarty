package org.wipf.jasmarty.logic.jasmarty;



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
		conf.setRefreshRate(wipfConfig.getConfParamInteger("lcd_refreshrate"));
		conf.setBaudRate(wipfConfig.getConfParamInteger("lcd_baudrate"));

		return conf;

		// return defaultConfig();
	}

	/**
	 * @return
	 * 
	 */
	private LcdConfig defaultConfig() {
		LcdConfig lcDef = new LcdConfig();
		lcDef.setPort("");
		lcDef.setBaudRate(57600);
		lcDef.setRefreshRate(200);

		setConfig(lcDef);
		return lcDef;
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
