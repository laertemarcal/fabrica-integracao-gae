package br.ufg.integracao.gcm.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import br.ufg.integracao.gcm.R;

public class AlertDialogManager {
	/**
	 * Método para gerar o AlertDialog
	 * 
	 * @param context
	 *            - contexto da app
	 * @param title
	 *            - título do alert dialog
	 * @param message
	 *            - mensagem de alerta
	 * @param status
	 *            - seta o ícone: sucesso/falha (true/false)
	 * */
	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		// Seta o título do AlertDialog
		alertDialog.setTitle(title);

		// Seta a mensagem
		alertDialog.setMessage(message);

		if (status != null)
			// Seta o ícone
			alertDialog
					.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Seta o Botão OK, que fechará a aplicação.
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		});

		// Mostra o AlertDialog
		alertDialog.show();
	}
}
