package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.jasmarty.LcdConfig;
import org.wipf.jasmarty.logic.base.WipfConfig;

import com.fazecast.jSerialComm.SerialPort;

@ApplicationScoped
public class SerialConfig {

	private static final Logger LOGGER = Logger.getLogger("SerialConfig");

	@Inject
	WipfConfig wipfConfig;

	/**
	 * @return
	 * @throws SQLException
	 */
	public LcdConfig getConfig() throws SQLException {
		try {
			LcdConfig conf = new LcdConfig();

			conf.setPort(wipfConfig.getConfParamString("port"));
			conf.setRefreshRate(wipfConfig.getConfParamInteger("refreshrate"));
			conf.setWidth(wipfConfig.getConfParamInteger("widht"));
			conf.setHeight(wipfConfig.getConfParamInteger("height"));
			conf.setBaudRate(wipfConfig.getConfParamInteger("baudrate"));

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

		setConfig(lcDef);
		return lcDef;
	}

	/**
	 * @param conf
	 * @throws SQLException
	 */
	public void setConfig(LcdConfig conf) throws SQLException {
		wipfConfig.setConfParam("port", conf.getPort());
		wipfConfig.setConfParam("refreshrate", conf.getRefreshRate());
		wipfConfig.setConfParam("widht", conf.getWidth());
		wipfConfig.setConfParam("height", conf.getHeight());
		wipfConfig.setConfParam("baudrate", conf.getBaudRate());

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
