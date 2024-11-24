package ms.model;

/*
 * Interface que define o que um Observador de um campo deve incluir/fazer.
*/
@FunctionalInterface
public interface CampoObserver {
	public void eventoOcorreu(Campo f, CampoEvent e);
}