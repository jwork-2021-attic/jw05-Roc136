package com.jwork.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.jwork.app.asciiPanel.AsciiFont;
import com.jwork.app.asciiPanel.AsciiPanel;
import com.jwork.app.screen.Screen;
import com.jwork.app.screen.StartScreen;

/**
 *
 * @author Aeranythe Echosong
 */
public class App extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;

    public App() {
        super();
        terminal = new AsciiPanel(40, 41, AsciiFont.TALRYTH_15_15);
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
        repaint();
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

    public static void main(String[] args) {
        App app = new App();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

}
