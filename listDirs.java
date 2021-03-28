import java.io.*;
import java.util.Arrays;
public class listDirs {

    public static void main(String[] args) {
	    File f = new File(".");
	    File[] files = f.listFiles();
	    Arrays.sort(files);
            // Get the names of the files by using the .getName() method
            for (int i = 0; i < files.length; i++) {
                if ( files[i].isDirectory()) {
		  System.out.println(files[i].getName());
		}
            }
        }
    }