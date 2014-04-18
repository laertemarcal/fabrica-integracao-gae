package br.ufg.integracao.gcm.utilities;

import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author Laerte Filho
 * 
 *         Classe utilitária
 *         <p>
 *         Fornece variáveis constantes para facilitar a comunicação com o GCM
 *         Tais como o ID do Projeto Google, Tag usada no LogCat e variáveis de
 *         ação do Broadcast
 * 
 */

public final class CommonUtilities {

	// Id do Projeto Google
	public static final String SENDER_ID = "984742398302";
	
	/**
	 * TAG usada no LogCat.
	 */
	public static final String TAG = "UFG GCM";

	public static final String DISPLAY_MESSAGE_ACTION = "br.ufg.integracao.gcm.DISPLAY_MESSAGE";

	public static final String EXTRA_MESSAGE = "message";

	/**
	 * Avisa a Interface para que mostre a mensagem.
	 * 
	 * @param context
	 *            contexto da aplicação.
	 * @param message
	 *            mensagem extraída do gcm.
	 */
	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}
