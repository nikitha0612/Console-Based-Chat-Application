import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.BufferOverflowException;

import javax.imageio.ImageIO;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame {

    Socket client_socket;
    BufferedReader br;
    PrintWriter pw;

    //Components  - GUI
    private Font textfont = new Font("Courier", Font.PLAIN, 18);
    private JLabel heading = new JLabel("Client");
    private JTextArea textarea = new JTextArea();
    private JTextField textinput = new JTextField();
    public Client() {
        try {
            System.out.println("Sending request to Server");
            client_socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connnection established with Server");
            br = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            pw = new PrintWriter(client_socket.getOutputStream());
            createChatWindow();
            handleEvents();
            readMessages();
            //writeMessages();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not establish connection with Server");
        }

    }
    


    private void handleEvents() {

        textinput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode() == 10){
                   //enter button
                   String msgtyped = textinput.getText();
                   textarea.append("Me : " + msgtyped + "\n");
                   pw.println(msgtyped);
                   pw.flush();
                   textinput.setText("");
                   textinput.requestFocus();

                }
                
            }
        });
            

    }




    private void createChatWindow() {
        //GUI Box Layout
        this.setTitle("Client Messenger");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //GUI Box inside layout
        heading.setFont(textfont);
        textarea.setFont(textfont);
        textarea.setBackground(Color.magenta);
        textarea.setBackground(Color.LIGHT_GRAY);
        textinput.setFont(textfont);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        textinput.setHorizontalAlignment(SwingConstants.CENTER);
        textarea.setEditable(false);
        this.setLayout(new BorderLayout());

        
        //adding components to jframe
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jscrollpane = new JScrollPane(textarea);
        this.add(jscrollpane, BorderLayout.CENTER);
        this.add(textinput, BorderLayout.SOUTH);
        this.setVisible(true);
    }


    public static void main(String[] args) {
        System.out.println("Starting Client's Chat Window");
        new Client();

    }

    private void writeMessages() {
        // thread for writing messages and sending
        Runnable write = () -> {
            System.out.println("Starting Message Writer");
            try {
                while (!client_socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader((System.in)));
                    String msg_content = br1.readLine();
                    pw.println(msg_content);
                    pw.flush();
                    if (msg_content.equals("exit")) {
                        client_socket.close();
                        break;
                    }

                }
                System.out.println("Connection closed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        // start the thread
        new Thread(write).start();
    }

    private void readMessages() {
        // thread for reading messages

        Runnable read = () -> {
            System.out.println("Starting Message Reader");

            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Chat is terminated by Server , Bye !");
                        JOptionPane.showMessageDialog(this, "Chat is terminated by Server");
                        textinput.setEnabled(false);
                        client_socket.close();
                        break;
                    }
                  //  System.out.println("Server : " + msg);
                  textarea.append("Server : " + msg + "\n");
                }
            } catch (Exception e) {
                System.out.println("Connection Closed");
            }
        };
        // start the thread
        new Thread(read).start();
    }

}
