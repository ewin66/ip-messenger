package ip;
import java.io.*;
import java.net.*;
public class ClientFile implements Runnable{
                    String ta,tb,t;
                    public ClientFile(String a,String b,String c)
                    {
                        ta = a;
                        tb = b;
                        t = c;
                    }
                    public void run() {
		    try
                    {   Socket clientsocket = new Socket(t,6789);
			System.out.println("send1");
                        String msg = ta;
                        String filename = tb;
                        System.out.println("send5");
                        //BufferedReader in = new BufferedReader( new InputStreamReader(clientsocket.getInputStream(), "8859_1") );
                        DataOutputStream outtoserver = new DataOutputStream(clientsocket.getOutputStream());
                        BufferedReader infromserver = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
                        System.out.println("send6");
                        System.out.println("filename = " + filename);
                        if(filename.equals(""))
                            msg = "Message : \n" + msg;
                        else
                            msg = "Message : \n" + msg + "\n\n\n\nFiles Received : \n" + filename;
                        int index = 0;

                        outtoserver.writeBytes(msg.length()+"\n");
                        System.out.println("send length = "+msg.length());
                        String local = infromserver.readLine();
                        if (local.equals("Over1"))
                        {
                            while (index < msg.length())
                            {
                                outtoserver.write((int)msg.charAt(index));
                                index++;
                            }
                            local = infromserver.readLine();
                            
                            if(local.equals("Over2") && !filename.equals(""))
                            {   outtoserver.writeBytes("FiLe\n");
                                String[] files = filename.split("\n");
                                copy(outtoserver,infromserver, files);
                                
                             }
                            else if(local.equals("Over2") && filename.equals(""))
                                outtoserver.writeBytes("No FiLe\n");
                        }
			System.out.println("send7");
                        clientsocket.close();
                        System.out.println("send8");
                    }
                    catch(Exception e2)
                    {
                            System.out.println("ClientFile   "+String.valueOf(e2) + "\n");
                    }
    }
    public void copy(DataOutputStream outtoserver,BufferedReader infromserver,String[] files)
    {           try
                {
                    
                    int totalfiles=0;
                    int r=0;
                    FileInputStream fromFile = null;
                    File oldfile;
                    outtoserver.write(files.length);
                    String chk;
                    while(totalfiles<files.length)
                    {

                        oldfile = new File(files[totalfiles]);
                        if(oldfile.isDirectory())
                        {   
                            System.out.println("client folder 4");
                            outtoserver.writeBytes("folder\n");
                            System.out.println("client folder 5");
                            File folder1[] = oldfile.listFiles();
                            String[] folder = new String[folder1.length];
                            int count=0;
                            for(File name : folder1)
                            {
                                folder[count] = name + "";
                                System.out.println(folder[count]);
                                count++;
                            }
                            System.out.println("client folder 6");
                            outtoserver.writeBytes(oldfile.getName()+"\n");
                            System.out.println("client folder 7");
                            copy(outtoserver,infromserver,folder);
                            System.out.println("client folder 8");

                        }
                        else if(oldfile.isFile())
                        {   outtoserver.writeBytes("file\n");
                            fromFile =  new FileInputStream(oldfile);
                            System.out.println("client file 4");
                            chk = infromserver.readLine();
                            if(chk.equals("send"))
                            {   System.out.println("client  file 5");
                                outtoserver.writeBytes(oldfile.getName()+"\n");
                                System.out.println("client  file 6");
                                r=0;
                                while( r != -1)
                                {   r = fromFile.read();
                                    outtoserver.write(r);
                                }
                                System.out.println("client  file 7");
                            }
                            totalfiles++;
                            System.out.println("client  file 8");
                        }
                    }
                }
                catch(Exception e)
                {
                    System.out.println(e.toString());
                }
    }
}
