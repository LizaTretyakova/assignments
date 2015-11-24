package ru.spbau.mit;

import java.util.*;


public class SumTwoNumbersGame implements Game {
    private Random random = new Random(42);
    private Integer a, b;
    private final GameServer gameServer;


    public SumTwoNumbersGame(GameServer server) {
        //throw new UnsupportedOperationException("TODO: implement");
        a = 1 + random.nextInt(100);
        b = 1 + random.nextInt(100);
        gameServer = server;
    }

    @Override
    public void onPlayerConnected(String id) {
        //throw new UnsupportedOperationException("TODO: implement");
        String message = Integer.toString(a) + " " + Integer.toString(b);
        gameServer.sendTo(id, message);
    }

    @Override
    public void onPlayerSentMsg(String id, String msg) {
        //throw new UnsupportedOperationException("TODO: implement");
        synchronized (this) {
            try {
                Integer answer = Integer.parseInt(msg);
                if (answer == a + b) {
                    gameServer.sendTo(id, "Right");
                    gameServer.broadcast(id + " won");

                    a = 1 + random.nextInt(100);
                    b = 1 + random.nextInt(100);

                    String message = Integer.toString(a) + " " + Integer.toString(b);
                    gameServer.broadcast(message);
                }
                else {
                    gameServer.sendTo(id, "Wrong");
                }
            } catch (NumberFormatException e) {
                return;
            }
        }
    }
}
