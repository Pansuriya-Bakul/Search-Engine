package searchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class mergeTextFiles {

    public static void mergeFiles() throws IOException {
        // Create an instance of the directory
        File dir = new File("convertedWebPages");

        // Create an object of PrintWriter for the output file
        PrintWriter pw = new PrintWriter("src//TxtFiles//output.txt");

        // Get a list of all the files in the directory convertedWebPages
        String[] fileNames = dir.list();

        // Loop for reading the contents of all the files in the directory
        for (String fileName : fileNames) {
            // Create an instance of the file from the name of the file stored in the string array
            File file = new File(dir, fileName);
            BufferedReader br = null;

            try {
                // Create an object of BufferedReader
                br = new BufferedReader(new FileReader(file));
                
                // Read from the current file
                String line = br.readLine();
                while (line != null) {
                    // Write to the output file
                    pw.println(line);
                    line = br.readLine();
                }
                pw.flush();
            } catch (FileNotFoundException e) {
                // File not found, print an error message and continue with the next file.
                System.err.println("File not found: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close the BufferedReader in a finally block to ensure resources are released.
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // Close the PrintWriter after merging all the files.
        pw.close();
    }
}
