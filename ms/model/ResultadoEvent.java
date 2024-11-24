package ms.model;

/*
 * Classe de campo único para acompanhar o resultado do jogo.
*/
public class ResultadoEvent {

	private final boolean ganhou;
	
	public ResultadoEvent(boolean ganhou) {
		this.ganhou = ganhou;
	}
	
	public boolean Ganhou() {
		return ganhou;
	}
}