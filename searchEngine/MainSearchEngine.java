package searchEngine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class MainSearchEngine {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {

        //Web crawling 
        String links = "https://www.apple.com/ca/";
        WebCrawler web = new WebCrawler();
//    	web.crawlPages(links);       

        //Conversion finished from html to text

        // User Interaction start 
        Scanner userInput = new Scanner(System.in); 
        
        String more = "yes";
        
        do {
            System.out.println("------------------------------------------------");
            System.out.println("Please enter the feature to be searched: "
                    + "\n 1. Regular Expression"
                    + "\n 2. Spell Checker"
                    + "\n 3. Word Completion" // Updated to Word Completion
                    + "\n 4. Inverted Index"
                    + "\n 5. Frequency Count"
                    + "\n 6. Sort Pages using Rank"); // Updated to 6 for consistency
            System.out.println("------------------------------------------------");

            int feature = 0;
            try {
                feature = userInput.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid feature number.");
                userInput.nextLine(); // Clear the input buffer
                continue; // Restart the loop
            }

            switch (feature) {
                case 1:
                    System.out.println("-----------Regular Expression------------");
                    System.out.println("Choose a number!\n1 :\tEmail Addresses\n2 :\tWeb Links\n3 :\tPostal Codes\n4 :\tPhone Numbers");
                    System.out.println("-----------------------------------------");
                    int choice = new Scanner(System.in).nextInt();
                    RegExpression.findPatterns(choice);
                    break;

                case 2:
                    try {
                        System.out.println("-----------Provide Your Input Search-----------");
                        Scanner sc = new Scanner(System.in);
                        String keyword = sc.next();
                        ISpellCorrector corrector = new SpellCorrector();
                        corrector.useDictionary("src//TxtFiles//dictonary.txt");
                        String suggestion = corrector.suggestSimilarWord(keyword);
                        if (suggestion == null) {
                            suggestion = "No similar word found";
                        }
                        System.out.println("Suggestion is: " + suggestion);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 3: 
                    System.out.println("-----------Provide Input Search-----------");
                    String keyword = userInput.next();
                    TrieTree.autoSuggest(keyword);
                    break;

                case 4:
                    InvertedIndex ii = new InvertedIndex();
                    ii.createInvertedIndex();
                    break;

                case 5:
                    FrequencyCount.printWordFrequency();
                    break;

                case 6:
                    FrequencyCount.sortByRank();
                    break;
                    
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        
            System.out.println("\nMore Search? (yes/no)");
            Scanner sc = new Scanner(System.in);  
            more = sc.next();
            
        } while (more.equalsIgnoreCase("yes"));

        System.out.println("\nThank You!");

    }
}
