/*
 * Copyright (C) 2021 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package marvel;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser utility to load the Marvel Comics dataset.
 */
public class MarvelParser {
    //MarvelParser is not an ADT and therefore does not have a abstraction function or a representation invariant


    /**
     * Reads the Marvel Universe dataset. Each line of the input file contains a character name and a
     * comic book the character appeared in, separated by a tab character
     *
     * @param filename the file that will be read
     * @throws IOException if an error occurs while reading the file
     * @return A Map from comic book names to a list of the characters that appeared in them
     * @spec.requires filename is a valid file in the resources/data folder.
     */
    public static Map<String, List<String>> parseData(String filename) throws IOException {
        try {
            List<String> lines = readLines(filename);

            // You'll need to:
            //  - Split each line into its individual parts
            //  - Collect the data into some convenient data structure(s) to return to the graph building code
            Map<String, List<String>> map = new HashMap<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                if(parts.length != 2){
                    throw new IOException();
                }
                String name = parts[0];
                String book = parts[1];
                if (!map.containsKey(book)) {
                    map.put(book, new ArrayList<>());
                }
                map.get(book).add(name);
            }
            return map;
        }catch (Exception e){
            throw new IOException();
        }
    }

    /**
     * Reads all lines contained within the provided data file, which is located
     * relative to the data/ folder in this parser's classpath.
     *
     * @param filename The file to read.
     * @return A new {@link List<String>} containing all lines in the file.
     * @throws IllegalArgumentException if the file doesn't exist, has an invalid name, or can't be read
     */
    private static List<String> readLines(String filename) {
        // You can use this code as an example for getting a file from the resources folder
        // in a project like this. If you access data files elsewhere in your code, you'll need
        // to use similar code. If you use this code elsewhere, don't forget:
        //   - Replace 'MarvelParser' in `MarvelParser.class' with the name of the class you write this in
        //   - If the class is in src/main, it'll get resources from src/main/resources
        //   - If the class is in src/test, it'll get resources from src/test/resources
        //   - The "/" at the beginning of the path is important
        // Note: Most students won't re-write this code anywhere, this explanation is just for completeness.
        URL url = MarvelParser.class.getResource("/data/" + filename);
        if (url == null) {
            // url is null if the file doesn't exist.
            // We want to handle this case so we don't try to call
            // readLines and have a null pointer exception.
            throw new IllegalArgumentException("No such file: " + filename);
        }
        try {
            Path path = Path.of(url.toURI());
            return Files.readAllLines(path);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalArgumentException("Unable to read file", e);
        }
    }
}
