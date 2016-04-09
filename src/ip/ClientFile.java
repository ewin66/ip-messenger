// file sender
package ip;
import java.io.*;
import java.net.*;
public class ClientFile implements Runnable{
    String ta,tb,t;
    Socket clientsocket;
    public ClientFile(String a,String b,String c)
    {
        ta = a;
        tb = b;
        t = c;
    }
    public void run() {
        try
        {
            clientsocket = new Socket(t,6789);
            System.out.println("send1");
            String msg = ta;
            String filename = tb;
            System.out.println("send5");

            //BufferedReader in = new BufferedReader( new InputStreamReader(clientsocket.getInputStream(), "8859_1") );
//            DataOutputStream outtoserver = new DataOutputStream(clientsocket.getOutputStream());
//            BufferedReader infromserver = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

            System.out.println("send6");
            System.out.println("filename = " + filename);
            if(filename.equals(""))
                msg = "Message : \n" + msg;
            else
                msg = "Message : \n" + msg + "\n\n\n\nFiles Received : \n" + filename;
            
                OutputStream os = clientsocket.getOutputStream();
                InputStream  in = clientsocket.getInputStream();
                ByteStream.toStream(os, msg);
                String local = ByteStream.toString(in);
                System.out.println("local received : " + local);
                if(local.equals("Over2") && !filename.equals(""))
                {
                    ByteStream.toStream(os, "FiLe");
//                    outtoserver.writeBytes("FiLe\n");
                    String[] files = filename.split("\n");
                    copy(files);
                    local = ByteStream.toString(in);
                                
                 }
                else if(local.equals("Over2") && filename.equals(""))
                    ByteStream.toStream(os,"No FiLe");
            
            System.out.println("send7");
            clientsocket.close();
            System.out.println("send8");
        }
        catch(Exception e2)
        {
                System.out.println("ClientFile   "+String.valueOf(e2) + "\n");
        }
    }
    public void copy(String[] files)
    {
        try
        {
            int totalfiles=0;
            int r=0;
//            FileInputStream fromFile1 = null;
//            BufferedInputStream fromFile=null;
            File oldfile;
            OutputStream os  = clientsocket.getOutputStream();
            InputStream  in = clientsocket.getInputStream();
          ByteStream.toStream(os, files.length);
//            outtoserver.write(files.length);
            String chk;
            while(totalfiles<files.length)
            {

                oldfile = new File(files[totalfiles]);
                if(oldfile.isDirectory())
                {
                    System.out.println("client folder 4");
                    ByteStream.toStream(os,"folder");
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
                    ByteStream.toStream(os,oldfile.getName());
                    System.out.println("client folder 7");
                    copy(folder);
//                    copy(outtoserver,infromserver,folder);
                    System.out.println("client folder 8");

                }
                else if(oldfile.isFile())
                {
                    ByteStream.toStream(os,"file");

//                    fromFile1 =  new FileInputStream(oldfile);
//                    fromFile= new BufferedInputStream(fromFile1);

                    System.out.println("client file 4");
                    chk = ByteStream.toString(in);
                    if(chk.equals("send"))
                    {
                        System.out.println("client  file 5");
                        ByteStream.toStream(os,oldfile.getName());
                        System.out.println("client  file 6");
                        ByteStream.toStream(os, oldfile);
//                        String[] folder = new String[]{oldfile.getAbsolutePath()};
//                        fs.main(clientsocket, folder);
//                        toStream(os, new File(files[totalfiles]));
                        System.out.println("client  file 7");
                    }
                }
                totalfiles++;
                System.out.println("client  file 8");
            }
        }
        catch(Exception e)
        {
            System.out.println("ClientFile -->>  "+e.toString());
        }
    }
    public static void toStream(OutputStream os, File file)
      throws java.io.FileNotFoundException,
             java.io.IOException{

    toStream(os, (int) file.length());

    byte b[]=new byte[1024];
    InputStream is = new FileInputStream(file);
    int numRead=0;

    while ( ( numRead=is.read(b)) > 0) {
      os.write(b, 0, numRead);
    }
    os.flush();
  }
    public static void toStream(OutputStream os, int i) throws java.io.IOException {
    byte [] byte_array_4 = toByteArray(i);
    System.out.println("The sent length is :   "+i);
    os.write(byte_array_4);
  }
    private static byte[] toByteArray(int in_int) {
    byte a[] = new byte[4];
    for (int i=0; i < 4; i++) {

      int  b_int = (in_int >> (i*8) ) & 255;
      byte b = (byte) ( b_int );

      a[i] = b;
     }
    return a;
  }
}
