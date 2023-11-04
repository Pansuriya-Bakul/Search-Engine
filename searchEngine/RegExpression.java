package searchEngine;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.*;

public class RegExpression {

    // CHANGE HERE!
    public static String folderLocation = "convertedWebPages";

    public static boolean findEmailAddresses(String text) {
        // Create a Pattern object
        Pattern pattern = Pattern.compile("\\b[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,4}\\b");
        Boolean valueFound = false;

        // Now create a matcher object.
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println("Found Email Address: " + matcher.group());
            valueFound = true;
        }
        return valueFound;
    }

    public static boolean findWebLinks(String text) {
        // Create a Pattern object
        Pattern pattern = Pattern.compile("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Boolean valueFound = false;
        // Now create a matcher object.
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println("Found Web Link: " + matcher.group());
            valueFound = true;
        }
        return valueFound;
    }

    public static boolean findPostalCodes(String text) {
        // Create a Pattern object for Canadian postal codes
        Pattern pattern = Pattern.compile("\\b[A-Za-z]\\d[A-Za-z] ?\\d[A-Za-z]\\d\\b");
        Boolean valueFound = false;

        // Now create a matcher object.
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println("Found Postal Code: " + matcher.group());
            valueFound = true;
        }
        return valueFound;
    }

    public static boolean findPhoneNumbers(String text) {
        String pattern = "(\\()?(\\d{3})(\\))?[- ](\\d{3})-(\\d{4})";
        Boolean valueFound = false;

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create a matcher object.
        Matcher matcher = r.matcher(text);
        while (matcher.find()) {
            System.out.println("Found Phone Number: " + matcher.group());
            valueFound = true;
        }
        return valueFound;
    }

    public static void findPatterns(int choice) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {

        HashMap<Integer, Method> methodMap = new HashMap<>();
        Boolean valueFound = false;
        final File newFolderStructure = new File(folderLocation);

        // store the Methods in the HashMap
        try {
            Class<?>[] cArg = new Class[]{String.class};
            methodMap.put(1, RegExpression.class.getMethod("findEmailAddresses", cArg));
            methodMap.put(2, RegExpression.class.getMethod("findWebLinks", cArg));
            methodMap.put(3, RegExpression.class.getMethod("findPostalCodes", cArg));
            methodMap.put(4, RegExpression.class.getMethod("findPhoneNumbers", cArg));

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (final File fileEntry : newFolderStructure.listFiles()) {
            if (!fileEntry.isDirectory()) {
                String pathname = fileEntry.getPath();
                try (BufferedReader br = new BufferedReader(new FileReader(pathname))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        try {
                            Boolean value = (Boolean) methodMap.get(choice).invoke(null, line);
                            if (value) {
                                valueFound = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e) {
                    // File not found, print an error message and continue with the next file.
//                    System.err.println("File not found: " + pathname);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!valueFound) {
            System.out.println("Oops! No value found!");
        }
    }

    public static void main(String args[]) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, IOException {
        System.out.println("Choose a number!\n1 :\tEmail Addresses\n2 :\tWeb Links\n3 :\tPostal Codes\n4 :\tPhone Numbers\n");
        int choice = new Scanner(System.in).nextInt();
        findPatterns(choice);
    }
}
