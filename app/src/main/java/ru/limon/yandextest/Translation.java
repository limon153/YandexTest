package ru.limon.yandextest;

/**
 * Created by User on 3/17/2017.
 */

public class Translation {

    String mInitialText;
    String mLang;
    String[] mText;
    int mResponseCode;

    public Translation(String initialText, String lang, String[] text, int responseCode) {
        mInitialText = initialText;
        mLang = lang;
        mText = text;
        mResponseCode = responseCode;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getInitialText() {
        return mInitialText;
    }

    public String getLang() {
        return mLang;
    }

    public String[] getText() {
        return mText;
    }
}
