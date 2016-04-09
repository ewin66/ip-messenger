package ip;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.SwingUtilities;

public class refresh implements Runnable{
            MyTableModel model;
            String host ;
            refresh(MyTableModel model)
            {
                this.model = model;
            }
            
            public void run ()
            {   
                ArrayList<String> total_add = new ArrayList<String>();
                ArrayList<String> total_name = new ArrayList<String>();
                try
                {
                      for (final Enumeration< NetworkInterface > interfaces = NetworkInterface.getNetworkInterfaces( );interfaces.hasMoreElements( );)
                      {
                                final NetworkInterface cur = interfaces.nextElement( );
                                
                                //if ( cur.isLoopback( ) )
                                    //continue;

                                for ( final InterfaceAddress addr : cur.getInterfaceAddresses( ) )
                                {
                                    final InetAddress inet_addr = addr.getAddress( );

                                    if ( !( inet_addr instanceof Inet4Address ) )
                                        continue;
                                    total_add.add(""+inet_addr.getHostAddress( ));
                                    total_name.add(""+inet_addr.getHostName());
                             }
			}
                       System.out.println(total_add.size());
                       int index=0;
                        for(String i: total_add)
                        {       host = i;
                                if(total_add.size()>1 && i.equalsIgnoreCase("127.0.0.1"))
                                    continue;
                                System.out.println(""+host);
                               try
                               {
                                   Socket chk = new Socket(host, 6789);
                                   System.out.println(host + "  --->>    connected");
                                   final String[] aa = new String[]{host,total_name.get(index)+"",System.getProperty("user.name")};
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            System.out.println("Pankaj1");
                                            model.setValueAt(aa);
                                            System.out.println("Pankaj1");
                                            //System.out.println(model.getValueAt(0, 0));
                                        }
                                    });
                                    try{System.in.read();}
                                    catch(Exception q){}
                                    chk.close();
                               }
                               catch(Exception notfound)
                               {    System.out.println(host + "  --->>   not connected");
//                                    try{System.in.read();}
//                                    catch(Exception q){}
                               }
                                index++;
                            }
                }
                catch(Exception e1)
                {
                        System.out.println(String.valueOf(e1) + "\n");
                }
                //return users;
            }
}
