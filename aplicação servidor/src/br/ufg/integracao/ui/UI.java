package br.ufg.integracao.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import br.ufg.integracao.envia.Envia;

public class UI extends JFrame implements ActionListener {

	/**
	 * Interface gráfica da aplicação
	 */

	private static final long serialVersionUID = 8414190459255647741L;
	private JLabel rotulo;
	private JTextArea campo;
	private JButton botao;

	private UI() {
		setTitle("Envia Notificação");
		setSize(320, 240);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		Font font = new Font("Segoe UI", Font.BOLD, 14);

		Container container = getContentPane();
		container.setLayout(new BorderLayout(10, 10));

		rotulo = new JLabel("Insira a mensagem da notificação");
		rotulo.setFont(font);

		campo = new JTextArea();
		campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		campo.setLineWrap(true);
		campo.setWrapStyleWord(true);

		botao = new JButton("Enviar");
		botao.addActionListener(this);
		botao.setFont(font);

		container.add(rotulo, BorderLayout.NORTH);
		container.add(campo, BorderLayout.CENTER);
		container.add(botao, BorderLayout.SOUTH);

	}

	public void actionPerformed(ActionEvent e) {
		enviarMensagem();
	}

	private void enviarMensagem() {
		String mensagem = campo.getText();
		if (mensagem.equals("")) {
			new Envia();
		} else {
			new Envia(mensagem);
		}
	}

	public static void main(String[] args) {
		new UI();
	}
}