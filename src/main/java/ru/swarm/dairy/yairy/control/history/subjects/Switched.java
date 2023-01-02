package ru.swarm.dairy.yairy.control.history.subjects;

import ru.swarm.dairy.yairy.model.DataProxy;

public class Switched implements HistorySubject{
    String text1, fake1;
    String text2, fake2;
    int index1, index2;

    public Switched(String text1, String fake1, String text2, String fake2, int index1, int index2) {
        this.text1 = text1;
        this.fake1 = fake1;
        this.text2 = text2;
        this.fake2 = fake2;
        this.index1 = index1;
        this.index2 = index2;
    }

    @Override
    public void undo() {
        var p2 = DataProxy.getBook().getPages().get(index1);
        var p1 = DataProxy.getBook().getPages().get(index2);
        p1.setEncryptedText(text2);
        p2.setEncryptedText(text1);
        p1.setFake(fake2);
        p2.setFake(fake1);
    }

    @Override
    public void make() {
        var p1 = DataProxy.getBook().getPages().get(index1);
        var p2 = DataProxy.getBook().getPages().get(index2);
        p1.setEncryptedText(text2);
        p2.setEncryptedText(text1);
        p1.setFake(fake2);
        p2.setFake(fake1);
    }
}
