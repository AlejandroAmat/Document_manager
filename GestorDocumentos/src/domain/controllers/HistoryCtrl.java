package domain.controllers;

import java.util.LinkedList;

public class HistoryCtrl {

    private LinkedList<String> history;

//--------------------------------------------------------

    public HistoryCtrl() {
        this.history = new LinkedList<>();
    }
    public HistoryCtrl(LinkedList<String> h) {
        this.history = h;
    }

    public LinkedList<String> getHistory() {
       return history;
    }

    public void addStatement(String statement) {
        history.add(statement);
    }

    public void clearHistory() {
        history.clear();
    }

}
