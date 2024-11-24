package ms.model;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;

/*
 * Classe para o Campo.
 * Guarda 3 grupos de informações pricipais:
 * 
 * Posição (em Linha X e Coluna Y);
 * Estado no jogo (Minado, aberto e/ou Marcado);
 * Listas dos Vizinhos (para lógica do jogo) e dos Observadores (para Notificação de eventos).
*/
public class Campo {
	
	private final int lin_X;
	private final int col_Y;
	
	private boolean minado;
	private boolean aberto;
	private boolean marcado;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObserver> observadores = new ArrayList<>();
	
	Campo(int x, int y){
		this.lin_X = x;
		this.col_Y = y;
	}
	
	/*
	 * Métodos relacionados aos Observadores.
	*/
	public void registrarObservador(CampoObserver observer) {
		observadores.add(observer);
	}
	public void notificarObservadores(CampoEvent e) {
		observadores.stream().forEach(obs -> obs.eventoOcorreu(this, e));
	}
	
	/*
	 * Método para adicionar um outro campo para lista de vizinhos
	 * Um campo pode ser considerado vizinho de outro se este estiverem
	 * adjacentes ou nas diagonais.
	*/
	boolean adicionarVizinho(Campo vizinho) {
		boolean isDiagonal = lin_X != vizinho.lin_X && col_Y != vizinho.col_Y;
		int delta = abs(lin_X - vizinho.lin_X) + abs(col_Y - vizinho.col_Y);
		
		if (delta == 1 && !isDiagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (delta == 2 && isDiagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Alterna (de forma booleana, SIM/NÃO) a marcação do Campo,
	 * notificando os observadores da mudança.
	*/
	public void alterarMarcacao() {
		if (!aberto) {
			marcado = !marcado;
			
			if (marcado) {
				notificarObservadores(CampoEvent.MARCAR);
			}
			else {
				notificarObservadores(CampoEvent.DESMARCAR);
			}
		}
	}

	void mine() {
		minado = true;
	}
	
	/*
	 * Abre o campo, caso esteja minado, os Observadores serão notificados da explosão,
	 * caso a vizinhaça (campos adjacentes) sejam seguros (não tenham minas), a vizinhaça
	 * será aberta.
	*/
	public boolean abrir() {
		if (!aberto && !marcado) {
			aberto = true;
			
			if (minado) {
				notificarObservadores(CampoEvent.EXPLODIR);
				return true;
			}
			
			setAberto(true);
			
			if (vizinhacaSegura()) {
				vizinhos.forEach(neighbor -> neighbor.abrir());
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Retorna se a vizinhaça é segura (sem minas em nenhum dos campos).
	*/
	public boolean vizinhacaSegura() {
		return vizinhos.stream().noneMatch(neighbor -> neighbor.isMinado());
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if(aberto) {
			notificarObservadores(CampoEvent.ABRIR);
		}
	}
	
	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isLocked() {
		return !aberto;
	}

	public int getLinha_X() {
		return lin_X;
	}

	public int getColuna_Y() {
		return col_Y;
	}
	
	/*
	 * Retorna se o objetivo do campo foi alcançado,
	 * para determinar o estado de vitória do jogo. 
	*/
	boolean alcancouObjetivo() {
		return (!minado && aberto) || (minado && marcado);
	}
	
	/*
	 * Conta o número de minas na vizinhaça.
	*/
	public long minasNaVizinhaca() {
		return vizinhos.stream().filter(neighbor -> neighbor.minado).count();
	}
	
	/*
	 * Reinicia o estado do campo para o mesmo quando é originalmente inicializado.
	*/
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvent.REINICIAR);
	}
}