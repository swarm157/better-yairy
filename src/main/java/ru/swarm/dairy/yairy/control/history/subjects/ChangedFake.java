package ru.swarm.dairy.yairy.control.history.subjects;

import ru.swarm.dairy.yairy.model.DataProxy;

public class ChangedFake implements HistorySubject {
    String before, after;
    int pageNumber;

    public ChangedFake(String before, String after, int pageNumber) {
        this.before     = before;
        this.after      = after;
        this.pageNumber = pageNumber;

    }

    @Override
    public void undo() {
        DataProxy.getBook().getPages().get(pageNumber).setFake(before);
    }

    @Override
    public void make() {
        DataProxy.getBook().getPages().get(pageNumber).setFake(after);
    }
}
