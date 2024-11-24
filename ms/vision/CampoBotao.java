package ms.vision;

import static java.awt.Color.GRAY;
import static javax.swing.BorderFactory.createBevelBorder;
import static javax.swing.BorderFactory.createLineBorder;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import ms.model.Campo;
import ms.model.CampoEvent;
import ms.model.CampoObserver;

/*
 * Classe para os botões que representam visualmente os campos
*/
@SuppressWarnings("serial")
public class CampoBotao extends JButton
	implements CampoObserver, MouseListener{
	
	// Paleta de cores usada
	private final Color DEFAULT = new Color(184, 184, 184);
	private final Color ABERTO = new Color(175, 188, 200);
	private final Color MARCADO = new Color(8, 179, 247);
	private final Color EXPLODIDO = new Color(189, 66, 68);
	private final Color TEXTO_VERDE = new Color(0, 100, 0);
	
	private Campo Campo;
	
	/*
	 * Cria a aparência/detalhes do botão
	*/
	public CampoBotao(Campo Campo) {
		this.Campo = Campo;
		setBackground(DEFAULT);
		setOpaque(true);
		setBorder(createBevelBorder(0));
		
		setFocusable(false);
		
		addMouseListener(this);
		Campo.registrarObservador(this);
	}

	/*
	 * Altera o Estilo do Botão dependendo do evento.
	*/
	@Override
	public void eventoOcorreu(Campo f, CampoEvent e) {
		switch (e) {
			case ABRIR:
				aplicarEstiloABERTO();
				break;
			case MARCAR:
				aplicarEstiloMARCADO();
				break;
			case EXPLODIR:
				aplicarEstiloEXPLODIDO();
				break;
			default:
				aplicarEstiloDEFAULT();
				break;
		}
	}

	/*
	 * Aplica o Estilo "ABERTO" ao botão.
	*/
	private void aplicarEstiloABERTO() {
		setBorder(createLineBorder(GRAY));
		setOpaque(true);
		setBackground(ABERTO);
		if(Campo.isMinado()) {
			setBackground(EXPLODIDO);
		} else  {
			setForeground(TEXTO_VERDE);
		}
		String text = Campo.isMinado() ? "💣" :
					 (Campo.vizinhacaSegura() ? "" : "" + Campo.minasNaVizinhaca());
		setText(text);
	}
	/*
	 * Aplica o Estilo "MARCADO" ao botão.
	*/
	private void aplicarEstiloMARCADO() {
		setOpaque(true);
		setBackground(MARCADO);
		setText("🚩");
	}
	/*
	 * Aplica o Estilo "EXPLOSÃO" ao botão.
	*/
	private void aplicarEstiloEXPLODIDO() {
		setOpaque(true);
		setBackground(EXPLODIDO);
		setText("💣");
	}

	/*
	 * Aplica o Estilo "DEFAULT" ao botão.
	*/
	private void aplicarEstiloDEFAULT() {
		setOpaque(true);
		setBackground(DEFAULT);
		setBorder(createBevelBorder(0));
		setText("");
	}

	/*
	 * Códigos necessários pela Herança/Interfaces da Classe
	*/
	@Override
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	/*
	 * Abre campo quando clicado o botão principal (Esquerdo);
	 * Alterna a marcação quando clicado qualquer outro (em especial, o Direito)
	*/
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			Campo.abrir();
		} else {
			Campo.alterarMarcacao();
		}
	}
}