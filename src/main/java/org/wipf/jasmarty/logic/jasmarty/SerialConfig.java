package org.wipf.jasmarty.logic.jasmarty;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wipf.jasmarty.datatypes.LcdConfig;
import org.wipf.jasmarty.logic.base.BaseSettings;

import com.fazecast.jSerialComm.SerialPort;

@ApplicationScoped
public class SerialConfig {

	private static final Logger LOGGER = Logger.getLogger("SerialConfig");

	@Inject
	BaseSettings baseSettings;

	/**
	 * @return
	 * @throws SQLException
	 */
	public LcdConfig getConfig() throws SQLException {
		try {
			LcdConfig conf = new LcdConfig();

			conf.setPort(baseSettings.getConfParamString("port"));
			conf.setRefreshRate(baseSettings.getConfParamInteger("refreshrate"));
			conf.setWidth(baseSettings.getConfParamInteger("widht"));
			conf.setHeight(baseSettings.getConfParamInteger("height"));
			conf.setBaudRate(baseSettings.getConfParamInteger("baudrate"));

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
		baseSettings.setConfParam("port", conf.getPort());
		baseSettings.setConfParam("refreshrate", conf.getRefreshRate());
		baseSettings.setConfParam("widht", conf.getWidth());
		baseSettings.setConfParam("height", conf.getHeight());
		baseSettings.setConfParam("baudrate", conf.getBaudRate());

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
