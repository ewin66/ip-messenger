package ip;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class refresh implements Runnable{
            MyTableModel model;
            String host ;
            refresh(MyTableModel model)
            {
                this.model = model;
            }
            
            public void run ()
            {   new Thread(new refresh_send(1)).start();
                try
                {   byte[] buf = new byte[256];
                    final MulticastSocket socket_recv = new MulticastSocket(4446);
                    InetAddress group = InetAddress.getByName("234.0.0.1");
                    
                    try
                    {
                        socket_recv.joinGroup(group);
                    }
                    catch(Exception exp)
                    {
//                        InetAddress addr = InetAddress.getLocalHost();
//                        System.out.println(addr.getHostAddress()+"aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//                        String[] aa = new String[]{addr.getHostAddress()+"",InetAddress.getLocalHost().getHostName(),System.getProperty("user.name")};
//                        if(!model.search(aa))
//                            model.setValueAt(aa);
//                        DatagramSocket soc = new DatagramSocket(4445);
//                        byte[] buffer = new byte[1024];
//                        DatagramPacket packet1 = new DatagramPacket(buffer, buffer.length );
//                        System.out.println("chkpoint ............");
//                          soc.receive(packet1);
//                          System.out.println("chkpoint ............");
//                          String data = new String(packet1.getData());
//                          System.out.println("hahahahahaha"+data+"hahahahahahahahahahhahahah");

                    }
                    System.out.println("pankaj\n");
                    while(true)
                    {   buf = new byte[256];
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        socket_recv.receive(packet);
                        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        if(Thread.currentThread().isInterrupted())
                            throw new InterruptedException("Stopped by ifInterruptedStop()");
                        String received = new String(packet.getData());
                        final String[] aa = received.split(" ");
                        System.out.println("sssssssssssssss   "+aa[3]+"   ppppppppppppppppppppp");
                        if(aa[3].equalsIgnoreCase("new")==true)
                        {   System.out.println("Sending old");
                            new Thread(new refresh_send(0)).start();
                        }
//                        System.out.println("Pankaj1  -->>  "+ Thread.currentThread().getId());
                        if(!model.search(aa))
                            model.setValueAt(aa);
                        System.out.println("Pankaj1");
//                        SwingUtilities.invokeLater(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if(!model.search(aa))
//                                                model.setValueAt(aa);
//                                            System.out.println("Pankaj1");
//                                            //System.out.println(model.getValueAt(0, 0));
//                                        }
//                                    });
                        System.out.println("received: " + received);
//                        Thread.currentThread().sleep(5000);

                    }
                }
                catch(Exception e1)
                {
                        System.out.println(String.valueOf(e1) + "pankaj\n");
//                        socket_recv.leaveGroup(group);
                        return;
                }
            }
}
