package ru.swarm.dairy.yairy.model.saves;

import ru.swarm.dairy.yairy.model.data.book.Book;

import java.io.File;

public interface Save {
    long getVersion();
    boolean isNewest();
    boolean isOldest();
    Save upgrade();
    Save downgrade();
    boolean save(File file, Book book);
    Book load(File file);
}
