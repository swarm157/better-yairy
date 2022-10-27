package ru.swarm.dairy.yairy.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.swarm.dairy.yairy.model.data.page.PageV1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataProxyTests {
    @Test()
    public void saving() {
        var r = DataProxy.save();
        Assertions.assertNotEquals(0, r, "не могу что-либо сделать");
        Assertions.assertTrue(new File("yairy.save").exists()&&new File("yairy.save").isFile(), "не могу обнаружить файл");
        Assertions.assertNotEquals(1, r, "был создан, только backup");
        Assertions.assertNotEquals(2, r, "был создан, только файл сохранения");
        Assertions.assertEquals(3, r, "что-то не так, неизвестная ошибка");
    }
    @Test()
    public void loading() {
        Assertions.assertTrue(DataProxy.load("yairy.save"), "не могу загрузить файл сохранения");
    }
    @Test()
    public void saveBook() {
        DataProxy.getBook().getPages().add(new PageV1());
        List<Integer> indexes = new ArrayList<Integer>();
        indexes.add(DataProxy.getBook().getSize()-1);
        var result = DataProxy.save(indexes, "somebook", "somebook");
        DataProxy.getBook().getPages().remove(DataProxy.getBook().getPages().remove(DataProxy.getBook().getSize()-1));
        Assertions.assertTrue(result, "не могу сохранить книгу");
    }
    @Test()
    public void insertBook() {
        int sizeBefore =DataProxy.getBook().getSize();
        Assertions.assertTrue(DataProxy.insert("somebook.book"), "Не могу загрузить книгу");
        Assertions.assertTrue(DataProxy.remove("somebook.book"), "Не могу выгрузить книгу");
        int sizeAfter =DataProxy.getBook().getSize();
        Assertions.assertEquals(sizeBefore, sizeAfter, "Внимание! Размер, после загрузки и выгрузки файла сохранения книги, не совпадает");
    }
    @Test()
    public void savePage() {
        var page = new PageV1();
        DataProxy.getBook().getPages().add(page);
        Assertions.assertTrue(DataProxy.save(DataProxy.getBook().getSize()-1, "saved page"));
        DataProxy.getBook().remove(page);
    }
    @Test()
    public void loadPage() {
        int sizeBefore =DataProxy.getBook().getSize();
        Assertions.assertTrue(DataProxy.insert("saved page.page"), "Не могу загрузить страницу");
        Assertions.assertTrue(DataProxy.remove("saved page.page"), "Не могу выгрузить страницу");
        int sizeAfter =DataProxy.getBook().getSize();
        Assertions.assertEquals(sizeBefore, sizeAfter, "Внимание! Размер, после загрузки и выгрузки страницы, не совпадает");
    }
}
