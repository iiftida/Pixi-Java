package Main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.*;

public class GameClient  extends JFrame {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;


    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public GameClient(){

        try {
            System.out.println("Sending Request");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Connection Done ");


            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(in);
            out = new PrintWriter(socket.getOutputStream());
            CreateGUI();
            handleEvents();
            startReading();
            startWriting();


        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }
    private void CreateGUI(){

        JPanel win = new JPanel();
        win.setSize(700,480);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageArea.setRows(15);
         messageArea.setColumns(15);
        win.setLayout(new BorderLayout());

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        win.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        win.add(jScrollPane, BorderLayout.CENTER);
        win.add(messageInput, BorderLayout.SOUTH);


        JFrame window = new JFrame("Pixy Green");
        JPanel firstPanel = new GamePanel();
        JPanel secondPanel = win;
        window.setLayout(new FlowLayout());
        window.add(firstPanel);
        window.add(secondPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                firstPanel.requestFocus();
                firstPanel.requestFocusInWindow();
                firstPanel.setRequestFocusEnabled(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                firstPanel.requestFocus();
                firstPanel.requestFocusInWindow();
                firstPanel.setRequestFocusEnabled(true);

            }
        });
    }

    public void startReading(){
        Runnable r1 =()->{
            System.out.println("Reader Started");
            try {
                while(true){

                    String msg =  br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Server Terminated gthe Chat");
//                        JOptionPane.showMessageDialog(this, "Server Terminated The Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    messageArea.append("Server : "+ msg+"\n");
                    System.out.println("Server : " + msg);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        Runnable r2 =()->{
            System.out.println("writiting Started");
            try {
                while(!socket.isClosed()){

                    InputStreamReader in  = new InputStreamReader(System.in);
                    BufferedReader br1 = new BufferedReader(in);
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args){
        System.out.println("This is Client");
new GameClient();





    }
}
