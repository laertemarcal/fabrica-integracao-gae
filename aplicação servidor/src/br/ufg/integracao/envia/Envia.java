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
 * 
 *         Classe responsável pelo POST HTTP enviando a notificação.
 * 
 * @param message
 *            String - mensagem a ser enviada.
 */

public class Envia {
	private String URL = "https://android.googleapis.com/gcm/send";
	private String SENDER_ID = "AIzaSyASGKkq29juNaOfRxCuTDFm1iDn0QhG4p8";

	public Envia(String regId, String message) {
		enviaNotificacao(regId, message);
	}

	public Envia(String message) {
		enviaNotificacao(
				"APA91bEdGqSzO9yzi5Gd44R--EPotFaDudv4FC0JRzYgsEpbOLUjn9GdmTuBCPBjyg5gTbs80i6eWa3NQvEW41ZHaDrBYolZipIO1Id9eyFu9_xHjaxCL4qNCo8Rs4A1kOstNuJlQLlsnJ8dvr-jgmJfbRMXrsuw9Q",
				message);
	}

	public Envia() {
		enviaNotificacao(
				"APA91bEdGqSzO9yzi5Gd44R--EPotFaDudv4FC0JRzYgsEpbOLUjn9GdmTuBCPBjyg5gTbs80i6eWa3NQvEW41ZHaDrBYolZipIO1Id9eyFu9_xHjaxCL4qNCo8Rs4A1kOstNuJlQLlsnJ8dvr-jgmJfbRMXrsuw9Q",
				"Mensagem recebida!");
	}

	private void enviaNotificacao(String registrationId, String message) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams
				.add(new BasicNameValuePair("registration_id", registrationId));
		formparams.add(new BasicNameValuePair("data.price", message));
		formparams.add(new BasicNameValuePair("time_to_live", Integer.toString(88640)));
		formparams.add(new BasicNameValuePair("delay_while_idle", Boolean.toString(true)));

		HttpPost httpPost = new HttpPost(URL);
		httpPost.setHeader("Authorization", "key=" + SENDER_ID);
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
