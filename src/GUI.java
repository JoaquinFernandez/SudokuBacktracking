import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Graphical User Interface. Interfaz gráfica de usuario
 *
 */

public class GUI {
	
	/** Slider que determina la velocidad de refresco de los numeros */
	private Resolver resolver;
	
	/** The velocidad. */
	private JSlider velocidad;
	
	/** Panel que contiene el tablero del sudoku  */
	private static JPanel panel;
	
	/** El marco principal del programa */
	private static JFrame frame;
	
	/** Boton comenzar */
	private static JButton comenzar;
	
	/** El hilo principal de ejecucion */
	private static Thread thread;
	
	/** Tiempo de espera entre numero y numero (ms). */
	private static int tEspera = 500;
	
	/** Estado del boton comenzar. */
	private int estado = 0;
	
	/**
	 * Metodo main
	 *
	 * @param args argumentos
	 * @throws sudokuNoValidoException Excepcion que implica que el sudoku no es válido
	 */
	public static void main(String args[]) throws sudokuNoValidoException{ 
		new GUI();
	}
	
	/**
	 * Pinta los numeros iniciales en el tablero en negrita
	 *
	 * @param a la fila
	 * @param b la columna
	 * @param n numero a pintar
	 */
	public static void pintaNumIni(int a, int b, int n) {
		JTextField text = (JTextField) panel.getComponents()[a*9 + b];
		text.setFont(text.getFont().deriveFont(Font.BOLD));
		text.setText("" + n);
		text.repaint();
	}
	
	/**
	 * Pinta un numero en una casilla de la interfaz
	 *
	 * @param i la fila
	 * @param j la columna
	 * @param num numero a pintar
	 */
	public static void pintaNum(int i, int j, int num) {
		JTextField text = (JTextField) panel.getComponents()[i*9 + j];
		text.setForeground(Color.GRAY);
		text.setText("" + num);
		text.repaint();
			try {
				Thread.sleep(tEspera);
			} catch (InterruptedException e) {
			}
	}
	
	/**
	 * Deja en blanco una determinada casilla de la interfaz
	 *
	 * @param i la fila
	 * @param j la columna
	 */
	public static void limpia(int i, int j) {
		JTextField text = (JTextField) panel.getComponents()[i*9 + j];
		text.setText("");
		text.repaint();
			try {
				Thread.sleep(tEspera);
			} catch (InterruptedException e) {
			}
	}

	/**
	 * se ejecuta cuando finaliza la ejecucion
	 */
	public static void terminado() {
		comenzar.setVisible(false);
		dialogo("¡Sudoku resuelto!");
	}
	
	/**
	 * Se ejecuta si no hay solucion para el sudoku
	 */
	public static void noSolucion() {
		comenzar.setVisible(false);
		dialogo("El Sudoku propuesto no tiene solución");
	}

	/**
	 * Crea una ventana de dialogo
	 *
	 * @param string texto que aparecera en la ventana de dialogo
	 */
	private static void dialogo(String string) {
		final JDialog dialog = new JDialog(new Frame(), "", true);
		dialog.setLocationRelativeTo(frame);
		JLabel boton = new JLabel(string);
		boton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				dialog.setVisible(false);
				dialog.dispose();
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
		boton.setBorder(BorderFactory.createEmptyBorder(  
				20, //arriba  
				10, //izquierda  
				20, //abajo  
				10)); //derecha  
		dialog.add(boton);
		dialog.setLocationRelativeTo(frame);
		dialog.pack();
		dialog.setVisible(true);
		
	}

	/**
	 * Limpia completamente la interfaz de los numeros del sudoku, menos los iniciales
	 */
	public static void limpiar() {
		for (int i = 0; i < 81; i++){
			JTextField text = (JTextField) panel.getComponents()[i];
			if (!text.getFont().isBold()) {
				text.setText("");
				text.repaint();
			}
		}
	}
	
	/**
	 * Crea una nueva GUI
	 */
	public GUI(){
		panel = new JPanel();
		panel.setLayout(new GridLayout(9,9));
		panel.setPreferredSize(new Dimension(300,300));
		panel.setBorder(BorderFactory.createEmptyBorder(  
				10, //arriba  
				30, //izquierda  
				10, //abajo  
				25)); //derecha  

		for (int i = 0; i < 81; i++){
			JTextField text = new JTextField("");
			text.setHorizontalAlignment(0);
			text.setFocusable(false);
			panel.add(text);
		}
		
		JPanel botones = new JPanel();
		botones.setBorder(BorderFactory.createEmptyBorder(  
				15, //arriba  
				0, //izquierda  
				0, //abajo  
				0)); //derecha  
		comenzar = new JButton("Comenzar");
		comenzar.addActionListener(new ButtonListener1());
		JButton reiniciar = new JButton("Reiniciar");
		reiniciar.addActionListener(new ButtonListener2());
		botones.add(comenzar);
		botones.add(reiniciar);
		
		JPanel slider = new JPanel();
		slider.setBorder(BorderFactory.createEmptyBorder(  
				0, //arriba  
				0, //izquierda  
				10, //abajo  
				0)); //derecha
		slider.setLayout(new BorderLayout());
		velocidad = new JSlider();
		velocidad.addChangeListener(new SliderListener());
		velocidad.setMinorTickSpacing(0); //Velocidad maxima (ms) para nuevo numero
		velocidad.setMajorTickSpacing(1000); //Velocidad minima (ms) para nuevo numero
		velocidad.setPaintTicks(true);
		slider.add(velocidad, BorderLayout.SOUTH);
		JLabel textoSlider = new JLabel("Velocidad de Juego");
		textoSlider.setHorizontalAlignment(0);
		slider.add(textoSlider, BorderLayout.CENTER);
		frame = new JFrame("Sudoku");
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(400, 400));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.getContentPane().add(botones, BorderLayout.NORTH);
		frame.getContentPane().add(slider, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			resolver = new Resolver("Sudoku.txt");
		} catch (sudokuNoValidoException e) {
		}
		thread = new Thread(resolver);
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Listener para boton de accion comenzar
	 */
	private class ButtonListener1 implements ActionListener {
		
		/**
		 * Se ejecuta cuando se hace click en el boton
		 */
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent arg0) {
			if (estado == 0) {
				thread.start();
				comenzar.setText("Parar");
				estado = 1;
			}
			else if (estado == 1) {
				thread.suspend();
				comenzar.setText("Continuar");
				estado = 2;
			}
			else if (estado == 2){
				thread.resume();
				comenzar.setText("Parar");
				estado = 1;
			}
			else if (estado == 3){
				comenzar.setText("Comenzar");
				estado = 0;
			}
		}
	}
	
	/**
	 * Listener para boton de accion reiniciar
	 */
	private class ButtonListener2 implements ActionListener {

		/**
		 * Se ejecuta cuando se hace click en el boton
		 */
		public void actionPerformed(ActionEvent arg0) {
			comenzar.setVisible(true);
			resolver.finalizar();
			try {
				resolver = new Resolver("sudoku.txt");
			} catch (sudokuNoValidoException e) {
			}
			thread = new Thread(resolver);
			estado = 3;
			comenzar.doClick();
		}
	}
	
	/**
	 * Listener del tipo Slider
	 */
	private class SliderListener implements ChangeListener {

		/**
		 * Cuando cambia el estado del listener se actualiza el tiempo de espera
		 */
		public void stateChanged(ChangeEvent arg0) {
			//Se multiplica x10 puesto que getValue() da un valor entre 0 y 100 y nuestro intervalo es de 0 a 1000
			tEspera = velocidad.getValue()*10; 
			
		}
	}
}

