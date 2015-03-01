
/**
 * Clase sudokuIrresolubleException.
 * 
 * Hemos creado esta excepci�n para que salte si el Sudoku no tiene soluci�n
 */
@SuppressWarnings("serial")
public class sudokuNoValidoException extends Exception {

		/**
		 * Crea una excepci�n 
		 *
		 * @param cadena el texto que queremos que aparezca
		 */
		sudokuNoValidoException(String cadena) {

		super( cadena );

		}
}
