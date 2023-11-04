package searchEngine;

import java.io.*;
import java.util.*;

public class TrieTree {
    private static class TrieNode {
        Map<Character, TrieNode> children;
        char character;
        boolean isWordComplete;

        public TrieNode(char character) {
            this.character = character;
            children = new HashMap<>();
        }

        public TrieNode() {
            children = new HashMap<>();
        }

        public void insert(String word) {
            if (word == null || word.isEmpty())
                return;
            char firstChar = word.charAt(0);
            TrieNode child = children.get(firstChar);
            if (child == null) {
                child = new TrieNode(firstChar);
                children.put(firstChar, child);
            }

            if (word.length() > 1) {
                child.insert(word.substring(1));
            } else {
                child.isWordComplete = true;
            }
        }
    }

    private TrieNode root;

    public TrieTree(List<String> words) {
        root = new TrieNode();
        for (String word : words) {
            root.insert(word);
        }
    }

    private void suggestHelper(TrieNode node, List<String> suggestions, StringBuffer current) {
        if (node.isWordComplete) {
            suggestions.add(current.toString());
        }

        if (node.children == null || node.children.isEmpty())
            return;

        for (TrieNode child : node.children.values()) {
            suggestHelper(child, suggestions, current.append(child.character));
            current.setLength(current.length() - 1);
        }
    }

    public List<String> suggest(String prefix) {
        List<String> suggestions = new ArrayList<>();
        TrieNode lastNode = root;
        StringBuffer current = new StringBuffer();
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null) {
                return suggestions;
            }
            current.append(c);
        }
        suggestHelper(lastNode, suggestions, current);
        return suggestions;
    }

    public static void autoSuggest(String keyword) {
        try {
            List<String> words = new ArrayList<>();
            File folder = new File("src\\TxtFiles\\");
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    try (Scanner input = new Scanner(file)) {
                        while (input.hasNext()) {
                            String word = input.next();
                            words.add(word);
                        }
                    }
                }
            }

            File dictionaryFile = new File("src\\TxtFiles\\dictionary.txt");
            if (!dictionaryFile.exists()) {
                try {
                    dictionaryFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(dictionaryFile);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                    for (String word : words) {
                        if (!word.startsWith("http") && word.matches("[A-Za-z]+")) {
                            bw.write(word);
                            bw.newLine();
                        }
                    }
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            TrieTree trie = new TrieTree(words);
            List<String> relevantWords = trie.suggest(keyword);
            for (String word : relevantWords) {
                System.out.println(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Exception: " + e + e.getMessage());
        }
    }
}
