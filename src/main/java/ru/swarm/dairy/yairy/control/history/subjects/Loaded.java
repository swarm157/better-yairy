package ru.swarm.dairy.yairy.control.history.subjects;

import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.book.Book;

public class Loaded implements HistorySubject{
    Book book;

    public Loaded(Book book) {
        this.book = book;
    }

    @Override
    public void undo() {
        DataProxy.getBook().remove(book);
    }

    @Override
    public void make() {
        DataProxy.getBook().insert(book);
    }
}
