
/**
 * Clase sudokuIrresolubleException.
 * 
 * Hemos creado esta excepción para que salte si el Sudoku no tiene solución
 */
@SuppressWarnings("serial")
public class sudokuNoValidoException extends Exception {

		/**
		 * Crea una excepción 
		 *
		 * @param cadena el texto que queremos que aparezca
		 */
		sudokuNoValidoException(String cadena) {

		super( cadena );

		}
}
