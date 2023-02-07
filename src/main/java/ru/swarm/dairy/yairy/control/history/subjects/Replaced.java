package ru.swarm.dairy.yairy.control.history.subjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.page.Page;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Replaced implements HistorySubject{

    String pass, from, to;
    int index;
    ArrayList<Pair<Integer, String>> before = new ArrayList<>();

    public Replaced(String pass, String from, String to, int index) {
        this.pass = pass;
        this.from = from;
        this.to = to;
        this.index = index;
    }

    public Replaced(String pass, String from, String to) {
        this.pass = pass;
        this.from = from;
        this.to = to;
        index = -1;
    }

    @Override
    public void undo() {
        var book = DataProxy.getBook();
        for (Pair<Integer, String> pair : before) {
            book.getPages().get(pair.getKey()).setText(pass, pair.getValue());
        }
    }

    @Override
    public void make() {
        var book = DataProxy.getBook();
        if (index<0) {
            int i = 0;
            for (Page page : book.getPages()) {
                String text = page.getText(pass);
                if (!text.equals(page.getFake())) {
                    before.add(new Pair<>(new Integer(i), text));
                    page.setText(pass, text.replace(from, to));
                }
                i++;
            }
        } else {
            var page = DataProxy.getBook().getPages().get(index);
            String text = page.getText(pass);
            if (!text.equals(page.getFake())) {
                before.add(new Pair<>(new Integer(index), text));
                page.setText(pass, text.replace(from, to));
            }
        }
    }
}
