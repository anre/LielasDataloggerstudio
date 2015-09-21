package org.lielas.dataloggerstudio.test;

import org.lielas.dataloggerstudio.test.gui.MainFrame;

import java.awt.*;

/**
 * Created by Andi on 29.05.2015.
 */
public class Main {

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
