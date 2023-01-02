package ru.swarm.dairy.yairy.control.history.subjects;

import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.book.Book;

public class Updated implements HistorySubject{

    Book book;

    public Updated(Book book) {
        this.book = book;
    }

    @Override
    public void undo() {
        DataProxy.log("unimplemented method Updated.undo() has been called");
    }

    @Override
    public void make() {
        DataProxy.getBook().update(book);
    }
}
