package ru.swarm.dairy.yairy.control.history.subjects;

import com.google.gson.GsonBuilder;
import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.book.Book;

public class Updated implements HistorySubject{

    Book book;
    String before;

    public Updated(Book book) {
        this.book = book;
    }

    @Override
    public void undo() {
        GsonBuilder builder = new GsonBuilder();
        var pages = DataProxy.getBook().getPages();
        var b = builder.create().fromJson(before, Book.class);
        b.prepareAfterGson();
        pages.clear();
        pages.addAll(b.getPages());
        //DataProxy.log("unimplemented method Updated.undo() has been called");
    }

    @Override
    public void make() {
        book.prepareAfterGson();
        var b = DataProxy.getBook();
        b.prepareToGson();
        GsonBuilder builder = new GsonBuilder();
        before = builder.create().toJson(b);
        DataProxy.getBook().update(book);
    }
}
