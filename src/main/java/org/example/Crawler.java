package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crawler {
    public static void main(String[] args) {
        String url = "https://www.google.com/";
        cleanCsv();
        crawl(1, url, new ArrayList<String>());
    }

    private static void crawl(int level, String url, ArrayList<String> visitedUrls) {
        if (level <= 5) {
            Document doc = request(url, visitedUrls);

            if (doc != null) {
                for (Element link : doc.select("a[href]")) {
                    String next_link = link.absUrl("href");
                    if (!visitedUrls.contains(next_link)) {
                        crawl(++level, next_link, visitedUrls);
                    }
                }
            }
        }
    }

    private static Document request(String url, ArrayList<String> visited) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if (con.response().statusCode() == 200) {
//                System.out.println("Link: " + url);
//                System.out.println("Title: " + doc.title());

                saveToCsv(""+url,"" + doc.title(),"Result.csv");
                visited.add(url);

                return doc;
            }
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, "Something bad happened!");
        }

        return null;
    }

    public static void saveToCsv(String url, String title,String filepath){
        try {
            FileWriter fw = new FileWriter(filepath,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(title+","+url);
            pw.flush();
            pw.close();

        }
        catch (Exception E){
            JOptionPane.showMessageDialog(null,"Records not saved");
        }
    }

    public static void cleanCsv(){
        try {
            FileWriter fw = new FileWriter("Result.csv",false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println("Title"+","+"URL");
            pw.flush();
            pw.close();

        }
        catch (Exception E){
            JOptionPane.showMessageDialog(null,"Records not saved");
        }
    }
}
