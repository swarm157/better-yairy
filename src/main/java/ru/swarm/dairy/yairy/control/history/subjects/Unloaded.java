package ru.swarm.dairy.yairy.control.history.subjects;

import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.book.Book;

public class Unloaded implements HistorySubject{
    Book book;

    public Unloaded(Book book) {
        this.book = book;
    }

    @Override
    public void undo() {
        DataProxy.getBook().insert(book);
    }

    @Override
    public void make() {
        DataProxy.getBook().remove(book);
    }
}
