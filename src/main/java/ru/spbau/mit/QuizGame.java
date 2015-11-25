package ru.spbau.mit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class QuizGame implements Game {
    private Integer delayUntilNextLetter;
    private Integer maxLettersToOpen = 2;
    private String dictionaryFilename;
    private List<String> questions;
    private List<String> answers;
    private int curQuestion = -1;
    private GameServer gameServer;
    private Thread game;

    private static final String START_CMD = "!start";
    private static final String STOP_CMD = "!stop";
    private static final String NEW_ROUND_FST = "New round started: ";
    private static final String NEW_ROUND_SND = " (";
    private static final String NEW_ROUND_THIRD = " letters)";
    private static final String HINT = "Current prefix is ";
    private static final String WRONG_TRY = "Wrong try";
    private static final String PLAYERS_LOST = "Nobody guessed, the word was ";
    private static final String PLAYERS_WON = "The winner is ";
    private static final String STOP = "Game has been stopped by ";


    public void setDelayUntilNextLetter(Integer delay) {
        delayUntilNextLetter = delay;
    }

    public void setMaxLettersToOpen(Integer cnt) {
        maxLettersToOpen = cnt;
    }

    public void setDictionaryFilename(String path) {
        dictionaryFilename = path;
        initDictionary(path);
    }

    private void initDictionary(String path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(String line: lines) {
            String[] questionAndAnswer = line.split(";");
            questions.add(questionAndAnswer[0]);
            answers.add(questionAndAnswer[1]);
            curQuestion = 0;
        }
    }

    public QuizGame(GameServer server) {
        //throw new UnsupportedOperationException("TODO: implement");
        gameServer = server;
        game = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    int lettersOpened = 0;
                    boolean isRoundOver = false;

                    synchronized(QuizGame.this) {
                        gameServer.broadcast(NEW_ROUND_FST +
                                             questions.get(curQuestion) +
                                             NEW_ROUND_SND +
                                             Integer.toString(answers.get(curQuestion).length()) +
                                             NEW_ROUND_THIRD);
                    }
                    while(!isRoundOver && lettersOpened < maxLettersToOpen) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(delayUntilNextLetter);
                        } catch (InterruptedException e) {
                            isRoundOver = true;
                            break;
                        }
                        synchronized(this) {
                            gameServer.broadcast(HINT + answers.get(curQuestion).substring(0, lettersOpened + 1));
                        }
                        lettersOpened++;
                    }

                    if(!isRoundOver) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(delayUntilNextLetter);
                        } catch (InterruptedException e) {
                            continue;
                        }
                    }

                    synchronized(QuizGame.this) {
                        gameServer.broadcast(PLAYERS_LOST + answers.get(curQuestion));
                        curQuestion = (curQuestion + 1) % questions.size();
                    }
                }
            }
        });
    }

    @Override
    public void onPlayerConnected(String id) {
        //throw new UnsupportedOperationException("TODO: implement");
    }

    @Override
    public void onPlayerSentMsg(String id, String msg) {
        //throw new UnsupportedOperationException("TODO: implement");
        synchronized (this) {
            if (msg.equals(START_CMD)) {
                game.start();
            } else if (msg.equals(STOP_CMD)) {
                game.interrupt();
                gameServer.broadcast(STOP + id);
            } else {
                if(msg.equals(answers.get(curQuestion))) {
                    gameServer.broadcast(PLAYERS_WON + id);
                    game.interrupt();
                }
                else {
                    gameServer.sendTo(id, WRONG_TRY);
                }
            }
        }
    }
}
