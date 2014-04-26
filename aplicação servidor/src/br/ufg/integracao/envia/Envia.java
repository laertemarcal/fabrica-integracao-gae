package br.ufg.integracao.envia;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author Laerte Filho
 * <p>
 *         Classe responsável pelo POST HTTP enviando a notificação.
 * 
 * @param message
 *            String - mensagem a ser enviada.
 */

public class Envia {
	private static final String URL = "https://android.googleapis.com/gcm/send";
	private static final String APIKEY = ConfigReader.getApiKey();

	public Envia(String regId, String message) {
		enviaNotificacao(regId, message);
	}

	public Envia(String message) {
		enviaNotificacao(ConfigReader.getRegId(), message);
	}

	public Envia() {
		enviaNotificacao(ConfigReader.getRegId(), "Mensagem recebida!");
	}

	private void enviaNotificacao(String registrationId, String message) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams
				.add(new BasicNameValuePair("registration_id", registrationId));
		formparams.add(new BasicNameValuePair("data.price", message));
		formparams.add(new BasicNameValuePair("time_to_live", Integer
				.toString(86400)));
		formparams.add(new BasicNameValuePair("delay_while_idle", Boolean
				.toString(true)));

		HttpPost httpPost = new HttpPost(URL);
		httpPost.setHeader("Authorization", "key=" + APIKEY);
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
			HttpResponse httpResponse = httpclient.execute(httpPost);
			System.out.println(EntityUtils.toString(httpResponse.getEntity()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
