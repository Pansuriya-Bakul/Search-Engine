package searchEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class InvertedIndex {

    static File allFiles = new File("convertedWebPages");
    HashMap<String, String> wordToFileMap;
    Scanner sc = new Scanner(System.in);

    public void createInvertedIndex() throws IOException {
        System.out.println("Enter the word to find the file that contains it: ");
        String userInput = sc.next().toLowerCase();
        int count = 0;

        try {
            for (File file : allFiles.listFiles()) {
                wordToFileMap = new HashMap<String, String>();
                Document doc = Jsoup.parse(file, "UTF-8");
                String text = doc.text();
                String[] words = text.split("\\W+");

                for (String word : words) {
                    if (!wordToFileMap.containsKey(word.toLowerCase())) {
                        wordToFileMap.put(word.toLowerCase(), file.getName());
                    }
                }

                if (wordToFileMap.containsKey(userInput)) {
                    count++;
                    System.out.println("'" + userInput + "' found in '" + wordToFileMap.get(userInput) + "'");
                }
            }

        } catch (IOException e) {
            // Handle the exception if needed
        }

        if (count == 0) {
            System.out.println("'" + userInput + "' not found in any of the files.");
        }
    }

    public static void main(String[] args) throws IOException {
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.createInvertedIndex();
    }
}
