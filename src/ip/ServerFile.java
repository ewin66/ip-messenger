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
            System.out.println("reached3");
            InputStream in = conn.getInputStream();
            OutputStream os = conn.getOutputStream();
            String s = ByteStream.toString(in);
            System.out.println("reached3.1");
            ByteStream.toStream(os,"Over2");
            System.out.println("reached3.2");
            clientsen = ByteStream.toString(in);
            System.out.println("reached3.3");
            if (clientsen.equals("FiLe"))
            {   System.out.println("reached3.4");
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
                            copy(file1.getAbsolutePath());
                            ByteStream.toStream(os,"Over3");
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
    public void copy(String basepath)
    {
        try
        {
             InputStream in = conn.getInputStream();
             OutputStream os = conn.getOutputStream();
             int totfiles = ByteStream.toInt(in);
//             int totfiles = infromclient.read();
             int tot=0;
             File file;
//             FileOutputStream toFile1;
//             BufferedOutputStream toFile;
             String path = null,chk;
             System.out.println("total files = "+totfiles);
             while(tot<totfiles)
             {
                 chk = ByteStream.toString(in);
                 if(chk.equals("file"))
                 {
                    System.out.println("server file 1");
                    ByteStream.toStream(os,"send");
                    System.out.println("server file 2");
                    path = ByteStream.toString(in);
                    System.out.println("server file 3");
                    path = basepath+File.separator+path;
                    System.out.println("server file 4");
                    System.out.println("Receiving file  = "+ path);
                    System.out.println("server file 5");
//                    toFile1 = new FileOutputStream(path);
//                    toFile= new BufferedOutputStream(toFile1);
                    System.out.println("server file 6");
//                    int r=0;
//                    String r1 = new String("q");
                    System.out.println("server file 7");


                    file=new File(path);
                    System.out.println("server file 7.1");
                    ByteStream.toFile(in, file);
//                    fr.main(conn, basepath+File.separator);
                    System.out.println("server file 8");
                    System.out.println("server file 9");
                 }
                 else if(chk.equals("folder"))
                 {
                     System.out.println("server folder 4");
                     String name = ByteStream.toString(in);
                     System.out.println("server folder 5");
                     name = basepath+File.separator+name;
                     System.out.println("server folder 6");
                     new File(name).mkdir();
                     System.out.println("server folder 7");
//                     fr.main(conn, name+File.separator);
                     copy(name);
                     System.out.println("server folder 8");
                 }
                 tot++;
             }
        }
        catch(Exception e)
        {
            System.out.println("Server file:    "+e.toString());
        }
    }
      private static void toFile(InputStream ins, FileOutputStream fos, int len, int buf_size) throws
        java.io.FileNotFoundException,
        java.io.IOException {

    byte[] buffer = new byte[buf_size];

    int       len_read=0;
    int total_len_read=0;
    System.out.println("The Received length is :   "+len);
    while ( total_len_read + buf_size <= len) {
      len_read = ins.read(buffer);
      total_len_read += len_read;
      fos.write(buffer, 0, len_read);
    }

    if (total_len_read < len) {
      toFile(ins, fos, len-total_len_read, buf_size/2);
    }
  }

  private static void toFile(InputStream ins, File file, int len) throws
        java.io.FileNotFoundException,
        java.io.IOException {

    FileOutputStream fos=new FileOutputStream(file);

    toFile(ins, fos, len, 1024);
  }

  public static void toFile(InputStream ins, File file) throws
        java.io.FileNotFoundException,
        java.io.IOException {

    int len = toInt(ins);
    toFile(ins, file, len);
  }

    public static int toInt(byte[] byte_array_4) {
    int ret = 0;
    for (int i=0; i<4; i++) {
      int b = (int) byte_array_4[i];
      if (i<3 && b<0) {
        b=256+b;
      }
      ret += b << (i*8);
    }
    return ret;
  }

//alternative   ---->>>    int a = ByteBuffer.wrap(new byte[] {0,0,0,-1}).getInt()


  public static int toInt(InputStream in) throws java.io.IOException {
    byte[] byte_array_4 = new byte[4];

    byte_array_4[0] = (byte) in.read();
    byte_array_4[1] = (byte) in.read();
    byte_array_4[2] = (byte) in.read();
    byte_array_4[3] = (byte) in.read();

    return toInt(byte_array_4);
  }
}//end class