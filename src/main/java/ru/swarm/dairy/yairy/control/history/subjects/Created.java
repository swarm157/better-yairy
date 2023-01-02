package ru.swarm.dairy.yairy.control.history.subjects;

import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.page.Page;

public class Created implements HistorySubject{
    Page page;

    public Created(Page page) {
        this.page = page;
    }

    @Override
    public void undo() {
        DataProxy.getBook().getPages().remove(page);
    }

    @Override
    public void make() {
        DataProxy.getBook().getPages().add(page);
    }
}
