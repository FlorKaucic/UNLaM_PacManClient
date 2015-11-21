package client.conn;

import javax.swing.JOptionPane;

import client.gui.LogInFrame;
import client.gui.LogUpDialog;
import client.gui.StatsFrame;
import client.gui.game.GameFrame;
import client.logic.Parser;

public class ClientProtocol {
	
	public static void processInput(String input) {
		System.out.println(input);
		if (input.startsWith("LOGIN")) {
			System.out.println("LOGIN");
			processLogin(input.substring(5));
		}
		if (input.startsWith("LOGUP")) {
			System.out.println("LOGUP");
			processLogup(input.substring(5));
		}
		if (input.startsWith("MAP")) {
			System.out.println("MAP");
			processMap(input.substring(3));
		}
//		if (input.startsWith("COUNTDOWN")){
//			System.out.println("COUNTDOWN");
//			processCountdown();
//		}
		if (input.startsWith("START")) {
			processStart(input.substring(6));
		}
		if (input.startsWith("STATS")) {
			System.out.println("STATS");
			input = input.substring(5);
		}
	}
	
//	private static void processCountdown() {
//		GameFrame frame = GameFrame.getInstance();
//		frame.initCountdown();
//	}

	private static void processStart(String input) {
		GameFrame frame = GameFrame.getInstance();
		System.out.println("before parse");
		frame.initMatch(Parser.parseCharacters(input));
		System.out.println("after parse");
	}

	private static void processLogin(String input){
		if (input.startsWith("OK")) {
			GameFrame frame = GameFrame.getInstance();
			frame.setUser(Parser.parseUser(input.substring(3)));
			Connection.getInstance().send("GETMAP");
			return;
		}
		JOptionPane.showMessageDialog(null,
				"No se puede ingresar." + "\nCompruebe que el nombre de usuario y la contraseña sean correctos."
						+ "\nSi lo son, su cuenta podria estar deshabilitada por el administrador.",
				"Ingresar al juego", JOptionPane.ERROR_MESSAGE);
		LogInFrame frame = new LogInFrame();
		frame.setVisible(true);
	}
	
	private static void processLogup(String input){
		if (input.startsWith("OK")) {
			JOptionPane.showMessageDialog(null, "Cuenta creada con exito", "Nuevo usuario", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(null, "No se puede registrar.", "Nuevo usuario", JOptionPane.ERROR_MESSAGE);	
		LogUpDialog frame = new LogUpDialog();
		frame.setVisible(true);
	}
	
	private static void processMap(String input){
		if (input.startsWith("OK")) {
			input = input.substring(3);
			GameFrame frame = GameFrame.getInstance();
			frame.setVisible(true);
			frame.setMap(Parser.parseMap(input));
		}
	}
	
	public static void processStats(String input){
		if (input.startsWith("OK")) {
			Object[][] data = Parser.parseStats(input.substring(3));
			StatsFrame frame = new StatsFrame();
			frame.load(data);
			frame.setVisible(true);
			return;
		}
		JOptionPane.showMessageDialog(null, "No se pudieron obtener las estadísticas", "Estadísticas",
				JOptionPane.ERROR_MESSAGE);
		LogInFrame frame = new LogInFrame();
		frame.setVisible(true);
	}
	
}
