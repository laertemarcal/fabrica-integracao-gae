package br.ufg.inf.integracao.ufgnotify.client;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.ufg.inf.integracao.ufgnotify.client.util.Constants;
import br.ufg.inf.integracao.ufgnotify.client.view.MainActivity;

import com.google.android.gms.gcm.GoogleCloudMessaging;


/*
 * Classe responsável por tratar o serviço do GCM
 * Aqui serão tratadas as mensagens recebidas pelo servidor
 */
public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//Recebe a mensagem da Intent e manda gerar as notificações.
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (extras != null && !"".equals(extras)) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				String message = extras.getString(Constants.EXTRA_MESSAGE);
				sendNotification(message, this);
				displayMessage(message, this);
				Log.i(Constants.INTENT_SERVICE_TAG, "Recebido: " + extras.toString());
			}
		}
		//Desarma o bloqueio do celular.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	/*
	 * Método responsável por gerar notificações
	 */
	private void sendNotification(String msg, Context context) {
		long when = System.currentTimeMillis();
		long[] pattern = { 0, 300, 100, 700, 100 };
		Uri sound = Uri.parse("android.resource://" + getPackageName() + "/"
				+ R.raw.bloop);
		
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra(Constants.EXTRA_MESSAGE, msg);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(context.getString(R.string.app_name))
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setWhen(when)
				.setSound(sound)
				.setAutoCancel(true)
				.setLights(Color.BLUE, 500, 500)
				.setVibrate(pattern)
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	static void displayMessage(String message, Context context) {
		Intent intent = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
		intent.putExtra(Constants.EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}
