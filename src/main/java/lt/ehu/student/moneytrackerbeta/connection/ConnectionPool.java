package lt.ehu.student.moneytrackerbeta.connection;

import lt.ehu.student.moneytrackerbeta.exception.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ResourceBundle;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class.getName());
    private static final String DB_PROPERTY_FILE = "database";
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_POOL_SIZE = "db.poolsize";
    
    private BlockingQueue<Connection> availableConnections;
    private BlockingQueue<Connection> usedConnections;
    private String url;
    private String user;
    private String password;
    private int poolSize;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.fatal("PostgreSQL JDBC Driver not found", e);
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    private ConnectionPool() {
        ResourceBundle bundle = ResourceBundle.getBundle(DB_PROPERTY_FILE);
        this.url = bundle.getString(DB_URL);
        this.user = bundle.getString(DB_USER);
        this.password = bundle.getString(DB_PASSWORD);
        this.poolSize = Integer.parseInt(bundle.getString(DB_POOL_SIZE));
    }

    private static class ConnectionPoolHolder {
        private static final ConnectionPool instance = new ConnectionPool(); // Use JVM's class initialization guarantees for thread safety
    }

    public static ConnectionPool getInstance() {
        return ConnectionPoolHolder.instance;
    }

    public void initializePool() throws ConnectionPoolException {
        logger.info("Initializing connection pool");
        try {
            availableConnections = new LinkedBlockingQueue<>(poolSize);
            usedConnections = new LinkedBlockingQueue<>(poolSize);
            
            for (int i = 0; i < poolSize; i++) {
                Connection connection = createConnection();
                availableConnections.offer(connection);
            }
        } catch (SQLException e) {
            logger.error("Error initializing connection pool", e);
            throw new ConnectionPoolException("Failed to initialize connection pool", e);
        }
        logger.info("Connection pool initialized with {} connections", poolSize);
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() throws ConnectionPoolException {
        try {
            Connection connection = availableConnections.take();
            usedConnections.offer(connection);
            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConnectionPoolException("Error getting connection from pool", e);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null && usedConnections.remove(connection)) {
            availableConnections.offer(connection);
        }
    }

    public void closePool() {
        logger.info("Closing connection pool");
        closeConnections(usedConnections);
        closeConnections(availableConnections);
        logger.info("Connection pool closed");
    }

    private void closeConnections(BlockingQueue<Connection> connections) {
        Connection connection;
        while ((connection = connections.poll()) != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        }
    }
}
