import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase resolver.
 * 
 * Lleva todo el proceso de la solución del sudoku. Es runnable, para poder hacerlo
 * thread y manejarlo como tal
 */
public class Resolver implements Runnable {

	/** Objeto del tipo tablero. */
	private Tablero tablero;
	
	/** Comprobacion de si hay que matar el thread. */
	private boolean matarThread = false;

	/**
	 * Resolver.
	 * 
	 * Utilizando el nombre del archivo que le pasan como parametro, lee los valores
	 * en el siguiente formato:
	 * 
	 * 000075400000000008080190000300001060000000034000068170204000603900000020530200000
	 * 
	 * es decir 81 numeros con las posiciones vacias como 0;
	 * @param archivo nombre del archivo que tendra que estar en la misma carpeta
	 * 
	 * @throws sudokuNoValidoException Excepcion que implica que el sudoku no es valido
	 */
	public Resolver(String archivo) throws sudokuNoValidoException {

		int[][] tabla = new int[9][9];
		File f = new File(archivo);
		BufferedReader entrada = null;
		try {
			entrada = new BufferedReader( new FileReader( f ) );
		} catch (FileNotFoundException e) {
			throw new sudokuNoValidoException("No se ha encontrado el archivo");
		}
		if (f.exists()){
			String texto = "";
			int contador = 0;
			try {
				texto = entrada.readLine();
			} catch (IOException e) {
				throw new sudokuNoValidoException("No se ha podido leer el archivo");
			}
			boolean primero = true;
			for ( int a = 0; a < 9; a++) {
				for (int b = 0; b < 9; b++) {
					int n = texto.charAt(contador) - 48;
					//Guardamos la posicion del primer cero para saber donde empezar
					if (n == 0 && primero) {
						primero = false;
					}
					if (n!= 0) {
						GUI.pintaNumIni(a, b, n);
					}
					contador++;
					tabla[a][b] = n;
				}
			}
		}
		else
			throw new sudokuNoValidoException("No se ha podido leer el archivo");
		Tablero tableroJuego = new Tablero(tabla);
		tablero = tableroJuego;
	}

	/**
	 * Resolver. Resuelve el sudoku
	 *
	 * @param i la fila
	 * @param j la columna
	 * @return true, si ha coseguido poner el numero
	 * @throws sudokuNoValidoException Excepción para sudoku no valido
	 * @throws matarHiloException the matar hilo exception
	 */
	private boolean resolver(int i, int j) throws sudokuNoValidoException, matarHiloException {
		
		//Lanza una excepcion para que salga del backtracking y la maneje el llamante
		if (matarThread)
			throw new matarHiloException();
		boolean terminado = false;
		ArrayList<Integer> candidatos = tablero.getCandidatos(i, j);
		//Comprobamos si es null (si es una posicion de tablero inicial)
		if (candidatos == null) {
			if (j == 8 && i == 8) {
				boolean fin = false;
				fin = acabado();
				return fin;
			}
			else if (j == 8) {
				terminado = resolver (i + 1, 0);
				return terminado;
			}
			else {
				terminado = resolver (i, j + 1);
				return terminado;
			}
		}
		int nroCandidato = candidatos.size();
		if (nroCandidato == 0) {
			tablero.setNum(i, j, 0);
			GUI.limpia(i, j);
			return false;
		}
		//Ejecutamos un bucle infinito que cuando acabe devolvera true o false segun haya conseguido o no poner
		//un numero en su casilla
		while(true) {
			boolean asignado = false;
			nroCandidato--;
			int nroProbar = candidatos.get(nroCandidato);
			asignado = tablero.setNum(i, j, nroProbar);
			GUI.pintaNum(i, j, tablero.getNum(i, j));
			if (asignado) {
				if (j == 8 && i == 8) {
					boolean fin = false;
					fin = acabado();
					return fin;
				}
				else if (j == 8) {
					GUI.pintaNum(i, j, tablero.getNum(i, j));
					terminado = resolver (i + 1, 0);
					if (terminado) {
						return true;
					}
				}
				else {
					GUI.pintaNum(i, j, tablero.getNum(i, j));
					terminado = resolver (i, j + 1);
					if (terminado) {
						return true;
					}
				}
			}
			if (nroCandidato == 0) {
				tablero.setNum(i, j, 0);
				GUI.limpia(i, j);
				return false;
			}
		}
	}

	/**
	 * Acabado. Comprueba si hemos llegado al final
	 *
	 * @return true, si ya hemos terminado el sudoku
	 */
	private boolean acabado() {
		
		for ( int a = 0; a < 9; a++) {
			for (int b = 0; b < 9; b++) {
				int n = tablero.getNum(a, b);
				if (n == 0)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Finalizar. Acaba con el thread poniendo a true la variable matarThread
	 */
	public void finalizar() {
		matarThread = true;
	}

	/** Run.
	 * 
	 * Se ejecuta al comenzar un hilo.
	 * Cuando termina la ejecucion, el hilo muere
	 */
	public void run() {
		boolean resuelto = false;
		try {
			resuelto = resolver(0, 0);
		} catch (sudokuNoValidoException e) {
			e.printStackTrace();
		} catch (matarHiloException e) {
			//Termina la ejecucion de este hilo
			GUI.limpiar();
		}		
		if (resuelto) {
			GUI.terminado();
		}
		else if (!matarThread)
			GUI.noSolucion();
	}
	
	/**
	 * Excepcion matarHiloException.
	 * 
	 * No contiene nada pues solo interesa saber que es una excepcion concreta debida
	 * a que ese hilo debe terminar su ejecucion
	 */
	private class matarHiloException extends Exception {
		
	}
}

