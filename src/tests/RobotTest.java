package tests;
import UI.*;
import java.awt.Robot;
import java.awt.Window;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class RobotTest implements MouseListener, KeyListener{
	public Robot robot;
	public Window window;
	public GraphicsDevice device;
	public Frame frame;
	
	public RobotTest() throws AWTException {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();
		//DisplayMode dm=new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		frame = new Frame("School of Thought");
		//frame.setUndecorated(true);
		frame.setResizable(false);
		//device.setFullScreenWindow(frame);
		/*
		if (device.isDisplayChangeSupported()) {
			try {
				device.setDisplayMode(dm);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
		*/
		robot = new Robot();
		window = device.getFullScreenWindow();
	}
	
	public void logIn(String username, String password) {
		if (robot != null) {
			Point a = frame.login.getComponent(1).getLocationOnScreen(); // Username Field Location
			Point b = frame.login.getComponent(3).getLocationOnScreen(); // Password Field Location
			Point c = frame.login.getComponent(4).getLocationOnScreen(); // Submit Button Location
			
			System.out.println(a.getX() + " " + a.getY());
			System.out.println(b.getX() + " " + b.getY());
			System.out.println(c.getX() + " " + c.getY());
			
//			moveAndClickOnPoint(a);
			moveAndClickOnPoint(new Point(463, 445));
			typeInEntireString(username);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			moveAndClickOnPoint(b);
			moveAndClickOnPoint(new Point(463, 465));
			typeInEntireString(password);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			moveAndClickOnPoint(new Point(463, 485));
		}
		
	}
	
	private void typeInEntireString(String string) {
		string = string.toUpperCase();
		for (int i = 0; i < string.length(); i++) {
			robot.keyPress(string.charAt(i));
			robot.keyRelease(string.charAt(i));
			System.out.print((int) string.charAt(i));
		}
	}
	
	private void moveAndClickOnPoint(Point p) {
		robot.mouseMove(p.x, p.y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	public static void main(String[] args) {
		try {
			RobotTest test = new RobotTest();
			test.logIn("heLlo", "world");
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getX() + " " + arg0.getY());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
