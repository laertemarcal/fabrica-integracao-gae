package br.ufg.integracao.gcm;

import static br.ufg.integracao.gcm.utilities.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static br.ufg.integracao.gcm.utilities.CommonUtilities.EXTRA_MESSAGE;
import static br.ufg.integracao.gcm.utilities.CommonUtilities.SENDER_ID;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import br.ufg.integracao.gcm.utilities.AlertDialogManager;
import br.ufg.integracao.gcm.utilities.WakeLocker;

import com.google.android.gcm.GCMRegistrar;

/**
 * 
 * @author Laerte Filho
 *
 * Activity Principal
 * <p>
 * Checa o registro do dispositivo no GCM e mostra todas as mensagens recebidas pelas notificações
 * 
 */

public class ShowMessageActivity extends Activity {
	// TextView para mostrar as mensagens.
	TextView lblMessage;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_message);

		// Verifica se o dispositivo contém as especificações exigidas pela
		// aplicação.
		GCMRegistrar.checkDevice(this);

		// Verifica se o manifesto foi configurado corretamente.
		GCMRegistrar.checkManifest(this);

		lblMessage = (TextView) findViewById(R.id.lblMessage);

		// Seta o TextView com a mensagem recebida pela notificação.
		if (getIntent().getStringExtra(EXTRA_MESSAGE) != null) {
			lblMessage.setTextColor(Color.YELLOW);
			lblMessage
					.setText(getIntent().getStringExtra(EXTRA_MESSAGE) + "\n");
		}

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// Seta o ID de Registro do dispositivo na variável regId.
		final String regId = GCMRegistrar.getRegistrationId(this);
		System.err.print(regId);
		// Checa o regId novamente, se não estiver registrado, o registra e
		// checa novamente.
		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Re-checa o registro do dispositivo no GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				return;
			} else {
				GCMRegistrar.setRegisteredOnServer(this, true);
			}
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Usa o WakeLocker para acordar o dispositivo
			WakeLocker.acquire(getApplicationContext());

			// Mostra a mensagem recebida na UI
			lblMessage.append(newMessage + "\n");
			Toast.makeText(getApplicationContext(),
					"Nova Mensagem: " + newMessage, Toast.LENGTH_LONG).show();

			// Desliga o WakeLocker
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("Erro no unregisterReceiver", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
