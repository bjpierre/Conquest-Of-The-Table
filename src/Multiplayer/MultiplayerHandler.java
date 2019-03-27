package Multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 
 * @author Ben Pierre
 */
public class MultiplayerHandler {
	InetAddress ip;
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	public MultiplayerHandler(String Address, int port) throws IOException {
        ip = InetAddress.getByName(Address);
        socket = new Socket(ip, port); 
	}
	
	public MultiplayerHandler() throws IOException {
		ip = InetAddress.getByName("localhost");
		socket = new Socket(ip, 4444); 
	}
	
	public boolean run() {
		try {
	        dis = new DataInputStream(socket.getInputStream()); 
	        dos = new DataOutputStream(socket.getOutputStream()); 
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		Thread readMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
            	//wait for server input then send to console
                while (true) { 
                    try { 
                        String msg = dis.readUTF(); 
                        System.out.print(msg+"\n>"); 
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
	
	public boolean sendMessage(String message) {
		try {
			dos.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
}
