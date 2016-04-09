package ip;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

public class refresh_send implements Runnable{
    int chkbit;
    refresh_send(int chkbit)
    {
        this.chkbit = chkbit;
    }
    public void run() {
                String host = "";
                ArrayList<String> total_add = new ArrayList<String>();
                try
                {   byte[] buf = new byte[256];
                    DatagramSocket socket = new DatagramSocket();
                    InetAddress group = InetAddress.getByName("224.0.0.1");
                    InetAddress local = null;
                    
                      for (final Enumeration< NetworkInterface > interfaces = NetworkInterface.getNetworkInterfaces( );interfaces.hasMoreElements( );)
                      {
                                final NetworkInterface cur = interfaces.nextElement( );

//                                if ( cur.isLoopback( ) )
//                                    continue;

                                for ( final InterfaceAddress addr : cur.getInterfaceAddresses( ) )
                                {
                                    final InetAddress inet_addr = addr.getAddress( );

                                    if ( !( inet_addr instanceof Inet4Address ) )
                                        continue;
                                    total_add.add(""+inet_addr.getHostAddress( ));
                             }
			}
                       System.out.println(total_add.size() + " -> " + total_add);
                       int index=0;
                        for(String i: total_add)
                        {       host = i;
                                if(total_add.size()>1 && i.startsWith("127.")) //ignore all ip like 127.0.0.1
                                    continue;
                                if(total_add.size()==1)
//                                    local = InetAddress.getByName(host);
                                    group = InetAddress.getByName(host);
                                System.out.println(""+host);
                               try
                               {
//                                   Socket chk = new Socket(host, 6789);
                                   String[] aa = new String[]{host,InetAddress.getLocalHost().getHostName(),System.getProperty("user.name")};
                                   String send = new String("");
                                   for(String j : aa)
                                   {
                                       send += j + " ";
                                   }

                                   if(chkbit==1)
                                       send +="new a";
                                   else if(chkbit==0)
                                       send +="old";
                                   buf = send.getBytes();
//                                   System.out.print(buf+"llllllllllllllllllllllllllllllllllllllllllll");
//                                   System.out.println("Pankaj1  -->>  "+ Thread.currentThread().getId());
                                   System.out.println(host + "  --->>   making datagram packet : " + send);
                                   DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
                                   System.out.println(host + "  --->>   sending pacet");
//                                   try
//                                   {
                                       socket.send(packet);
//                                   }
//                                   catch(Exception exp)
//                                   {
////                                       InetAddress group1 = InetAddress.getByName("234.0.0.1");
//                                       //DatagramSocket socket1 = new DatagramSocket();
//                                       System.out.println("chkpoint ............send");
////                                       DatagramPacket packet1 = new DatagramPacket(buf, buf.length, local, 4446);
//                                       System.out.println("chkpoint ............send");
//                                       socket.send(packet);
//                                   }
                                   System.out.println(host + "  --->>   sent packet");
                                   
                                   socket.close();
                               }
                               catch(Exception notfound)
                               {    System.out.println(host + "  --->>   not connected   --->>"+ notfound.getMessage());
                               		notfound.printStackTrace();
                               }
                                index++;
                            }
                }
                catch(Exception e1)
                {
                        e1.printStackTrace();
                }
    }

}
