package br.ufg.integracao.envia;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private static final String FILEPATH = "C:/Users/Laerte Filho/Desktop/config.ini";
	private static final Properties config = new Properties();
	private static String data;

	static String getApiKey() {
		return getData("apiKey");
	}

	static String getRegId() {
		return getData("regId");
	}

	private static String getData(String dataType) {
		try {
			config.load(new FileInputStream(FILEPATH));
			data = config.getProperty(dataType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}
