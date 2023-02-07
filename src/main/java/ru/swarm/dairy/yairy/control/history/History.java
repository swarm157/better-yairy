package ru.swarm.dairy.yairy.control.history;

import ru.swarm.dairy.yairy.control.history.subjects.HistorySubject;

import java.util.ArrayList;

public class History {
    public static ArrayList<HistorySubject> getHistorySubjects() {
        return historySubjects;
    }

    private static ArrayList<HistorySubject> historySubjects = new ArrayList<>();

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        History.index = index;
    }

    private static int index = 0;
    private History() {}

    static public void undo() {
        if (index>0) {
            index--;
            historySubjects.get(index).undo();
        }
    }

    static public void make() {
        if (index+1<historySubjects.size()&&historySubjects.size()!=0) {
            index++;
            historySubjects.get(index).make();
        }
    }

    static public void make(HistorySubject historySubject) {
        if (index!=0)
            for(int i = historySubjects.size()-1; i>index; i--) {
                historySubjects.remove(i);
            }
        historySubjects.add(historySubject);
        historySubject.make();
        index++;
    }
    static public void reset() {
        historySubjects = new ArrayList<>();
    }

}
