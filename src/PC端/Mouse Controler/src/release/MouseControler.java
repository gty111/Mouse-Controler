package release;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.awt.*;
import java.awt.event.InputEvent;

public class MouseControler {
	static double x,y;
	static Robot robot;
	static int port;
	static int height;
	static int width;
	static int verbose;
	static int sensibility;
	static boolean ifon;
	
	public MouseControler() throws AWTException, IOException {
		robot = new Robot();
		Properties properties = new Properties();
		BufferedReader bufferedReader = new BufferedReader(new FileReader("config.properties"));
		properties.load(bufferedReader);
		height = Integer.parseInt(properties.getProperty("height"));
		width = Integer.parseInt(properties.getProperty("width"));
		port = Integer.parseInt(properties.getProperty("port"));
		verbose = Integer.parseInt(properties.getProperty("verbose"));
		sensibility = Integer.parseInt(properties.getProperty("sensibility"));
		ifon = Boolean.parseBoolean(properties.getProperty("ifon"));
		System.out.println("height:"+String.valueOf(height));
		System.out.println("width:"+String.valueOf(width));
		System.out.println("verbose:"+String.valueOf(verbose));
		System.out.println("sensibility:"+String.valueOf(sensibility));
		System.out.println("ifon:"+String.valueOf(ifon));
	}

	public static void syn() {
		Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
		x = point.getX();
		y = point.getY();
	}
	
	public static void main(String[] args) throws Exception {
		new MouseControler();
		ServerSocket listenSocket = new ServerSocket(port);
		System.out.println("server start at port:" + port);
		while(true) {
			try {
				Socket clientSocket = listenSocket.accept();
				clientSocket.setTcpNoDelay(ifon);
				System.out.println("connected to client");
				InputStream inStream = clientSocket.getInputStream();
				OutputStream outStream = clientSocket.getOutputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
				double dx,dy;
				while(true) {
					dx = Double.parseDouble(in.readLine());
					dy = Double.parseDouble(in.readLine());
					outStream.write(1);
					outStream.flush();
					
					if(dx==100000&&dy==100000) {
						robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						System.out.println("press");
						continue;
					}else if(dx==100001&&dy==100001) {
						robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						System.out.println("release");
						continue;
					}else if(dx==100002&&dy==100002) {
						robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						System.out.println("left click");
						continue;
					}else if(dx==100003&&dy==100003) {
						robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
						robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
						System.out.println("right click");
						continue;
					}else if(dx==100004&&dy==100004) {
						robot.mouseWheel(sensibility);
						System.out.println("roll downward");
						continue;
					}else if(dx==100005&&dy==100005) {
						robot.mouseWheel(-sensibility);
						System.out.println("roll upward");
						continue;
					}
					
					syn();
					x += dx;
					y += dy;
					x = Math.max(x, 0);
					y = Math.max(y, 0);
					x = Math.min(x, width);
					y = Math.min(y, height);
					if(verbose>=1)
						System.out.println(x+" "+y);
					robot.mouseMove((int)Math.round(x), (int)Math.round(y));
				}
			}catch(Exception e) {
				System.out.println("client disconnected");
			}
			
		}
	}
}
