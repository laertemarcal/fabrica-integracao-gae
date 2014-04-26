package br.ufg.integracao.envia;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.ini4j.InvalidFileFormatException;

public class ConfigReader {
	private static final String FILENAME = "C:/Users/Laerte Filho/Desktop/config.ini";
	private static Preferences prefs;
	private static String key;

	public static String getApiKey() {
		try {
			prefs = new IniPreferences(new Ini(new File(FILENAME)));
			key = prefs.node("config").get("apiKey", null);
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}

	public static String getRegId() {
		try {
			prefs = new IniPreferences(new Ini(new File(FILENAME)));
			key = prefs.node("config").get("regId", null);
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}
}
