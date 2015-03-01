import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * Clase tablero
 * 
 * Representa el tablero del sudoku.
 */
public class Tablero {

	/** Array de dos dimensiones de enteros que contiene como vamos probando el tablero. */
	private int[][] tablero;
	
	/** Array de dos dimensiones de booleanos para saber si en una posici�n es un n�mero dado al inicio o no. */
	final private boolean[][] tableroIni;

	/** Tama�o de juego. */
	final private int size = 9;

	/**
	 * Crea un nuevo tablero a partir de un tablero inicial.
	 *
	 * @param tableroI Tablero inicial de juego
	 */
	public Tablero(int[][] tableroI) {
		tablero = new int[size][size];
		tablero = tableroI;
		tableroIni = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (tableroI[i][j] != 0) 
					tableroIni[i][j] = true;
			}
		}
	}

	/**
	 * Devuelve el n�mero en una posici�n.
	 *
	 * @param i coordenada en el eje Y
	 * @param j coordenada en el eje X
	 * @return el numero en (X,Y)
	 */
	public int getNum(int i, int j) {
		return tablero[i][j];
	}

	/**
	 * Pone un n�mero en una posici�n, pero primero comprueba si
	 * esa posici�n es inicial (no se puede tocar) y si lo es,
	 * devuelve false y no hace nada.
	 *
	 * @param i coordenada en el eje Y
	 * @param j coordenada en el eje X
	 * @param num n�mero a ponero en la posici�n
	 * @return true si se puede poner
	 */
	public boolean setNum(int i, int j, int num) {
		if (compPos(i, j)) {
			tablero[i][j] = num;
			return true;
		}
		return false;
		
	}

	/**
	 * Devuelve un ArrayList con los candidatos que hay en cada posici�n
	 * teniendo en cuenta los que ya hay puestos.
	 *
	 * @param i coordenada en el eje Y
	 * @param j coordenada en el eje X
	 * @return candidatos a poner en esa posici�n
	 */
	public ArrayList<Integer> getCandidatos(int i, int j) {

		//Si es posici�n inicial devuelve null
		if (!compPos(i, j))
			return null;

		ArrayList<Integer> candidatos = new ArrayList<Integer>();

		//Desde 1 hasta 9 incluido, comprobamos si se puede poner
		for (int num = 1; num <= size; num++) {
			if (CompX(num, i) && CompY(num, j)
					&& CompCuadrante(num, i, j)) {
				candidatos.add(num);
			}
		}
		return candidatos;
	}

	/**
	 * Comprueba que el n�mero que le pases no est� utilizado en
	 * ese cuadrante ya.
	 *
	 * @param num numero a comprobar
	 * @param fila fila de la posici�n
	 * @param columna columna de la posici�n
	 * @return true, si no est�
	 */
	private boolean CompCuadrante(int num, int fila, int columna) {

		double q = fila / 3;
		double p = columna / 3;
		int cuadrFila, cuadrColumna;

		if (q < 1)
			cuadrFila = 0;
		else if (q < 2)
			cuadrFila = 3;
		else
			cuadrFila = 6;

		if (p < 1)
			cuadrColumna = 0;
		else if (p < 2)
			cuadrColumna = 3;
		else
			cuadrColumna = 6;

		int f = cuadrFila + 3;
		int c = cuadrColumna + 3;

		for (int cFila = cuadrFila; cFila < f; cFila++) {
			for (int cColumna = cuadrColumna; cColumna < c; cColumna++) {
					if (tablero[cFila][cColumna] == num)
						return false;
			}
		}
		return true;
	}

	/**
	 * Comprueba que el n�mero no est� en su misma
	 * fila.
	 *
	 * @param num n�mero a comprobar
	 * @param fila fila que queremos comprobar
	 * @return true, si no est�
	 */
	private boolean CompX(int num, int fila) {
		for (int x = 0; x < size; x++) {
				if (tablero[fila][x] == num)
					return false;
		}
		return true;
	}

	/**
	 * Comprueba que el n�mero no est� en su misma
	 * columna.
	 *
	 * @param num n�mero a comprobar
	 * @param columna columna que queremos comprobar
	 * @return true, si no est�
	 */
	private boolean CompY(int num, int columna) {
		for (int y = 0; y < size; y++) {
				if (tablero[y][columna] == num)
					return false;
		}
		return true;
	}

	/**
	 * Comprueba que la posicion no contenga uno de los
	 * n�meros que daban al inicio.
	 *
	 * @param i coordenada en el eje Y
	 * @param j coordenada en el eje X
	 * @return true, si no es posici�n inicial
	 */
	public boolean compPos(int i, int j) {
		
			boolean si = !tableroIni[i][j];
			return si;
	}
}
