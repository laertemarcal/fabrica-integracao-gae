package br.ufg.inf.integracao.ufgnotify.client.view;

import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import br.ufg.inf.integracao.ufgnotify.client.R;
import br.ufg.inf.integracao.ufgnotify.client.util.Constants;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {
	TextView mMessage;
	Context context;
	GoogleCloudMessaging gcm;
	String regId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		mMessage = (TextView) findViewById(R.id.txtMessage);
		context = getApplicationContext();
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regId = getRegistrationId(context);

			if (regId == null || "".equals(regId)) {
				registerDeviceInGcm();
			} else {
				Log.d(Constants.TAG, "ID de Registro do Dispositivo: " + regId);
				if (!hasMessage()) {
						Toast.makeText(this, "Pronto para receber notificações...", Toast.LENGTH_SHORT).show();
					}
				}
			if (hasMessage()) {
				String message = getMessage();
				mMessage.setText(message + "\n");
			}
			this.registerReceiver(mMessageReceiver, new IntentFilter(
					Constants.DISPLAY_MESSAGE_ACTION));

		} else {
			Log.i(Constants.TAG, "No valid Google Play Services APK found.");
		}
	}

	
	private boolean hasMessage() {
		String message = getMessage();
		if (message == null || "".equals(message)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @return String - mensagem recebida da intent
	 */
	private String getMessage() {
		return getIntent().getStringExtra(Constants.EXTRA_MESSAGE);
	}

	//Este método refaz a checagem do GooglePlayServices se a aplicação for resumida
	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	//Desregistra o BroadcastReceiver ao sair da aplicação
	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mMessageReceiver);
		} catch (Exception e) {
			Log.e("Erro no unregisterReceiver", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	
	/**
	 * Método responsável por checar se o dispositivo a ser registrado no GCM possui suporte ao Google Play Services
	 * @return true or false
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(Constants.TAG, "Dispositivo não suportado.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Este método retorna o id de registro (registrationId) armazenado nas preferências compartilhadas
	 * @param context
	 * @return registrationId
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(Constants.PROPERTY_REG_ID, "");
		if (registrationId == null || "".equals(registrationId)) {
			Log.i(Constants.TAG, "Registro não encontrado.");
			return "";
		}
		int registeredVersion = prefs.getInt(Constants.PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(Constants.TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * Método responsável por pegar as preferências armazenadas. No caso, o ID de Registro do dispositivo.
	 * @param context
	 * @return prefs
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Método responsável por pegar a versão do app através de informações do pacote
	 * @param context
	 * @return número da versão da aplicação
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// não deve acontecer (se o nome do pacote não for encontrado)
			throw new RuntimeException(
					"Não foi possível recuperar o nome do pacote: " + e);
		}
	}

	/**
	 * Método responsável por registrar o dispositivo no serviço do GCM
	 * O registro é feito de forma assíncrona, instanciando e executando um objeto AsyncTask
	 */
	private void registerDeviceInGcm() {
		new AsyncTask<Void, Void, String>() {
			protected String doInBackground(Void... params) {
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(Constants.SENDER_ID);
					Log.d(Constants.TAG,
							"Dispositivo registrado, ID de Registro: " + regId);
					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					Log.d(Constants.TAG,
							"Erro ao gravar ID de Registro no dispositivo: "
									+ ex.getMessage());
				}
				return regId;
			}

			@Override
			protected void onPostExecute(String regId) {
				if (regId == null || "".equals(regId)) {
					storeRegistrationId(context, regId);
					Toast.makeText(context,
							"Dispositivo registrado com sucesso!",
							Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}

	/**
	 * Método responsável por registrar os id's de registro dos dispositivos nas
	 * preferências compartilhadas (SharedPreferences)
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i("ID de Registro", "Armazenando ID de Registro na versão "
				+ appVersion + " do app.");
		Log.d("ID de Registro: ", regId);
		Toast.makeText(this, "Dispositivo registrado com sucesso!", Toast.LENGTH_LONG).show();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.PROPERTY_REG_ID, regId);
		editor.putInt(Constants.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/*
	 *  Registra o BroadCastReceiver tratando as mensagens recebidas pelo serviço
	 *  do GCM
	 */
	private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String message = intent.getStringExtra(Constants.EXTRA_MESSAGE);
			mMessage.append(message + "\n");
			Toast.makeText(MainActivity.this, "Nova mensagem: " + message,
					Toast.LENGTH_SHORT).show();
		}
	};
}
