package ru.spbau.mit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class QuizGame implements Game {
    private Integer delayUntilNextLetter;
    private Integer maxLettersToOpen;
    private String dictionaryFilename;
    private HashMap<String, String> dictionary;
    private int curQuestion = -1;
    private GameServer gameServer;

    private static final String START = "!start";
    private static final String STOP = "!stop";
    private static final String NEW_ROUND_FST = "New round started: ";
    private static final String NEW_ROUND_SND = " (";
    private static final String NEW_ROUND_THIRD = " letters)";
    private static final String HINT = "Current prefix is ";

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
            dictionary.put(questionAndAnswer[0], questionAndAnswer[1]);
            curQuestion = 0;
        }
    }

    public QuizGame(GameServer server) {
        //throw new UnsupportedOperationException("TODO: implement");
        gameServer = server;
/*
        Thread execution = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
*/
    }

    @Override
    public void onPlayerConnected(String id) {
        throw new UnsupportedOperationException("TODO: implement");
    }

    @Override
    public void onPlayerSentMsg(String id, String msg) {
        //throw new UnsupportedOperationException("TODO: implement");
    }
}
