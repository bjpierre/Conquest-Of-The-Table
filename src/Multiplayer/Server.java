package Multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;



/**
 * Handles multiplayer interactions
 * @author Ben Pierre
 *
 */
public class Server {
	static Vector<Player> PlayerList = new Vector<>();
	static final int port = 5656;
	static final String address = "LocalHost";
	static int id = 0;

	public static void main(String[] args) throws IOException  
    { 
        @SuppressWarnings("resource")
		ServerSocket ss = new ServerSocket(port); 
          
        Socket socket; 
        System.out.println("Game Server Hosted on "+address+ ":" + port);
        while (true)  
        {  
        	//Connect socket, create IO and add user to list. Create and start a thread for the user
            socket = ss.accept(); 
            System.out.println("New client request received : " + socket.toString()); 
            DataInputStream dis = new DataInputStream(socket.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); 
            Player joining = new Player(socket, id++, dis, dos); 
            Thread t = new Thread(joining); 
            dos.writeUTF("MKUSR-" + id);
            PlayerList.add(joining); 

            t.start(); 
  
        }
    } 
    /**
     * Sends command to all connected clients
     * @param message String to be broadcast, should be in form name:message
     * @param s socket of origin to prevent sender receiving
     */
    static void broadcast(String message, Socket s) {
    	for (Player user : Server.PlayerList)  
        { 
                try {
                	if(!user.s.equals(s)) user.dos.writeUTF(message);
				} catch (Exception e) {
					e.printStackTrace();
				} 
             
        } 
    }
    
    /**
     * Removes a player from the game
     * @param id player to be removed
     */
    public static void removeUser(int id) {
		Player ply = PlayerList.get(id);
		if (ply.getID() == id) {
			try {
				Server.id--;
				ply.stop();
				ply.dis.close();
				ply.dos.close();
				ply.s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			PlayerList.remove(ply);
		}

	}
    
    /**
     * Gets number of players currently connected to the server
     * @return Number of players tracked
     */
    public static int getPlayerCount() {
    	return PlayerList.size();
    }
	
	
	
	
}

class Player implements Runnable {
	// create data streams and names
	final DataInputStream dis;
	final DataOutputStream dos;
	int id;
	Socket s;
	Boolean stopped;

	/**
	 * Creates a player handler
	 * @param s Socket the client connects from
	 * @param id ID of the player
	 * @param dis Input Stream
	 * @param dos Output Stream
	 */
	public Player(Socket s, int id, DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.id = id;
		this.s = s;
		stopped = false;
	}

	@Override
	public void run() {

		String received;
		while (!stopped) {
			try {
				// TODO Handle user events
				received = dis.readUTF();
				System.out.println(received);
				if(received.startsWith("Terminate")) {
					Server.removeUser(id);
				}else if(received.startsWith("ReqPlayCount")) {
					dos.writeUTF("PlayerCount: " + Server.getPlayerCount());
				}else if(received.startsWith("CrX")){
					//System.out.println("Creating x at " + received.substring(6,8) + "," + received.substring(9,11));
					Server.broadcast(received, s);
				}else if(received.startsWith("Restart")) {
					//System.out.println("Restarting");
					Server.broadcast(received, s);
				}else {Server.broadcast(received, s);System.out.println("toggling turn");};
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	/**
	 * Stops the server
	 */
	public void stop() {
		stopped = true;
	}
	/**
	 * Gets the id of this player
	 * @return The Id of the player
	 */
	public int getID() {
		return id;
	}
}
