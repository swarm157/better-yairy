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
         log(load());
    }
    public static byte save() {
        byte result = 0;
        var name = "backups/backup"+System.currentTimeMillis()+".save";
        if(save.save(new File(name), book)) result+=1;
        System.out.println(System.currentTimeMillis() +" INFO: "+name+" has been saved");
        if(save.save(new File(DEFAULT_SAVE_NAME), book)) result+=2;
        System.out.println(System.currentTimeMillis() +" INFO: "+DEFAULT_SAVE_NAME+" has been saved");
        return result;
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
            String text = new String(new BufferedInputStream(new FileInputStream(name)).readAllBytes());
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
            String text = new String(new BufferedInputStream(new FileInputStream(name)).readAllBytes());
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
    public static boolean save(List<Integer> indexes, String name, String fileName) {
        return save.save(new File(fileName+".book"), getBook(indexes, name));
    }
    public static boolean save(int index, String filename) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filename+".page"));
            out.write(gson.toJson(book.getPages().get(index)).getBytes());
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            System.err.println(System.currentTimeMillis() +" ERROR: "+"couldn't wrote to file"+filename+".page\n"+e.getLocalizedMessage());
            return false;
        }
    }
    public static void log(Object text) {
        System.out.println(System.currentTimeMillis() +" INFO: "+text);
    }
}
