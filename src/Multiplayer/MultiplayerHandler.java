package Multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import GameBoard.Board;
import GameBoard.Board.Square;

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
	Square[][] board;
	Thread readMessage;

	public MultiplayerHandler(String Address, int port, Square[][] board) throws IOException {
			ip = InetAddress.getByName(Address);
			socket = new Socket(ip, port);
		this.board = board;
		run();
	}

	public MultiplayerHandler() throws IOException {
			ip = InetAddress.getByName("localhost");
			socket = new Socket(ip, 4444);
		run();
	}

	public boolean run() {
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to communicate with server");
			return false;
		}

		readMessage = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// wait for server input then send to console
				while (true) {
					try {
						String msg = dis.readUTF();
						if (msg.startsWith("MKUSR-")) {
							id = Integer.parseInt(msg.substring(6));
							System.out.println("Assigned UID: " + id );
						}else if(msg.startsWith("CrX-")){
							setGridx(msg);
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
	
	public boolean setGridx(String message) {
		try {
			int x = Integer.parseInt(message.substring(6,8));
			int y = Integer.parseInt(message.substring(9,11));
			board[x][y].mouseEvent();
		}catch(Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to create an x");
			return false;
		}
		
		
		return true;
	}
	
	public int getPlayerCount() throws IOException {
		dos.writeUTF("ReqPlayCount");
		return 0;
	}
	
	@SuppressWarnings("deprecation")
	public boolean leaveServer(){
		try {
			dos.writeUTF("Terminate..");
			System.out.println("Left server");
			dis.close();
			dos.close();
			socket.close();
			readMessage.stop();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Error leaving server");
			return false;
		}
		return true;
	}

	public boolean sendMessage(String message) {
		try {
			dos.writeUTF(message);
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to send to server");
			return false;
		}
		return true;
	}

	public boolean createX(int x, int y) {
		try {
			System.out.println("" + x + "," + y);
			sendMessage("CrX-" + id + "-" + String.format("%02d", x) + "," + String.format("%02d", y) + "..");
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
	
	public boolean restart() {
		try {
			sendMessage("Restart..");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
