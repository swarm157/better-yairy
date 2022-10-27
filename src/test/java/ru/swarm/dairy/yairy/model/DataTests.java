package ru.swarm.dairy.yairy.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.swarm.dairy.yairy.model.data.book.Book;
import ru.swarm.dairy.yairy.model.data.book.BookV1;
import ru.swarm.dairy.yairy.model.data.page.PageV1;
import ru.swarm.dairy.yairy.model.saves.Save;
import ru.swarm.dairy.yairy.model.saves.SaveV1;

import java.io.File;


public class DataTests {
    private static GsonBuilder builder = new GsonBuilder();
    private static Gson gson = builder.create();
    @Test()
    public void nonNullPageSerialization() {
        Assertions.assertNotEquals("null", gson.toJson(new PageV1()), "Не могу сереализовать страницу");
    }
    @Test()
    public void nonNullPageDeserialization() {
        Assertions.assertNotNull(gson.fromJson(gson.toJson(new PageV1()), PageV1.class), "Не могу десерелиазовать страницу");
    }
    @Test()
    public void nonNullBookSerialization() {
        Book book = new BookV1("the yet one strange book");
        book.getPages().add(new PageV1());
        book.getPages().add(new PageV1());
        book.getPages().add(new PageV1());
        book.getPages().get(0).setText("123123", "какая-то страничка");
        Assertions.assertNotEquals("null", gson.toJson(book), "Не могу сереализовать книгу");

    }
    @Test()
    public void nonNullBookDeserialization() {
        Book book = new BookV1("the yet one strange book");
        book.getPages().add(new PageV1());
        book.getPages().add(new PageV1());
        book.getPages().add(new PageV1());
        book.getPages().get(0).setText("123123", "какая-то страничка");
        book.prepareToGson();
        book = gson.fromJson(gson.toJson(book), BookV1.class);
        book.prepareAfterGson();
        Assertions.assertNotNull(book, "Не могу десериализовать книгу");
        Assertions.assertNotNull(book.getPages().get(0), "Не могу десереализовать страницу книги");
    }
    @Test()
    public void saving() {
        Save save = new SaveV1();
        Assertions.assertEquals(true ,save.save(new File("somefile.save"), new BookV1("somebook")), "Не работает система сохранений, сохранение");
    }
    @Test()
    public void loading() {
        Save save = new SaveV1();
        Assertions.assertNotNull(save.load(new File("somefile.save")), "Не работает система сохранений, загрузка");
    }
}
