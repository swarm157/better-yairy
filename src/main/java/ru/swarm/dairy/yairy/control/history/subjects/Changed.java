package ru.swarm.dairy.yairy.control.history.subjects;

import ru.swarm.dairy.yairy.model.DataProxy;
import static ru.swarm.dairy.yairy.model.DataProxy.*;

public class Changed implements HistorySubject{

    String before, after;
    String pass;
    int pageNumber;

    public Changed(String before, String after, int pageNumber, String pass) {
        this.before     = before;
        this.after      = after;
        this.pageNumber = pageNumber;
        this.pass       = pass;
    }

    @Override
    public void undo() {
        DataProxy.getBook().getPages().get(pageNumber).setText(pass, before);
    }

    @Override
    public void make() {
        DataProxy.getBook().getPages().get(pageNumber).setText(pass, after);
    }
}
