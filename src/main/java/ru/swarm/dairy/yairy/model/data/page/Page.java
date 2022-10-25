package ru.swarm.dairy.yairy.model.data.page;

import ru.swarm.dairy.yairy.model.data.CommonMethods;

public interface Page extends CommonMethods {
    String getText(String password);
    String getFake();
    void setFake(String text);
    void setText(String password, String text);
    String getEncryptedText();
    void setEncryptedText(String encryptedText);
    void append(String text, String password);
    boolean isPasswordCorrect(String password);
    Page downgrade();
    Page upgrade();
}
