package test;

import java.awt.Point;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

public class Test1 {
	public static void main(String[] args) throws Exception {
		/*
		Robot robot = new Robot();
		Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
		robot.mouseMove(point.x+100,point.y+100);*/
		
		Properties properties = new Properties();
		// ʹ��InPutStream����ȡproperties�ļ�
		BufferedReader bufferedReader = new BufferedReader(new FileReader("config.properties"));
		properties.load(bufferedReader);
		// ��ȡkey��Ӧ��valueֵ
		System.out.println(properties.getProperty("height"));
	}
	
	
}
