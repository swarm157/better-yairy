package ru.swarm.dairy.yairy.model.data.page;

import ru.swarm.dairy.yairy.model.data.CommonMethods;

import static java.lang.System.getProperty;

public class PageV1 implements Page {
    public String text = "";
    public String fake = "";
    public String creator = "";
    public String name = "";
    public String lastEditor = "";
    public long creationDate = 1;

    public long version = 1;
    public long textVersion = System.currentTimeMillis();
    public PageV1() {
        lastEditor = getProperty("user.name");
        creator = getProperty("user.name");
        creationDate = System.currentTimeMillis();
    }


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
    public Page downgrade() {
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();
        return null;
    }

    @Override
    public Page upgrade() {
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
    public boolean isPasswordCorrect(String password) {
        try {
            CommonMethods.decrypt(this.text, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean contain(String text, String password) {
        try {
            return CommonMethods.decrypt(this.text, password).contains(text);
        } catch (Exception e) {
            return getFake().contains(text);
        }
    }


    @Override
    public void update(Page page) {
        if (getUID().equals(page.getUID())) {
            if (getTextVersion() < page.getTextVersion()) {
                text = page.getEncryptedText();
                lastEditor = page.getLastEditorName();
                textVersion = page.getTextVersion();
            }
        }
    }

    @Override
    public void rename(String source, String destination) {
        if (name.equals(source)) {
            name = destination;
            textVersion = System.currentTimeMillis();
            lastEditor = getProperty("user.name");
        }
    }

    @Override
    public void remove(String text, String password) {
        try {
            this.text = CommonMethods.encrypt(CommonMethods.decrypt(this.text, password).replaceAll(text, ""), password);
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
    }


    @Override
    public String getText(String password) {
        try {
            return CommonMethods.decrypt(text, password);
        } catch (Exception e) {
            return getFake();
            //throw new RuntimeException(e);
        }
        //return null;
    }

    @Override
    public String getFake() {
        return fake;
    }

    @Override
    public void setFake(String text) {
        this.fake = text;
    }

    @Override
    public void setText(String password, String text) {
        this.text = CommonMethods.encrypt(text, password);
        textVersion = System.currentTimeMillis();
        lastEditor = getProperty("user.name");
    }

    @Override
    public String getEncryptedText() {
        return text;
    }

    @Override
    public void setEncryptedText(String encryptedText) {
        text = encryptedText;
        lastEditor = getProperty("user.name");
        textVersion = System.currentTimeMillis();
    }

    @Override
    public void append(String text, String password) {
        try {
            this.text= CommonMethods.decrypt(this.text, password)+ CommonMethods.encrypt(text, password);
            textVersion = System.currentTimeMillis();
            lastEditor = getProperty("user.name");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String toString() {
        return "PageV1{" +
                "text='" + text + '\'' +
                ", fake='" + fake + '\'' +
                ", creator='" + creator + '\'' +
                ", name='" + name + '\'' +
                ", lastEditor='" + lastEditor + '\'' +
                ", creationDate=" + creationDate +
                ", version=" + version +
                ", textVersion=" + textVersion +
                '}';
    }
}
