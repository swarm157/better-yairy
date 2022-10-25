package ru.swarm.dairy.yairy.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.swarm.dairy.yairy.model.data.book.Book;
import ru.swarm.dairy.yairy.model.data.book.BookV1;
import ru.swarm.dairy.yairy.model.data.page.PageV1;
import ru.swarm.dairy.yairy.model.saves.Save;
import ru.swarm.dairy.yairy.model.saves.SaveV1;

import java.io.*;
import java.util.Date;
import java.util.List;

public class DataProxy {
    private static Save save = new SaveV1();
    private static Book book;
    private final static String DEFAULT_SAVE_NAME = "yairy.save";
    static {
        load();
    }
    public static void save() {
        var name = "backups/backup"+System.currentTimeMillis()+".save";
        save.save(new File(name), book);
        System.out.println(System.currentTimeMillis() +" INFO: "+name+" has been saved");
        save.save(new File(DEFAULT_SAVE_NAME), book);
        System.out.println(System.currentTimeMillis() +" INFO: "+DEFAULT_SAVE_NAME+" has been saved");
    }
    public static boolean load() {
        book = save.load(new File(DEFAULT_SAVE_NAME));
        if (book==null) {
            book = new BookV1("");
            return false;
        }
        return true;
    }
    public static boolean load(String name) {
        book = save.load(new File(name));
        if (book==null) {
            book = new BookV1("");
            return false;
        }
        return true;
    }
    public static boolean insert(String name) {
        try {
            String text = new BufferedInputStream(new FileInputStream(name)).readAllBytes().toString();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            try {
                book.insert(gson.fromJson(text, BookV1.class));
            } catch (Exception E) {
                try {
                    book.insert(gson.fromJson(text, PageV1.class));
                } catch (Exception E2) {
                    System.err.println(System.currentTimeMillis() +" ERROR: "+name+" couldn't be inserted\n"+E2.getLocalizedMessage());
                    return false;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static boolean remove(String name) {
        try {
            String text = new BufferedInputStream(new FileInputStream(name)).readAllBytes().toString();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            try {
                book.remove(gson.fromJson(text, BookV1.class));
            } catch (Exception E) {
                try {
                    book.remove(gson.fromJson(text, PageV1.class));
                } catch (Exception E2) {
                    System.err.println(System.currentTimeMillis() +" ERROR: "+name+" couldn't be removed\n"+E2.getLocalizedMessage());
                    return false;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static Book getBook() {
        return book;
    }
    public static Book getBook(List<Integer> indexes, String name) {
        Book b = new BookV1(name);
        for (Integer i : indexes) {
            b.insert(book.getPages().get(i));
        }
        return b;
    }
}
