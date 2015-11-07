package client.conn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import client.config.Config;

public class Connection {

	private static Connection INSTANCE = null;
	private static String STATUS = null;
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;

	private Connection(){
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(Config.get("ip"));
		} catch (UnknownHostException e1) {
			
			
			JOptionPane.showMessageDialog(null, 
					"No se puede obtener la direccion de IP.", 
					"Error de conexion", JOptionPane.ERROR_MESSAGE);
		}
		
		int port = Integer.parseInt(Config.get("port"));

		try{
			socket = new Socket(ip, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			createListener();
			System.out.println("Cargado " + ip + ":" + port);
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, 
					"No se puede establecer la conexion con " + ip + ":" + port, 
					"Error de conexion", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	public static Connection getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Connection();
		}
		return INSTANCE;
	}

	public void send(String message) {
		out.println(message);
	}

	private void createListener() {
		Thread t = new ServerListener(this.in);
		t.start();
	}

	public String getStatus(){
		return STATUS;
	}
	
	public void setStatus(String change){
		STATUS = change;
	}
	
}
