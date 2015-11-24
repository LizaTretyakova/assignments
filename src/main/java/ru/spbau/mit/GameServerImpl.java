package ru.spbau.mit;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class GameServerImpl implements GameServer {
    private static Integer numberConnected = 0;
    private static final int timeout = 1000;
    HashMap<String, Connection> activeConnections;
    Game plugin;

    private Integer detectInteger(String src) throws NumberFormatException {
        Integer res = Integer.parseInt(src);
        return res;
    }

    public GameServerImpl(String gameClassName, Properties properties) {
        //throw new UnsupportedOperationException("TODO: implement");
        try {
            activeConnections = new HashMap<>();

            Class<?> pluginClass = Class.forName(gameClassName);
            plugin = (Game) pluginClass.getConstructor(GameServer.class).newInstance(this);

            Enumeration names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String property = (String) names.nextElement();
                String setterName = "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
                try {
                    Integer propertyValue = detectInteger(properties.getProperty(property));
                    pluginClass.getMethod(setterName, Integer.TYPE).invoke(propertyValue);
                }
                catch (NumberFormatException e) {
                    pluginClass.getMethod(setterName, String.class).invoke(properties.getProperty(property));
                }
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void accept(final Connection connection) {
        //throw new UnsupportedOperationException("TODO: implement");
        final String id;
        synchronized (GameServerImpl.this) {
            id = Integer.toString(numberConnected);
            activeConnections.put(id, connection);
            numberConnected++;
        }
        sendTo(id, id);
        plugin.onPlayerConnected(id);

        Thread processConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!connection.isClosed()) {
                    try {
                        String message = connection.receive(timeout);
                        if(message != null) {
                            plugin.onPlayerSentMsg(id, message);
                        }
                    } catch (InterruptedException e) {
                        //return;
                        break;
                    }
                }
            }
        });
        processConnection.start();
    }

    @Override
    public void broadcast(String message) {
        //throw new UnsupportedOperationException("TODO: implement");
        //synchronized (this) {
        synchronized (this) {
            for(Connection connection: activeConnections.values()) {
                connection.send(message);
            }
        }
    }

    @Override
    public void sendTo(String id, String message) {
        //throw new UnsupportedOperationException("TODO: implement");
        synchronized (this) {
            activeConnections.get(id).send(message);
        }
    }
}
