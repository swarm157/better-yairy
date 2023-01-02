package ru.swarm.dairy.yairy.control.history.subjects;

import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.page.Page;

public class Inserted implements HistorySubject{

    Page page;
    int index;

    public Inserted(Page page, int index) {
        this.page = page;
        this.index = index;
    }

    @Override
    public void undo() {
        DataProxy.getBook().getPages().remove(index);
    }

    @Override
    public void make() {
        DataProxy.getBook().getPages().add(index, page);
    }
}
