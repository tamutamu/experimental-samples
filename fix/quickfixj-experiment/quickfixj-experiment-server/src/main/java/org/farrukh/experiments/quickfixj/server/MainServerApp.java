package org.farrukh.experiments.quickfixj.server;

import org.farrukh.experiments.quickfixj.shared.FixSettingsProvider;
import org.farrukh.experiments.quickfixj.shared.exception.FixException;

import quickfix.Application;
import quickfix.CompositeLogFactory;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.SLF4JLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;

/**
 * Main Fix Server App class.
 *
 */
public class MainServerApp {

    private static final String CONFIG_FILE_NAME = "config.properties";

    private SocketAcceptor acceptor;

    public MainServerApp() {
        try {
            Application application = new FixServerApplication();
            SessionSettings settings = new FixSettingsProvider().loadSettings(CONFIG_FILE_NAME);
            MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
            LogFactory logFactory = new CompositeLogFactory(new LogFactory[]{new SLF4JLogFactory(settings), new FileLogFactory(settings)});
            MessageFactory messageFactory = new DefaultMessageFactory();
            acceptor = new SocketAcceptor(application, messageStoreFactory, settings, logFactory, messageFactory);
        } catch (ConfigError e) {
            throw new FixException(e);
        }
    }

    public void start() {
        try {
            acceptor.start();
        } catch (RuntimeError | ConfigError e) {
            throw new FixException(e);
        }
    }
    
    public static void main(String[] args) {
        new MainServerApp().start();
    }

}
