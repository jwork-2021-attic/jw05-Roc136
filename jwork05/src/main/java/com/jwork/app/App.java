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
import com.jwork.app.utils.EventManager;
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
    static public final int terminalHeight = 40;
    static public final int terminalWidth = 64;
    static public final int fps = 30;

    public App() {
        super();
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

    public static void initMapFile() {
        File file = new File("map");
        if (file.exists() && file.listFiles().length > 0) {
            return;
        } else {
            file.mkdir();
            File new_map0 = new File("map/map0.csv");
            File new_map1 = new File("map/map1.csv");
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

    public AsciiPanel getTerminal() {
        return terminal;
    }

    private static boolean isServer;

    public static boolean isServer() {
        return isServer;
    }

    private static int playerNum;

    public static int playerNum() {
        return playerNum;
    }

    private static int playerID;

    public static int playerID() {
        return playerID;
    }

    private static String host;

    public static String host() {
        return host;
    }

    public static void main(String[] args) {
        // for (String s: args) {
        //     System.out.println(s);
        // }
        if (args.length == 0) {
            System.out.print("请添加参数\"-s $playerNum\"或\"-c $playerID\"");
            System.exit(-2);
        }
        if (args[0].equals("-s")) {
            App.isServer = true;
            App.host = "";
            App.playerID = 0;
            try {
                App.playerNum = Integer.valueOf(args[1]);
                if (playerNum <= 0 || playerNum > 7) {
                    System.out.print("最大玩家数量：7");
                    System.exit(-1);
                }

            } catch (Exception e) {
                System.out.print("参数错误");
                System.exit(-1);
            }
        } else if (args[0].equals("-c")) {
            App.isServer = false;
            try {
                App.host = args[1];
                App.playerID = Integer.valueOf(args[2]);
                try {
                    App.playerNum = Integer.valueOf(args[3]);
                    if (playerID < 0 || playerID > playerNum - 1) {
                        System.out.print("请输入正确的ID");
                        System.exit(-1);
                    }
                } catch (Exception e) {
                    System.out.print("连接服务器错误");
                    System.exit(-1);
                }
            } catch (Exception e) {
                System.out.print("参数错误");
                System.exit(-1);
            }
        } else {
            System.out.print("请添加参数\"-s $playerNum\"或\"-c $host $playerID\"");
            System.exit(-2);
        }
        App.initMapFile();
        App app = new App();
        Thread t = new Thread(new Flasher(app, fps));
        t.start();
        try {
            EventManager m = new EventManager();
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

}
