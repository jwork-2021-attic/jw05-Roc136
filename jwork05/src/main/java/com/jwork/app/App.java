package com.jwork.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JFrame;

import com.jwork.app.asciiPanel.AsciiFont;
import com.jwork.app.asciiPanel.AsciiPanel;
import com.jwork.app.screen.Screen;
import com.jwork.app.screen.StartScreen;
import com.jwork.app.utils.Flasher;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 *
 * @author Aeranythe Echosong
 */
public class App extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;
    static public int terminalHeight = 40;
    static public int terminalWidth = 64;

    public App() {
        super();
        // terminal = new AsciiPanel(terminalWidth, terminalHeight, AsciiFont.MYUI_16_16);
        terminal = new AsciiPanel(terminalWidth, terminalHeight, AsciiFont.MYUI_15_15);
        add(terminal);
        pack();
        screen = new StartScreen();
        addKeyListener(this);
        repaint();
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    /**
     *
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        // repaint();
    }

    /**
     *
     * @param e
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     *
     * @param e
     */
    public void keyTyped(KeyEvent e) {
    }

    private static void initMapFile() {
        File file = new File("map");
        if (file.exists() && file.listFiles().length > 0) {
            return;
        } else {
            file.mkdir();
            // File map0 = new File(App.class.getClassLoader().getResourceAsStream("map/map0.csv").getPath());
            // File map1 = new File(App.class.getClassLoader().getResourceAsStream("map/map1.csv").getPath());
            File new_map0 = new File("map/map0.csv");
            File new_map1 = new File("map/map1.csv");
            // try {
            //     Files.copy(map0.toPath(), new_map0.toPath());
            //     Files.copy(map1.toPath(), new_map1.toPath());
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
            try (
                InputStream ins0 = App.class.getClassLoader().getResourceAsStream("map/map0.csv");
                Reader reader0 = new InputStreamReader(ins0);
                CSVReader csvReader0 = new CSVReader(reader0);
            ) {
                if (!new_map0.exists()) {
                    try {
                        new_map0.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try (
                    Writer writer0 = new FileWriter("map/map0.csv");
                    CSVWriter csvWriter0 = new CSVWriter(writer0, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, ' ', "\n");
                ) {
                    String[] record;
                    while ((record = csvReader0.readNext()) != null) {
                        csvWriter0.writeNext(record);
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try (
                InputStream ins1 = App.class.getClassLoader().getResourceAsStream("map/map1.csv");
                Reader reader1 = new InputStreamReader(ins1);
                CSVReader csvReader1 = new CSVReader(reader1)
            ) {
                if (!new_map1.exists()) {
                    try {
                        new_map1.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try (
                    Writer writer1 = new FileWriter("map/map1.csv");
                    CSVWriter csvWriter1 = new CSVWriter(writer1, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, ' ', "\n");
                ) {
                    String[] record;
                    while ((record = csvReader1.readNext()) != null) {
                        csvWriter1.writeNext(record);
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        App.initMapFile();
        App app = new App();
        Thread t = new Thread(new Flasher(app, 30));
        t.start();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

}
