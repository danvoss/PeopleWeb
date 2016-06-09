package com.dvoss;

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

                    boolean previous;
                    boolean next;
                    int offset = 0;
                    String offsetStr = request.queryParams("offset");

                    if(offsetStr != null) {
                        offset = Integer.valueOf(offsetStr);
                    }
                    if (offset < 20) {
                        previous = false;
                    }
                    else {
                        previous = true;
                    }
                    if ((offset - 20) > people.size()) {
                        next = false;
                    }
                    else {
                        next = true;
                    }

                    ArrayList<Person> twenty;
                    twenty = new ArrayList<>(people.subList(offset, offset + 20));

                    m.put("people", twenty);
                    m.put("offsetdown", offset - 20);
                    m.put("offsetup", offset + 20);
                    m.put("previous", previous);
                    m.put("next", next);

                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/person",
                (request, response) -> {
                    HashMap m = new HashMap();
                    String pId = request.queryParams("id");
                    int idNum = Integer.valueOf(pId);
                    Person person = people.get(idNum - 1);
                    m.put("person", person);
                    return new ModelAndView(m, "person.html");
                },
                new MustacheTemplateEngine()

        );


    }
}
