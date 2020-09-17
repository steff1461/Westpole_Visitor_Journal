package be.technobel.westpole_visitor_journal.utils;

import java.io.*;

public class LangProvider {

    private LangProvider() {

    }

    public static StringBuilder retrieveLangTxt(String langType) {


        StringBuilder builder = new StringBuilder();


        String path="C:/Users/Will/Documents/Cours informatique/Westpole-visitor-journal/be.technobel" +
              ".westpole_visitor_journal/src/main/resources/languages/"+langType+".json";
        String line;
        System.out.println(path);

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {

            while ((line = reader.readLine()) != null) {

                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder;



//        StringBuilder builder = new StringBuilder();
//        String path = "./languages/" + langType + ".json";
//           String line;
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
//
//            while ((line = reader.readLine()) != null) {
//
//                builder.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return builder;
    }
}