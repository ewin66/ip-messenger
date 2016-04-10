package remotedesktop;

import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.beans.PropertyVetoException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintWriter;

public class ServerInitiator
{
	private JFrame frame = new JFrame();
	private JDesktopPane desktop = new JDesktopPane();

	public static void main(String args[])
	{
		String port = JOptionPane.showInputDialog("Please enter listening port");
		new ServerInitiator().initialize(Integer.parseInt(port));
	}

	public void initialize(int port)
	{
		try
		{
			ServerSocket sc = new ServerSocket(port);

			drawGUI();

			while(true)
			{
				Socket client = sc.accept();
				System.out.println("New client Connected to the server");

				new ClientHandler(client,desktop);
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void drawGUI()
	{
		frame.getContentPane().add(desktop,BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	class ClientHandler extends Thread
	{
		private JDesktopPane desktop = null;
		private Socket cSocket = null;
		private JInternalFrame interFrame = new JInternalFrame("Client Screen",true,true,true);
		private JPanel cPanel = new JPanel();

		public ClientHandler(Socket cSocket, JDesktopPane desktop)
		{
			this.cSocket = cSocket;
			this.desktop = desktop;
			start();
		}

		public void run()
		{
			Rectangle clientScreenDim = null;
			ObjectInputStream ois = null;

			drawGUI();

			try
			{
				ois = new ObjectInputStream(cSocket.getInputStream());
				clientScreenDim =(Rectangle) ois.readObject();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			catch(ClassNotFoundException ex)
			{
					ex.printStackTrace();
			}

			new ClientScreenReciever(ois,cPanel);
		//	new ClientCommandsSender(cSocket,cPanel,clientScreenDim);
		}

		public void drawGUI()
		{
			interFrame.getContentPane().setLayout(new BorderLayout());
			interFrame.getContentPane().add(cPanel,BorderLayout.CENTER);
			interFrame.setSize(100,100);
			desktop.add(interFrame);

			try
			{
				interFrame.setMaximum(true);
			}
			catch (PropertyVetoException ex)
			{
				ex.printStackTrace();
			}

			cPanel.setFocusable(true);
			interFrame.setVisible(true);
		}

		class ClientScreenReciever extends Thread
		{
			private ObjectInputStream cObjectInputStream = null;
			private JPanel cPanel = null;
			private boolean continueLoop = true;

			public ClientScreenReciever(ObjectInputStream ois,JPanel p)
			{
				this.cObjectInputStream = ois;
				this.cPanel = p;
				start();
			}

			public void run()
			{
				try
				{
					while(continueLoop)
					{
						ImageIcon imageIcon = (ImageIcon) cObjectInputStream.readObject();
					//	System.out.println("New image recieved");
						Image image = imageIcon.getImage();
						image = image.getScaledInstance(cPanel.getWidth(),cPanel.getHeight(),Image.SCALE_FAST);
						Graphics graphics = cPanel.getGraphics();
						graphics.drawImage(image,0,0,cPanel.getWidth(),cPanel.getHeight(),cPanel);
					}
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
				catch(ClassNotFoundException ex)
				{
					ex.printStackTrace();
				}
			}
		}

		class ClientCommandsSender implements KeyListener,MouseMotionListener,MouseListener
		{
			private Socket cSocket = null;
			private JPanel cPanel = null;
			private PrintWriter writer = null;
			private Rectangle clientScreenDim = null;

			ClientCommandsSender(Socket s, JPanel p, Rectangle r)
			{
				this.cSocket = s;
				this.cPanel = p;
				this.clientScreenDim = r;
				cPanel.addKeyListener(this);
				cPanel.addMouseListener(this);
				cPanel.addMouseMotionListener(this);

				try
				{
					writer = new PrintWriter(cSocket.getOutputStream());
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
			 
			public void mouseDragged(MouseEvent e)
			{
    			}

			 public void mouseClicked(MouseEvent e) 
			{
    			}

			public void mouseEntered(MouseEvent e) 
			{
    			}

			public void mouseExited(MouseEvent e) 
			{
			}

			public void keyTyped(KeyEvent e) 
			{
    			}

			public void keyPressed(KeyEvent e)
			{
				//System.out.println("Key Pressed");
				writer.println(EnumCommands.PRESS_KEY.getAbbrev());
				writer.println(e.getKeyCode());
				writer.flush();
			}

			public void keyReleased(KeyEvent e)
			{
				//System.out.println("Mouse Released");
				writer.println(EnumCommands.RELEASE_KEY.getAbbrev());
				writer.println(e.getKeyCode());
				writer.flush();
			}

			public void mouseMoved(MouseEvent e)
			{
				double xScale = clientScreenDim.getWidth()/cPanel.getWidth();
			//	System.out.println("xScale: " + xScale);
				double yScale = clientScreenDim.getHeight()/cPanel.getHeight();
			//	System.out.println("yScale: " + yScale);
			//	System.out.println("Mouse Moved");
				writer.println(EnumCommands.MOVE_MOUSE.getAbbrev());
				writer.println((int)(e.getX() * xScale));
				writer.println((int)(e.getY() * yScale));
				writer.flush();
			}

			public void mousePressed(MouseEvent e)
			{
			//	System.out.println("Mouse Pressed");
				writer.println(EnumCommands.PRESS_MOUSE.getAbbrev());
				int button = e.getButton();
				int xButton = 16;
				if (button == 3)
				{
					xButton = 4;
				}
				writer.println(xButton);
				writer.flush();
			}

			public void mouseReleased(MouseEvent e)
			{
			//	System.out.println("Mouse Released");
				writer.println(EnumCommands.RELEASE_MOUSE.getAbbrev());
				int button = e.getButton();
				int xButton = 16;
				if (button == 3)
				{
					xButton = 4;
				}
				writer.println(xButton);
				writer.flush();
			}
		}
	}
}

enum EnumCommands
{
	PRESS_MOUSE(-1),
	RELEASE_MOUSE(-2),
	PRESS_KEY(-3),
	RELEASE_KEY(-4),
	MOVE_MOUSE(-5);

	private int abbrev;

	EnumCommands(int abbrev)
	{
		this.abbrev = abbrev;
	}

	public int getAbbrev()
	{
		return abbrev;
	}
}