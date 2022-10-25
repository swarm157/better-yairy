package ru.swarm.dairy.yairy.model.saves;

import org.apache.commons.io.FileUtils;
import ru.swarm.dairy.yairy.model.data.book.Book;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.swarm.dairy.yairy.model.data.book.BookV1;

import java.io.File;
import java.io.IOException;

public class SaveV1 implements Save{
    @Override
    public long getVersion() {
        return 1;
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
    public Save upgrade() {
        return null;
    }

    @Override
    public Save downgrade() {
        return null;
    }

    @Override
    public boolean save(File file, Book book) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        book.prepareToGson();
        try {
            FileUtils.writeStringToFile(file, gson.toJson(book));
            return true;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public Book load(File file) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            String temp = FileUtils.readFileToString(file);
            var book = gson.fromJson(temp, BookV1.class);
            book.prepareAfterGson();
            return book;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

}
