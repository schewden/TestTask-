package com.shevelev.organizer;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

import static java.sql.DriverManager.getConnection;

/**
 * Created by denis on 08.04.17.
 */
public class DBorganizer {
    public static final String URL = "jdbc:mysql://localhost:3306/testtask";
    public static final String USERNAME = "root";
    public static final String USERPASSWORD = "dshevelevr1994";

    public DefaultTableModel getData() {
        DefaultTableModel dm = new DefaultTableModel();
        dm.addColumn("Id");
        dm.addColumn("NameFile");
        dm.addColumn("Tag");
        dm.addColumn("BodyFile");
        String sql = "SELECT * FROM organizer";
        try {
            Connection con = getConnection(URL, USERNAME, USERPASSWORD);
            Statement s = con.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString(1);
                String nameFile = rs.getString(2);
                String tag = rs.getString(3);
                String bodyFile = rs.getString(4);
                dm.addRow(new String[]{id, nameFile, tag, bodyFile});
            }
            return dm;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean update(String id ,String tag) {
        String sql = "UPDATE organizer SET tag = CONCAT(tag, '" + tag + "') WHERE id='" + id + "'";
        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, USERPASSWORD);
            Statement s = con.prepareStatement(sql);
            s.execute(sql);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
