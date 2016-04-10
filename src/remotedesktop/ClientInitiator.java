package remotedesktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class ClientInitiator
{
	Socket socket = null;

	public static void main(String[] args)
	{
		String ip = JOptionPane.showInputDialog("Please enter server IP");
		String port = JOptionPane.showInputDialog("Please enter server port");
		new ClientInitiator().initialize(ip,Integer.parseInt(port));
	}

	public void initialize(String ip,int port )
	{
		Robot robot = null;
		Rectangle rectangle = null;
		try
		{
			System.out.println("Connecting to server...");
			socket = new Socket(ip, port);
			System.out.println("Connection Established.");

			GraphicsEnvironment
			gEnv=GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gDev=gEnv.getDefaultScreenDevice();

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

			rectangle = new Rectangle(dim);

			robot = new Robot(gDev);

			drawGUI();
            	

			new ScreenSpyer(socket,robot,rectangle);
		//	new ServerDelegate(socket,robot);
		}
		catch(UnknownHostException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		catch(AWTException ex)
		{
			ex.printStackTrace();
		}
	}

	private void drawGUI()
	{
		JFrame frame = new JFrame("Remote Admin");
		JButton button= new JButton("Terminate");

		frame.setBounds(100,100,150,150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(button);
		button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.exit(0);
				}
			});
		frame.setVisible(true);
	}

	class ScreenSpyer extends Thread
	{

		Socket socket = null;
		Robot robot = null;
		Rectangle rectangle = null;
		boolean continueLoop = true;

		public ScreenSpyer(Socket socket, Robot robot,Rectangle rect)
		{
			this.socket = socket;
			this.robot = robot;
			rectangle = rect;
			start();
		}

		public void run()
		{
			ObjectOutputStream oos = null;

			try
			{
				oos = new ObjectOutputStream(socket.getOutputStream());

				oos.writeObject(rectangle);
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}

			while(continueLoop)
			{
				BufferedImage image = robot.createScreenCapture(rectangle);

				ImageIcon imageIcon = new ImageIcon(image);

				try
				{
				//	System.out.println("before sending image");
					oos.writeObject(imageIcon);
					oos.reset();
				//	System.out.println("New screenshot sent");
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}

				try
				{
					Thread.sleep(0);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	class ServerDelegate extends Thread 
	{

			   Socket socket = null;
          	   		   Robot robot = null;
   	  		   boolean continueLoop = true;

			public ServerDelegate(Socket socket, Robot robot) 
	    		{
	     	   	this.socket = socket;
        			this.robot = robot;
        			start();
    			 }

			public void run()
	   		{
     	    		Scanner scanner = null;
        			try {
            
            			                 System.out.println("Preparing InputStream");
   		          	                  scanner = new Scanner(socket.getInputStream());
	
			            while(continueLoop)
			            {
                	
                 				System.out.println("Waiting for command");
                 				int command = scanner.nextInt();
                				System.out.println("New command: " + command);
                				switch(command)
				{
                    			case -1:
                    	    		robot.mousePress(scanner.nextInt());
                    			break;
                  				case -2:
                        			robot.mouseRelease(scanner.nextInt());
                    			break;
                    			case -3:
                 		       		robot.keyPress(scanner.nextInt());
                    			break;
                    			case -4:
                        			robot.keyRelease(scanner.nextInt());
                    			break;
                    			case -5:
                        			robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                    			break;
                			         	}
	                        	           }
		                      }

		     	     catch (IOException ex) 
		 	     {
           				 ex.printStackTrace();
        		   	    }
	   	        }	

	}

}