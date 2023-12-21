package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InputFocusControl {
    private JPanel getContent() {
        JTree tree = new JTree();
        tree.setEditable(true);
        JTextField textField = new JTextField(12);
        MouseListener ml = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                KeyboardFocusManager kfm =
                        KeyboardFocusManager.getCurrentKeyboardFocusManager();
                System.out.println("focusOwner = " +
                        kfm.getFocusOwner().getClass().getName());
            }
        };
        tree.addMouseListener(ml);
        textField.addMouseListener(ml);
        JPanel right = new JPanel(new GridBagLayout());
        right.add(textField, new GridBagConstraints());
        JPanel panel = new JPanel(new GridLayout(1,0));
        panel.add(tree);
        panel.add(right);
        return panel;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new InputFocusControl().getContent());
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}