package com.jwork.app.map;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Stack;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class MapGenerator {
    
    private int[][] map;
    private int width;
    private int height;
    // private String mapFile = null;
    private String mapFile = "map/map1.csv";

    public MapGenerator(int dim) {
        map = new int[dim][dim];
        this.width = dim;
        this.height = dim;
    }

    public MapGenerator(int width, int height) {
        map = new int[width][height];
        this.width = width;
        this.height = height;
    }

    public MapGenerator(String m) {
        this.mapFile= m;
        this.width = 0;
        this.height = 0;
    }

    public void generateMap() {
        if (mapFile == null) {
            Random rand = new Random();
            for(int x = 0; x < width; x++) {
                for(int y = 0; y < height; y++) {
                    map[x][y] = rand.nextInt(10);
                }
            }
        } else {
            try (Reader reader = Files.newBufferedReader(Paths.get(MapGenerator.class.getClassLoader().getResource(mapFile).toURI()));
                CSVReader csvReader = new CSVReader(reader)) {

                String[] record;
                Stack<String[]> ss = new Stack<>();
                height = 0;
                width = 0;
                while ((record = csvReader.readNext()) != null) {
                    // System.out.println("["+ String.join(", ", record) +"]");
                    height++;
                    ss.push(record);
                }
                width = ss.peek().length;

                map = new int[height][width];
                for (int x = height - 1; x >= 0; x--) {
                    String[] line = ss.pop();
                    for (int y = 0; y < width; y++) {
                        map[x][y] = Integer.parseInt(line[y]);
                    }
                }

            } catch (IOException | CsvValidationException ex) {
                ex.printStackTrace();
            } catch (URISyntaxException u) {
                u.printStackTrace();
            }
        }
    }

    public int[][] getMap() {
        return map;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
