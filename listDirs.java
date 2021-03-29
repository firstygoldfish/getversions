import java.io.*;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.Scanner;
public class listDirs {
    public static void main(String[] args) throws IOException, InterruptedException {
      if (args.length != 2) {
        print("Usage: java listDirs source_dir target_dir");
        System.exit(1);
      }
      loadVersions(args[0], args[1]);
    }
    
    public static void loadVersions(String path, String targetpath) throws IOException, InterruptedException {
      int foundversions = 0;
      File f = new File(path);
      File[] files = f.listFiles();
      Arrays.sort(files);
      for (int i = 0; i < files.length; i++) {
        if ( files[i].isDirectory() && files[i].getName().substring(0,2).equals("1.") ) {
          // Load version
          Path source = Paths.get(files[i].getPath()+"/"+files[i].getParentFile().getName());
          String targetstring = targetpath+files[i].getParentFile().getPath().substring(1)+"/"+files[i].getParentFile().getName();
          Path target = Paths.get(targetstring);
          print(files[i].getParentFile().getName()+":v"+files[i].getName());
          Files.copy(source,target,StandardCopyOption.REPLACE_EXISTING );
          // Get commit message
          String commitfile = files[i].getPath()+"/commit.txt";
          // commit current version and cross fingers
          String[] addcommands = {"svn", "add", targetstring};
          Process addprocess = Runtime.getRuntime().exec(addcommands);
          int addexitcode = addprocess.waitFor();
          monitorProcess(addprocess);
          String[] cicommands = {"svn", "ci", targetstring, "-F", commitfile};
          Process ciprocess = Runtime.getRuntime().exec(cicommands);
          int ciexitcode = ciprocess.waitFor();
          monitorProcess(ciprocess);
          //Process ciprocess = Runtime.getRuntime().exec("bash -c \"svn ci "+targetstring+" -F "+commitfile+"\"");
        } else {
          if (files[i].isDirectory()) loadVersions(files[i].getPath(),targetpath);
        }
      }
    }
    
    public static void monitorProcess(Process proc) throws IOException {
          BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));  
          BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream())); 
          // Read the output from the command 
          String s = null; 
          while ((s = stdInput.readLine()) != null) {     
            print(s); 
          }  
          // Read any errors from the attempted command 
          while ((s = stdError.readLine()) != null) {     
            System.out.println(s); 
          }
    }
    
    public static void print(String msg) {
      System.out.println(msg);
    }
}