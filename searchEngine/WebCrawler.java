package searchEngine;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
    private static final int MAX_PAGES_TO_CRAWL = 200;
    private static int pagesCrawled = 0;

    public static String crawl(String link) {
        if (pagesCrawled >= MAX_PAGES_TO_CRAWL) {
            return "";
        }

        String html = urlToHTML(link);
        Document doc = Jsoup.parse(html);
        String text = doc.text();
        String title = doc.title();
        createFile(title, text, link);
        Elements links = doc.select("a");
        String linksText = "";

        for (Element e : links) {
            String href = e.attr("abs:href");
            if (href.length() > 3) {
                linksText = linksText + "\n" + href;
            }
        }

        pagesCrawled++;
        return linksText;
    }

    public static void createFile(String title, String text, String link) {
        try {
            String[] titleSplit = title.split("\\|");
            String newTitle = "";
            for (String s : titleSplit) {
                newTitle = newTitle + " " + s;
            }
            File directory = new File("convertedWebPages");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, newTitle + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter pw = new PrintWriter(file);
            pw.println(link);
            pw.println(text);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String urlToHTML(String link) {
        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(4500);
            conn.setReadTimeout(4500);
            Scanner sc = new Scanner(conn.getInputStream());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {
                sb.append(" ").append(sc.next());
            }
            String result = sb.toString();
            sc.close();
            return result;
        } catch (IOException e) {
            System.out.println(e);
        }
        return link;
    }

    public static void crawlPages(String links) {
        try {
            File file = new File("CrawledPages.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.close();

            String allLinks = "";
            for (String link : links.split("\n")) {
                allLinks = allLinks + crawl(link);
                System.out.println(link);
                FileWriter fw = new FileWriter(file, true);
                fw.write(link + "\n");
                fw.close();
            }

            String allLinks2 = "";
            for (String link : allLinks.split("\n")) {
                In in = new In(file);
                String linksRead = in.readAll();
                if (!linksRead.contains(link)) {
                    allLinks2 = allLinks2 + crawl(link);
                    System.out.println(link);
                    FileWriter fw = new FileWriter(file, true);
                    fw.write(link + "\n");
                    fw.close();
                }
            }

            for (String link : allLinks2.split("\n")) {
                In in = new In(file);
                String linksRead = in.readAll();
                if (!linksRead.contains(link)) {
                    crawl(link);
                    System.out.println(link);
                    FileWriter fw = new FileWriter(file, true);
                    fw.write(link + "\n");
                    fw.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String links = "https://www.apple.com/ca/";
        crawlPages(links);
    }
}
