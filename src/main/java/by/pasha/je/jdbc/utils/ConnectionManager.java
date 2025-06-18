package by.pasha.je.jdbc.utils;

import java.lang.reflect.Proxy;
import java.net.ProxySelector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.lang.reflect.Proxy;

public final class ConnectionManager {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final int DEFAULT_SIZE_POOL = 10;
    private static final String SIZE_KEY = "db.size.pool";
    private static BlockingQueue<Connection> pool;

    static {
        poolConectionProxy();
    }

    private static void poolConectionProxy() {
        int size = PropertiesUtil.get(SIZE_KEY) == null ? DEFAULT_SIZE_POOL : Integer.parseInt(PropertiesUtil.get(SIZE_KEY));
        pool = new ArrayBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            var newProxy = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(),
                    new Class<?>[]{Connection.class},
                    (proxy, method, args) -> method.equals("close") ? pool.add((Connection) proxy) : method.invoke(connection, args));

            pool.add(newProxy);
        }
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection open() {
        try {

            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionManager() {
    }
}
