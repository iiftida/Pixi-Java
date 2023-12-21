package Main;
//import com.sun.deploy.panel.GeneralPanel;
import test.testfile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game {

    public static void main(String[] args)
    {
        JFrame window = new JFrame("Pixy Green");

//        window.setContentPane(new GamePanel());
//        window.setContentPane(new SwingJPanelDemo());

        JPanel firstPanel = new GamePanel();
        window.addMouseListener(new MouseAdapter() {
            private Color background;

            @Override
            public void mousePressed(MouseEvent e) {
                firstPanel.requestFocus();
                firstPanel.requestFocusInWindow();
                firstPanel.setRequestFocusEnabled(true);
                System.out.println("First Panel Mouse CLick");

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                firstPanel.requestFocus();
                firstPanel.requestFocusInWindow();
//        setFocusable(true);
                firstPanel.setRequestFocusEnabled(true);
                System.out.println("First Panel Mouse Relased");
            }
        });


//        JPanel secondPanel = new ChatPanel();
        JPanel secondPanel = new JPanel();
//        secondPanel = new testfile("127.0.0.1");
//        frame.setVisible(true);
//        secondPanel.addMouseListener(new MouseAdapter() {
//            private Color background;
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                secondPanel.requestFocus();
//                secondPanel.requestFocusInWindow();
//                secondPanel.setRequestFocusEnabled(true);
//                System.out.println("2nd Panel Mouse CLick");
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                firstPanel.requestFocus();
//                firstPanel.requestFocusInWindow();
//                firstPanel.setRequestFocusEnabled(true);
//                System.out.println("2bd Panel Mouse realsed");
//            }
//        });



        window.setLayout(new FlowLayout());
        window.add(firstPanel);
        window.add(secondPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
        System.out.println("Main file");
        String currentDir = System.getProperty("A.txt");

        System.out.println("Current dir using System:" +currentDir);





    }

}
