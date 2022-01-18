package test;

import java.io.*;
import java.net.Socket;

public class Client {
	public static void main(String[] args) throws Exception{
		Socket socket = new Socket("192.168.1.105", 7178);
	}
	
}
