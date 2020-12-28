package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.jasmarty.LcdConfig;
import org.wipf.jasmarty.datatypes.jasmarty.LcdConfig.lcdType;
import org.wipf.jasmarty.logic.base.WipfConfig;

import com.fazecast.jSerialComm.SerialPort;

@ApplicationScoped
public class SerialConfig {

	private static final Logger LOGGER = Logger.getLogger("lcd_SerialConfig");

	@Inject
	WipfConfig wipfConfig;

	/**
	 * @return
	 * @throws SQLException
	 */
	public LcdConfig getConfig() throws SQLException {
		try {
			LcdConfig conf = new LcdConfig();

			conf.setPort(wipfConfig.getConfParamString("lcd_port"));
			conf.setRefreshRate(wipfConfig.getConfParamInteger("lcd_refreshrate"));
			conf.setWidth(wipfConfig.getConfParamInteger("lcd_widht"));
			conf.setHeight(wipfConfig.getConfParamInteger("lcd_height"));
			conf.setBaudRate(wipfConfig.getConfParamInteger("lcd_baudrate"));
			conf.setType(wipfConfig.getConfParamString("lcd_type"));

			return conf;
		} catch (Exception e) {
			LOGGER.warn("Config nicht gefunden > default Config erstellen");
			return defaultConfig();
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private LcdConfig defaultConfig() throws SQLException {
		LcdConfig lcDef = new LcdConfig();
		lcDef.setPort("");
		lcDef.setHeight(4);
		lcDef.setWidth(20);
		lcDef.setBaudRate(9600);
		lcDef.setRefreshRate(200);
		lcDef.setType(lcdType.LCD_2004);

		setConfig(lcDef);
		return lcDef;
	}

	/**
	 * @param conf
	 * @throws SQLException
	 */
	public void setConfig(LcdConfig conf) throws SQLException {
		wipfConfig.setConfParam("lcd_port", conf.getPort());
		wipfConfig.setConfParam("lcd_refreshrate", conf.getRefreshRate());
		wipfConfig.setConfParam("lcd_widht", conf.getWidth());
		wipfConfig.setConfParam("lcd_height", conf.getHeight());
		wipfConfig.setConfParam("lcd_baudrate", conf.getBaudRate());
		wipfConfig.setConfParam("lcd_type", conf.getType().name());

		LOGGER.info("Config speichern");
	}

	/**
	 * @param jnRoot
	 * @throws SQLException
	 */
	public void setConfig(String jnRoot) throws SQLException {
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
