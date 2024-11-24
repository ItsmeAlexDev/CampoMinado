package ms.vision;

import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.GridLayout;

import javax.swing.JPanel;

import ms.model.Tabuleiro;

@SuppressWarnings("serial")
public class TabuleiroPanel extends JPanel {
	
	/*
	 * Painel para a janela do jogo.
	 * 
	 * Cria os botões para cada campo.
	 * 
	 * Exibe mensagem de sucesso/fracasso
	*/
	public TabuleiroPanel(Tabuleiro Tabuleiro) {
		int linhas = Tabuleiro.getLinhas();
		int colunas = Tabuleiro.getColunas();

		setLayout(new GridLayout(linhas, colunas));
		
		Tabuleiro.paraCadaCampo(c -> add(new CampoBotao(c)));
		
		Tabuleiro.registrarObservador(e -> {
			invokeLater(() -> {
				if (e.Ganhou()) {
					showMessageDialog(this, "Você Ganhou o jogo");
				} else {
					showMessageDialog(this, "YOU LOST!");
				}
				Tabuleiro.reiniciarJogo();
			});
		});
	}
}