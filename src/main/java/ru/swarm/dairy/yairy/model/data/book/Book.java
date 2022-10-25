package ru.swarm.dairy.yairy.model.data.book;

import ru.swarm.dairy.yairy.model.data.CommonMethods;
import ru.swarm.dairy.yairy.model.data.page.Page;

import java.util.List;

public interface Book extends CommonMethods {
    Book downgrade();
    Book upgrade();
    void insert(Book book);
    void insert(Page page);
    void update(Book book);
    int getSize();
    void remove(Page page);
    void remove(Book book);
    List<Page> getPages();
    void prepareToGson();
    void prepareAfterGson();
}
