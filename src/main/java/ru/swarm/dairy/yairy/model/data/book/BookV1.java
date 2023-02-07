package ru.swarm.dairy.yairy.model.data.book;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.swarm.dairy.yairy.model.DataProxy;
import ru.swarm.dairy.yairy.model.data.page.Page;
import ru.swarm.dairy.yairy.model.data.page.PageV1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.System.getProperty;


public class BookV1 implements Book{


    public String creator;
    public String name;
    public long creationDate;
    public transient ArrayList<Page> pages = new ArrayList<>();
    public String pagesGson;
    public String lastEditor;

    public long version = 1;
    public long textVersion;

    public BookV1(String name) {
        this.name = name;
        pages = new ArrayList<>();
        creator = getProperty("user.name");
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();

    }

    public BookV1(String creator, String name, long creationDate, ArrayList<Page> pages, String lastEditor, long version, long textVersion) {
        this.creator = creator;
        this.name = name;
        this.creationDate = creationDate;
        for (Page page : pages) {
            this.pages.add((PageV1) page);
        }
        this.lastEditor = lastEditor;
        this.version = version;
        this.textVersion = textVersion;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public long getTextVersion() {
        return textVersion;
    }

    @Override
    public String getLastEditorName() {
        return lastEditor;
    }

    @Override
    public Book downgrade() {
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();
        return null;
    }

    @Override
    public Book upgrade() {
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();
        return null;
    }

    @Override
    public boolean isNewest() {
        return true;
    }

    @Override
    public boolean isOldest() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();
        this.name = name;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public long getCreationDate() {
        return creationDate;
    }

    @Override
    public String getUID() {
        return creator+creationDate;
    }


    @Override
    public boolean contain(String text, String password) {
        for (Page page : pages) {
            if (page.contain(text, password)) return true;
        }
        return false;
    }


    @Override
    public void update(Page page) {
        for (var p2 : getPages()) {
            p2.update(page);
        }
    }

    @Override
    public void rename(String source, String destination) {
        for (Page page : pages) {
            page.rename(source, destination);
        }
    }

    @Override
    public void remove(String text, String password) {
        for (Page page : pages) {
            page.remove(text, password);
        }
    }

    @Override
    public void insert(Book book) {
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();

        for (Page page : book.getPages()) {
            DataProxy.log(page);
            pages.add((PageV1) page);
        }
    }

    @Override
    public void insert(Page page) {
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();

        pages.add((PageV1) page);
    }

    @Override
    public void update(Book book) {
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();
        for (var p1 : book.getPages()) {
            for (var p2 : getPages()) {
                p2.update(p1);
            }
        }
    }

    @Override
    public int getSize() {
        return pages.size();
    }

    @Override
    public void remove(Page page) {
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();
        pages.remove(page);
    }

    @Override
    public void remove(Book book) {
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();
        pages.removeAll(book.getPages());
    }

    @Override
    public List<Page> getPages() {
        if (pages==null) pages = new ArrayList<>();
        return pages;
    }

    @Override
    public void prepareToGson() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        pagesGson = "";

        for (Page page : pages) {
            pagesGson+=gson.toJson((PageV1)page)+"DDDDDDDD";

        }
    }

    @Override
    public void prepareAfterGson() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        pages = new ArrayList<>();
        for (String page : pagesGson.split("DDDDDDDD")) {
            pages.add(gson.fromJson(page, PageV1.class));
        }
    }



    @Override
    public String toString() {
        return "BookV1{" +
                "creator='" + creator + '\'' +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", pages=" + pages +
                ", lastEditor='" + lastEditor + '\'' +
                ", version=" + version +
                ", textVersion=" + textVersion +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookV1)) return false;
        BookV1 bookV1 = (BookV1) o;
        return getCreationDate() == bookV1.getCreationDate() && getVersion() == bookV1.getVersion() && getTextVersion() == bookV1.getTextVersion() && getCreator().equals(bookV1.getCreator()) && getName().equals(bookV1.getName()) && Objects.equals(getPages(), bookV1.getPages()) && Objects.equals(pagesGson, bookV1.pagesGson) && lastEditor.equals(bookV1.lastEditor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCreator(), getName(), getCreationDate(), lastEditor, getVersion(), getTextVersion());
    }
}
