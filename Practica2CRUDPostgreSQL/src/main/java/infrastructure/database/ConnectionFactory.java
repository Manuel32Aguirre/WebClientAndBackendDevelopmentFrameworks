package infrastructure.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final Properties props = new Properties();
    static{
    try(InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")){
        if(in == null){
            throw new RuntimeException("No se encontró el archivo db.properties");
        }
        props.load(in);

        Class.forName(props.getProperty("db.driver"));
    }catch(IOException | ClassNotFoundException e){
        throw new RuntimeException("Error cargando configuración de DB" + e);
    }
}


    public static Connection get() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }

}

