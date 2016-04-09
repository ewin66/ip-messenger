package ip;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
public class browse {
            public List<String> main ()
            {   List<String> path = new ArrayList<String>();
                File[] file=null;
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.setMultiSelectionEnabled(true);
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    file = fc.getSelectedFiles();
                    int a=0;
                    while(a<file.length)
                    {
                        path.add(file[a].getAbsolutePath() + "\n");
                        a++;
                    }
                }
                else
                    System.out.println("Open command cancelled by user.\n");
                return(path);
            }
}
