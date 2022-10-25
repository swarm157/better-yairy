package ru.swarm.dairy.yairy.model.data;

import ru.swarm.dairy.yairy.model.data.page.Page;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public interface CommonMethods {
    long getVersion();
    long getTextVersion();
    String getLastEditorName();

    boolean isNewest();
    boolean isOldest();
    String getName();
    void setName(String name);
    String getCreator();
    long getCreationDate();
    String getUID();

    boolean contain(String text, String password);
    void update(Page page);
    void rename(String source, String destination);
    void remove(String text, String password);

    static String encrypt(String plainText, String password) {
        plainText+="ddd";
        char[] text = plainText.toCharArray();
        char[] pass = password.toCharArray();
        char[] crypto = new char[text.length];


        int c = 0;
        for (int i = 0; i < text.length; i++) {

            crypto[i] = (char) (text[i]+pass[c]);
            c++;
            if (c>=password.length()) c=0;
        }

        String result = new String(crypto);


        return result;
    }

    static String decrypt(String encryptedText, String password) throws Exception {
        char[] text = encryptedText.toCharArray();
        char[] pass = password.toCharArray();
        char[] encrypt = new char[text.length];

        int c = 0;
        for (int i = 0; i < text.length; i++) {

            encrypt[i] = (char) (text[i]-pass[c]);

            c++;
            if (c>=password.length()) c=0;
        }

        String result = new String(encrypt);
        if (!result.endsWith("ddd")) throw new Exception("wrong password");
        else {
            char[] data = new char[encrypt.length-3];
            for (int i = 0 ; i < data.length; i ++) {
                data[i] = encrypt[i];
            }
            result = new String(data);
        }


        return result;
    }
}
