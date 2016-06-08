package com.dvoss;

import com.sun.org.apache.xpath.internal.operations.Mod;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static ArrayList<Person> people = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

        File f = new File("people.csv");
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split(",");
            Person person = new Person(columns[0], columns[1], columns[2], columns[3], columns[4], columns[5]);
            people.add(person);
        }

        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    HashMap m = new HashMap();

                    ArrayList<Person> twenty;
                    twenty = new ArrayList<>(people.subList(0, 19));
                    m.put("people", twenty);

                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );
//        Spark.get(
//                "/person",
//                (request, response) -> {
//                    HashMap m = new HashMap();
//                    m.put("person", people.get(Integer.valueOf()));
//                },
//
//        );


    }
}
