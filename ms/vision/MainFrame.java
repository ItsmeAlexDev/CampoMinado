package ms.vision;

import java.awt.Dimension;

import javax.swing.JFrame;

import ms.model.Tabuleiro;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{
	
	private static int LARGURA = 690;
	private static int ALTURA = 480;
	
	private int MINAS = 20;
	
	private static Dimension TAMANHO = new Dimension(LARGURA, ALTURA);

	public MainFrame() {
		Tabuleiro board = new Tabuleiro(16, 30, MINAS);
		add(new TabuleiroPanel(board));
		
		setTitle("Campo Minado");
		setSize(TAMANHO);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}
}