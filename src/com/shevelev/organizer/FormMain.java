package com.shevelev.organizer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by denis on 08.04.17.
 */
public class FormMain {
    private JPanel mainPanel;
    private JTable table1;
    private JButton addButtonFile;
    private JButton addButtonTag;
    private JButton filtrationButton;
    private JTextField textField1;
    private JButton retrieveButton;
    private JTextField textTag2;
    private JTextField nameTxt;
    private JTextField tagTxt;
    private JButton searchButton;
    String absolutePath;
    public static final String URL = "jdbc:mysql://localhost:3306/testtask";
    public static final String USERNAME = "root";
    public static final String USERPASSWORD = "dshevelevr1994";

    public FormMain() {
        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel defaultTableModel = new DBorganizer().getData();
                table1.setModel(defaultTableModel);
            }
        });
        addButtonFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sql = "INSERT INTO organizer(nameFile,tag,bodyFile) VALUES(?,?,?)";
                    Connection conn = DriverManager.getConnection(URL, USERNAME, USERPASSWORD);
                    PreparedStatement ps = conn.prepareStatement(sql);
                    InputStream is = new FileInputStream(new File(absolutePath));
                    ps.setString(1, nameTxt.getText());
                    ps.setString(2, tagTxt.getText());
                    ps.setString(3, String.valueOf(is));
                    ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "File Successfully Add");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "The File is missing");
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = jFileChooser.showSaveDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    File selectedFile = jFileChooser.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();
                    absolutePath = path;
                }
                else if(result == JFileChooser.CANCEL_OPTION){
                }
            }
        });
        filtrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableRowSorter sorter = new TableRowSorter(table1.getModel());
                table1.setRowSorter(sorter);
                RowFilter rowFilter = null;
                try {
                    rowFilter = RowFilter.regexFilter(textField1.getText(), 2);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                sorter.setRowFilter(rowFilter);
            }
        });
        addButtonTag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = table1.getSelectedRow();
                String id = table1.getValueAt(index, 0).toString();
                if (new DBorganizer().update(id,textTag2.getText())) {
                    JOptionPane.showMessageDialog(null, "Successfully Updated");
                    textTag2.setText("");

                } else {
                    JOptionPane.showMessageDialog(null, "Not Updated");
                }
            }
        });
    }

    public static void main(String[] args)  {
        JFrame jFrame = new JFrame("Test Task");
        jFrame.setBounds(100, 100, 828, 600);
        jFrame.setContentPane(new FormMain().mainPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
