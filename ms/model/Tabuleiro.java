package ms.model;

import static java.lang.Math.random;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/*
 * Classe para o tabuleiro, conjunto de Campos
 * guarda 2 grupos de informações principais:
 * 
 * Informações sobre os campos (número de linhas, colunas e minas);
 * Listas dos campos e dos Observadores para serem notificados.
*/
public class Tabuleiro implements CampoObserver{
	
	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvent>> observadores = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		minarCampos();
	}
	
	public void paraCadaCampo(Consumer<Campo> f) {
		campos.forEach(f);
	}
	
	public void registrarObservador(Consumer<ResultadoEvent> observer) {
		observadores.add(observer);
	}
	
	public void notificarObservadores(boolean result) {
		observadores.stream().forEach(obs -> obs.accept(new ResultadoEvent(result)));
	}
	
	/*
	 * Abre um campo com base nas coordenadas recebidas como parametro.
	*/
	public void abrir(int row, int col) {
			campos.stream()
				.filter(field -> field.getLinha_X() == row &&
				     	         field.getColuna_Y() == col)
				.findFirst()
				.ifPresent(field -> field.abrir());
	}
	
	/*
	 * Alterna a marcação de um campo com base nas coordenadas recebidas como parametro.
	*/
	public void alterarMarcacao(int row, int col) {
		campos.stream()
			.filter(field -> field.getLinha_X() == row &&
			     	         field.getColuna_Y() == col)
			.findFirst()
			.ifPresent(field -> field.alterarMarcacao());
	}

	/*
	 * gera uma matriz de campos para o tabuleiro.
	*/
	private void gerarCampos() {
		for (int row = 0; row < linhas; row++)
			for (int col = 0; col < linhas; col++) {
				Campo field = new Campo(row, col);
				field.registrarObservador(this);
				campos.add(field);
			}
	}

	/*
	 * Associa os campos como vizinhos uns dos outros.
	*/
	private void associarVizinhos() {
		for (Campo field_1 : campos)
			for (Campo field_2 : campos)
				field_1.adicionarVizinho(field_2);
	}
	
	/*
	 * Mina os campos de maneira aleatória até alcançar o número desejado de minas.
	*/
	private void minarCampos() {
		long minedFields = 0;
		
		while (minedFields < minas) {
			int rnd = (int) (random() * campos.size());
			campos.get(rnd).mine();
			minedFields = campos.stream().filter(field -> field.isMinado()).count();
		}
	}

	public boolean ganhou() {
		return campos.stream().allMatch(field -> field.alcancouObjetivo());
	}
	
	public void reiniciarJogo() {
		campos.stream().forEach(field -> field.reiniciar());
		minarCampos();
	}
	
	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public int getMinas() {
		return minas;
	}
	
	public List<Campo> getCampos() {
		return unmodifiableList(campos);
	}

	/*
	 * Lida com os eventos notificados de cada campo.
	*/
	@Override
	public void eventoOcorreu(Campo f, CampoEvent e) {
		if (e == CampoEvent.EXPLODIR) { 
			notificarObservadores(false);
			showMines();
		}
		else if(ganhou()) {
			notificarObservadores(true);
		}
	}
	
	/*
	 * Abre todos os campos que estejam minados para mostrar as minas.
	*/
	private void showMines() {
		campos.stream()
			  .filter(f -> f.isMinado())
			  .forEach(field -> field.setAberto(true));
	}
}