package lt.ehu.student.moneytrackerbeta.listener;

import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;
import lt.ehu.student.moneytrackerbeta.connection.ConnectionPool;
import lt.ehu.student.moneytrackerbeta.exception.ConnectionPoolException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

@WebListener
public class ConnectionPoolListener implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger(ConnectionPoolListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing database connection pool");
        try {
            ConnectionPool.getInstance().initializePool();
            // Store the initialized pool in ServletContext for verification
            sce.getServletContext().setAttribute("connectionPoolInitialized", true);
        } catch (Exception e) {
            logger.error("Failed to initialize connection pool", e);
            throw new ConnectionPoolException("Failed to initialize connection pool", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            logger.info("Shutting down database connection pool");
            ConnectionPool.getInstance().closePool();
            // Deregister JDBC driver
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                if (driver.getClass().getName().equals("org.postgresql.Driver")) {
                    DriverManager.deregisterDriver(driver);
                    logger.info("Deregistered JDBC driver");
                }
            }
        } catch (Exception e) {
            logger.error("Error during shutdown", e);
        }
    }
} 