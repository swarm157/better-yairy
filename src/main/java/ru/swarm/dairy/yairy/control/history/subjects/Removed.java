package ru.swarm.dairy.yairy.control.history.subjects;

import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.page.Page;

public class Removed implements HistorySubject{
    Page page;

    public Removed(Page page) {
        this.page = page;
    }

    @Override
    public void undo() {
        DataProxy.getBook().getPages().add(page);
    }

    @Override
    public void make() {
        DataProxy.getBook().getPages().remove(page);
    }
}
