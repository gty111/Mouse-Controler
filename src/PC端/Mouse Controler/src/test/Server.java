package test;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	public static void main(String[] args) throws Exception {
		ServerSocket listenSocket = new ServerSocket(7179);
		while(true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Accepted connection from client");
		}
		//listenSocket.close();
	}
}
