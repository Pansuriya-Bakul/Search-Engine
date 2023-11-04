package searchEngine;

import java.io.*;
import java.util.*;

public class FrequencyCount { 

    public static Hashtable<String, Integer> getWordCount() {
        Hashtable<String, Integer> wordCountMap = new Hashtable<String, Integer>();

        try {
            Scanner sc = new Scanner(System.in);
            File myFolder = new File("convertedWebPages");
            File[] files = myFolder.listFiles();

            System.out.println("Enter a word to search: "); 
            String userInput = sc.nextLine();

            for (File file : files) {
                HashMap<String, Integer> wordFrequencyMap = new HashMap<String, Integer>();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line = br.readLine();

                    while (line != null) {
                        String[] words = line.toLowerCase().split(" ");
                        for (String word : words) {
                            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);   
                        }
                        line = br.readLine();
                    }
                } catch (FileNotFoundException e) {
                    // Skip if file not found
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                wordCountMap.put(file.getName(), wordFrequencyMap.getOrDefault(userInput, 0));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return wordCountMap;
    }

    public static void printWordFrequency() {
        Hashtable<String, Integer> wordCountMap;
        try {
            wordCountMap = FrequencyCount.getWordCount();

            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordCountMap.entrySet());

            for (Map.Entry<String, Integer> entry : entryList) {
                String fileName = entry.getKey();
                int frequency = entry.getValue();
                if (frequency != 0) {
                    System.out.println("The word appeared " + frequency + " times in the file " + fileName);
                } else {
                    System.out.println("The word not found in " + fileName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sortByRank() {
        try {
            Hashtable<String, Integer> wordCountMap = FrequencyCount.getWordCount();

            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordCountMap.entrySet());
            entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            System.out.println("The Top 5 search results");               
            for (int i = 0; i < Math.min(5, entryList.size()); i++) {
                Map.Entry<String, Integer> entry = entryList.get(i);
                String fileName = entry.getKey();
                int frequency = entry.getValue();
                System.out.println("The word appeared " + frequency + " times in the file " + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FrequencyCount.printWordFrequency();
        FrequencyCount.sortByRank();
    }
}
