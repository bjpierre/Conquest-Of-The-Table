package Multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import GameBoard.Board;

/**
 * 
 * @author Ben Pierre
 */
public class MultiplayerHandler {
	InetAddress ip;
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	int id;
	Board board;

	public MultiplayerHandler(String Address, int port,Board board) throws IOException {
		ip = InetAddress.getByName(Address);
		socket = new Socket(ip, port);
		this.board = board;
	}

	public MultiplayerHandler() throws IOException {
		ip = InetAddress.getByName("localhost");
		socket = new Socket(ip, 4444);
	}

	public boolean run() {
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		Thread readMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				// wait for server input then send to console
				while (true) {
					try {
						String msg = dis.readUTF();
						if (msg.startsWith("MKUSR-")) {
							id = Integer.parseInt(msg.substring(6));
						} else {
							System.out.print(msg + "\n>");
						}
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}

		});

		readMessage.start();
		return true;
	}

	public int getPlayerCount() throws IOException {
		dos.writeUTF("ReqPlayCount");

		return 0;
	}
	
	public boolean leaveServer(){
		try {
			dos.writeUTF("Terminate..");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean sendMessage(String message) {
		try {
			dos.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean createX(int x, int y) {
		try {
			sendMessage("CrX-" + id + "-" + x + "-" + y + "..");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean moveUnit(int UnitId, int x, int y) {
		try {
			sendMessage("MVU-" + id + "-" + x + "-" + y + "..");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
