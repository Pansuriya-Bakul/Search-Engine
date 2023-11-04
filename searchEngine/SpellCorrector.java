package searchEngine;

import java.io.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {

    private Trie trie = new Trie();
    private Map<String, Integer> dictionary = new HashMap<>();
    private final static Set<String> invalidWords = new HashSet<>(Arrays.asList("abcdefghijklmnopqrstuvwxyz"));

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        if (!file.exists()) {
            System.out.println("Dictionary file not found.");
            return;
        }

        // Read and process the dictionary file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    String lowerCaseWord = word.toLowerCase();
                    dictionary.put(lowerCaseWord, dictionary.getOrDefault(lowerCaseWord, 0) + 1);
                    trie.add(lowerCaseWord);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading dictionary file: " + e.getMessage());
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        if (inputWord == null || inputWord.length() == 0 || invalidWords.contains(inputWord.toLowerCase())) {
            System.out.println("Please provide a valid input word.");
            return null;
        }

        String word = inputWord.toLowerCase();
        String result = null;
        TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> similarityMap = new TreeMap<>();

        // Search for the input word in the trie
        INode node = trie.find(word);
        if (node == null) {
            // Calculate edit distance and find similar words
            for (String dictWord : dictionary.keySet()) {
                int distance = editDistance(dictWord, word);
                TreeMap<Integer, TreeSet<String>> wordFrequencyMap = similarityMap.getOrDefault(distance, new TreeMap<>());
                int frequency = dictionary.get(dictWord);
                TreeSet<String> wordsWithSimilarity = wordFrequencyMap.getOrDefault(frequency, new TreeSet<>());
                wordsWithSimilarity.add(dictWord);
                wordFrequencyMap.put(frequency, wordsWithSimilarity);
                similarityMap.put(distance, wordFrequencyMap);
            }

            if (!similarityMap.isEmpty()) {
                // Find the most similar word based on edit distance and word frequency
                Map.Entry<Integer, TreeMap<Integer, TreeSet<String>>> firstEntry = similarityMap.firstEntry();
                TreeMap<Integer, TreeSet<String>> wordFrequencyMap = firstEntry.getValue();
                if (!wordFrequencyMap.isEmpty()) {
                    Map.Entry<Integer, TreeSet<String>> lastEntry = wordFrequencyMap.lastEntry();
                    TreeSet<String> wordsWithSimilarity = lastEntry.getValue();
                    result = wordsWithSimilarity.first();
                }
            }
        } else {
            // If the word is already present in the trie
            result = word;
        }

        return result;
    }

    private int editDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        int[][] dp = new int[n + 1][m + 1];

        // Calculate edit distance using dynamic programming
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (i == 0)
                    dp[i][j] = j;
                else if (j == 0)
                    dp[i][j] = i;
                else if (word1.charAt(i - 1) == word2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else if (i > 1 && j > 1 && word1.charAt(i - 1) == word2.charAt(j - 2) && word1.charAt(i - 2) == word2.charAt(j - 1))
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 2][j - 2], dp[i - 1][j]), Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                else
                    dp[i][j] = 1 + Math.min(dp[i][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
            }
        }

        return dp[n][m];
    }
}
