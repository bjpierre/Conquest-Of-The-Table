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

	/**
	 * Creates and handles a connection to a multiplayer server
	 * @param Address Server address
	 * @param port Port Game is hosted on
	 * @param board Reference to the board
	 * @throws IOException if connection fails
	 */
	public MultiplayerHandler(String Address, int port, Square[][] board) throws IOException {
			ip = InetAddress.getByName(Address);
			socket = new Socket(ip, port);
		this.board = board;
		run();
	}

	/**
	 * Creates and handles a connection to a multiplayer server with basic functionality
	 * @throws IOException if connection fails
	 */
	public MultiplayerHandler() throws IOException {
			ip = InetAddress.getByName("localhost");
			socket = new Socket(ip, 4444);
		run();
	}

	/**
	 * Sets up streams
	 * @return False if connection fails
	 */
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
						} else if(msg.startsWith("Restart")) { 
							restart();
						}else{
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
	
	/**
	 * Sets an x on a board when command received
	 * @param message Message from other client, includes where to place x
	 * @return false if anything fails
	 */
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
	
	/**
	 * Requests player count
	 * @return nothing at the moment
	 * @throws IOException
	 */
	public int getPlayerCount() throws IOException {
		dos.writeUTF("ReqPlayCount");
		return 0;
	}
	
	/**
	 * Leaves server
	 * @return False if any issues occur
	 */
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

	private boolean sendMessage(String message) {
		try {
			dos.writeUTF(message);
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Unable to send to server");
			return false;
		}
		return true;
	}

	/**
	 * Sends signal to server 
	 * @param x x Location to create an x
	 * @param y y Location to create an x
	 * @return False if anything goes wrong
	 */
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

	/**
	 * Example of how a method will look when the game is actually implemented
	 * @param UnitId Id of the unit to move
	 * @param x x Location to create an x
	 * @param y y Location to create an x
	 * @return False if anything goes wrong
	 */
	public boolean moveUnit(int UnitId, int x, int y) {
		try {
			sendMessage("MVU-" + id + "-" + x + "-" + y + "..");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Tell all clients to restart
	 * @return False if anything goes wrong
	 */
	public boolean sendRestart() {
		System.out.println("Sending restart");
		try {
			sendMessage("Restart..");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Sets the board to empty
	 * @return False if anything goes wrong
	 */
	private boolean restart() {
		System.out.println("Restarting");
		try {
			int row, column;

			for (row = 0; row < 10; row++) {
				for (column = 0; column < 15; column++) {
					board[row][column].setState();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
