package br.ufg.integracao.gcm;

import static br.ufg.integracao.gcm.utilities.CommonUtilities.SENDER_ID;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.ufg.integracao.gcm.utilities.AlertDialogManager;
import br.ufg.integracao.gcm.utilities.ConnectionDetector;

import com.google.android.gcm.GCMRegistrar;

/**
 * 
 * @author Laerte Filho
 *
 */
public class CheckActivity extends Activity {
	// alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// Internet detector
	ConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check);

		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectedToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(CheckActivity.this, "Falha na Conexão",
					"Por favor conecte-se a internet", false);
			// stop executing code by return
			return;
		}

		// Checa se já foi registrado no GCM, se já, vai para a Activity
		// principal para receber notificações.
		// Se não, cria o botão de registro e continua o código.
		if (GCMRegistrar.isRegisteredOnServer(this)) {
			Toast.makeText(getApplicationContext(),
					"Pronto para receber notificações", Toast.LENGTH_LONG)
					.show();
			Log.d("regId", "O REGID do dispositivo é: " + GCMRegistrar.getRegistrationId(this));
			Intent i = new Intent(getApplicationContext(), ShowMessageActivity.class);
			startActivity(i);
			finish();
		} else {
			Intent i = new Intent(getApplicationContext(), ShowMessageActivity.class);
			startActivity(i);
			finish();
		}
	}
}
