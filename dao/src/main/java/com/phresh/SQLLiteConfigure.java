package com.phresh;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLLiteConfigure {

    private static final Logger logger = Logger.getLogger(SQLLiteConfigure.class.getName());

    //TODO move to properties file
    private String dbUsername = "username";
    private String dbPassword = "password1-";
    private String dbConnectionUrl = "jdbc:mysql://localhost:3306/phresh";

    public void createDatabase() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(dbConnectionUrl + "?user=" + dbUsername + "&password=" + dbPassword);
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate("create DATABASE phreshDB");
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error creating DB", ex);
        }
        statement.close();
        connection.close();
    }

    synchronized public SessionFactory buildSessionFactory() {
        if (dbConnectionUrl != null && dbUsername != null && dbPassword != null) {
            try {
                Configuration configuration = new Configuration();
                configuration.setProperty("hibernate.connection.url", dbConnectionUrl);
                configuration.setProperty("hibernate.connection.username", dbUsername);
                configuration.setProperty("hibernate.connection.password", dbPassword);
                configuration.setProperty("javax.persistence.validation.mode", "NONE");
                configuration.setProperty("hibernate.id.new_generator_mappings", "false");

                return configuration.buildSessionFactory();

            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                String message = "Initial SessionFactory creation failed. " + ex.getMessage();
                if (ex.getCause() != null) {
                    message = message + ". " + ex.getCause().getMessage();
                }
                logger.log(Level.SEVERE, message, ex); //NON-NLS
                throw ex;
            }
        } else {
            logger.log(Level.SEVERE, "Unable to open SessionFactory as required parameters are null");
            return null;
        }
    }
}
