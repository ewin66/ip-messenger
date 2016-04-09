package ip;
import java.io.*;
import java.net.*;
import javax.swing.*;
class ServerFile implements Runnable {
        Socket conn;
        public ServerFile(Socket a)
        {
            conn = a;
        }
	public void run() {
		File file1;
            	String clientsen="";
                String[] options = {"Save Files", "Cancel"};
                JFileChooser fc1 = new JFileChooser();
		fc1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    try
                    {   
                        //while(true)
                        	System.out.println("reached2");
                                DataOutputStream outtoclient = new DataOutputStream(conn.getOutputStream());
                                BufferedReader infromclient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                
                                System.out.println("reached3");
                                clientsen = infromclient.readLine();
                                System.out.println(clientsen);
                                System.out.println("reached4");
                                  System.out.println("reached4");
                                  int index=clientsen.length()-1;
                                  long len=0;
                                  int dummy=0;
                                  System.out.println("\nindex = " + clientsen.length() + "\n");
                                  outtoclient.writeBytes("Over1\n");
                                  dummy=0;
                                  while(index>=0)
                                  {   len += ((int)clientsen.charAt(dummy)-48)*Math.pow(10, index);
                                      dummy++;
                                      index--;
                                  }
                                  index=0;
                                  String s ="";
                                  while(index<len)
                                  {
                                      s += (char)infromclient.read();
                                      index++;
                                  }
                                  outtoclient.writeBytes("Over2\n");
                                  clientsen = infromclient.readLine();
                                    if (clientsen.equals("FiLe"))
                                    {
                                            int n = JOptionPane.showOptionDialog(null,s,"Message Received",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.PLAIN_MESSAGE,
                                            null,
                                            options,
                                            options[0]);
                                            System.out.println(n);
                                            
                                            do{
                                            if (n==0)
                                            {
                                                  int returnVal1 = fc1.showOpenDialog(null);
                                                  if (returnVal1 == JFileChooser.APPROVE_OPTION)
                                                  {
                                                        file1 = fc1.getSelectedFile();
                                                        copy(outtoclient,infromclient,file1.getAbsolutePath());
                                                  }
                                                  else
                                                  {
                                                        System.out.println("Open command cancelled by user.\n");
                                                        break;
                                                  }

                                            }
                                            }while(n==-1);
                                    }
                                    else if (clientsen.equals("No FiLe"))
                                    {
                                        System.out.println("reached4");
                                        JOptionPane.showMessageDialog(null, s);   // without attach
                                        System.out.println("reached4");
                                    }

                                
                                System.out.println("reached5");
                                System.out.println(clientsen);
                                System.out.println("reached7");
                        }

                    
                    catch(Exception e0)
                    {
                         System.out.println("ServerFile   "+String.valueOf(e0)+"\n"+e0.getCause());

                    }




}//end main
                    public void copy(DataOutputStream outtoclient,BufferedReader infromclient,String basepath)
                    {
                        try
                        {
                            int totfiles = infromclient.read();
                                                        int tot=0;
                                                        FileOutputStream toFile;
                                                        String path = null,chk;
                                                        System.out.println("total files = "+totfiles);
                                                        while(tot<totfiles)
                                                        {    chk = infromclient.readLine();
                                                             if(chk.equals("file"))
                                                             {          
                                                                        System.out.println("server file 1");
                                                                        outtoclient.writeBytes("send\n");
                                                                        System.out.println("server file 2");
                                                                        path = infromclient.readLine();
                                                                        System.out.println("server file 3");
                                                                        path = basepath+"/"+path;
                                                                        System.out.println("server file 4");
                                                                        System.out.println("Receiving file  = "+ path);
                                                                        System.out.println("server file 5");
                                                                        toFile = new FileOutputStream(path);
                                                                        System.out.println("server file 6");
                                                                        int r=0;
                                                                        System.out.println("server file 7");
                                                                        while( r != 65533)
                                                                        {   r = infromclient.read();
                                                                            if(r!=65533)
                                                                                toFile.write(r);
                                                                        }
                                                                        System.out.println("server file 8");
                                                                        toFile.close();
                                                                        System.out.println("server file 9");
                                                             }
                                                             else if(chk.equals("folder"))
                                                             {
                                                                 System.out.println("server folder 4");
                                                                 String name = infromclient.readLine();
                                                                 System.out.println("server folder 5");
                                                                 name = basepath+"/"+name;
                                                                 System.out.println("server folder 6");
                                                                 new File(name).mkdir();
                                                                 System.out.println("server folder 7");
                                                                 copy(outtoclient,infromclient,name);
                                                                 System.out.println("server folder 8");
                                                             }
                                                             tot++;
                                                        }
                        }
                        catch(Exception e)
                        {
                            System.out.println(e.toString());
                        }
                    }
}//end class