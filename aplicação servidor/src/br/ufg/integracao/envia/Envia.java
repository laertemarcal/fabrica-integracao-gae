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
 * String - mensagem a ser enviada.
 */

public class Envia {
	private String URL = "https://android.googleapis.com/gcm/send";
	private String SENDER_ID = "AIzaSyASGKkq29juNaOfRxCuTDFm1iDn0QhG4p8";

	public Envia(String regId, String message) {
		enviaNotificacao(regId, message);
	}

	public Envia(String message) {
		enviaNotificacao(
				"APA91bFJn-0L8KbduvD7ykck4-fhdLxhzCMJlFoA6XVhU6k_wo9pis46bCyIiO5Axfbfa6xWagxCsLykR67JCXK1By6FG6Ywxofct84dWW3-1cJJce1CbwRgspdKIc0YN7P6nJBgW6oYhmy8AJvdizbJHv070cDYhw",
				message);
	}

	public Envia() {
		enviaNotificacao(
				"APA91bFJn-0L8KbduvD7ykck4-fhdLxhzCMJlFoA6XVhU6k_wo9pis46bCyIiO5Axfbfa6xWagxCsLykR67JCXK1By6FG6Ywxofct84dWW3-1cJJce1CbwRgspdKIc0YN7P6nJBgW6oYhmy8AJvdizbJHv070cDYhw",
				"Mensagem recebida!");
	}

	private void enviaNotificacao(String registrationId, String message) {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams
				.add(new BasicNameValuePair("registration_id", registrationId));
		formparams.add(new BasicNameValuePair("data.price", message));

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
