package be.technobel.westpole_visitor_journal.utils;

import java.io.*;

public class LangProvider {

    private LangProvider() {

    }


    public static StringBuilder retrieveLangTxt(String langType) {

        File file = new File("src/main/resources/languages/"+langType+".json");
        StringBuilder builder = new StringBuilder();
        String line;
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {

            while ((line=reader.readLine())!=null){

                builder.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return builder;

}

}
