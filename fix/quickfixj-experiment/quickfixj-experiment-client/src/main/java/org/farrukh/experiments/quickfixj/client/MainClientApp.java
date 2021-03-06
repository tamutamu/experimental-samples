package org.farrukh.experiments.quickfixj.client;

import java.util.concurrent.CountDownLatch;

import org.farrukh.experiments.quickfixj.shared.FixSettingsProvider;
import org.farrukh.experiments.quickfixj.shared.exception.FixException;

import quickfix.CompositeLogFactory;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.RuntimeError;
import quickfix.SLF4JLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

/**
 * Hello world!
 *
 */
public class MainClientApp {
    
    private static final String CONFIG_FILE_NAME = "config.properties";

    private static final CountDownLatch shutdown_latch = new CountDownLatch(1);
    
    private final SocketInitiator initiator;

    public MainClientApp() {
        SessionSettings settings = new FixSettingsProvider().loadSettings(CONFIG_FILE_NAME);
        LogFactory logFactory = new CompositeLogFactory(new LogFactory[] { new FileLogFactory(settings), new SLF4JLogFactory(settings) });
        try {
            initiator = new SocketInitiator(new FixClientApplication(), new FileStoreFactory(settings), settings, logFactory, new DefaultMessageFactory());
        } catch (ConfigError e) {
            throw new FixException(e);
        }
    }

    private void start() {
        try {
            initiator.start();
        } catch (RuntimeError | ConfigError e) {
            throw new FixException(e);
        }
    }

    public static void main(String[] args) {
        MainClientApp mainClientApp = new MainClientApp();
        mainClientApp.start();
        try {
            shutdown_latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
