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
		// 使用InPutStream流读取properties文件
		BufferedReader bufferedReader = new BufferedReader(new FileReader("config.properties"));
		properties.load(bufferedReader);
		// 获取key对应的value值
		System.out.println(properties.getProperty("height"));
	}
	
	
}
