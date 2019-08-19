package com.jagadeesh.moneytransfer.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class responsible for initialization and start of Jetty with Jersey ServletContainer
 * default port 9090
 * default URL: http://localhost:9090
 */
public class JettyApplicationServer {

    private static final Logger log = LoggerFactory.getLogger(JettyApplicationServer.class);
    private static final String CONTEXT_PATH = "/*";
    private static final int SERVER_PORT = 9090;

    private static Server getApplicationServer() {
        JerseyApplication config = new JerseyApplication();
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(SERVER_PORT);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, CONTEXT_PATH);
        return server;
    }

    public static void startServer() {
        Server server = getApplicationServer();
        try {
            server.start();
            server.join();
        } catch (Exception exception) {
            log.error("Server exception: " + exception.getClass() + " " + exception.getMessage());
            System.exit(1);
        } finally {
            server.destroy();
        }
    }
}
