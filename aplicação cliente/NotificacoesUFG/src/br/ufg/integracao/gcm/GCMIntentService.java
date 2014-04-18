package br.ufg.integracao.gcm;

import static br.ufg.integracao.gcm.utilities.CommonUtilities.SENDER_ID;
import static br.ufg.integracao.gcm.utilities.CommonUtilities.displayMessage;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * 
 * @author Laerte Filho
 * 
 *         IntentService do GCM
 */

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Dispositivo registrado, regId = " + registrationId);
		displayMessage(context, "Seu dispositivo foi registrado no GCM");
		displayMessage(context, "e está pronto para receber notificações");
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device desregistrado");
		displayMessage(context, getString(R.string.gcm_unregistered));
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("price");// price

		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Gera a notificação na barra de status do dispositivo.
	 */
	private void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		long[] pattern = { 0, 300, 100, 700, 100 };
		String title = context.getString(R.string.app_name);
		Uri sound = Uri.parse("android.resource://" + getPackageName() + "/"
				+ R.raw.bloop);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setSmallIcon(icon).setContentTitle(title)
				.setContentText(message);

		Intent notificationIntent = new Intent(this, ShowMessageActivity.class);
		notificationIntent.putExtra("message", message);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(contentIntent);
		builder.setAutoCancel(true);
		builder.setLights(Color.BLUE, 500, 500);
		builder.setWhen(when);
		builder.setVibrate(pattern);
		builder.setSound(sound);
		builder.setStyle(new NotificationCompat.InboxStyle());

		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1, builder.build());
	}
}
