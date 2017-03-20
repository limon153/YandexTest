package ru.limon.yandextest;

/**
 * Created by User on 3/17/2017.
 */

public class Translation {

    private String mLang;
    private String mTranslatedText;
    private int mResponseCode;

    public Translation(String lang, String text, int responseCode) {
        mLang = lang;
        mTranslatedText = text;
        mResponseCode = responseCode;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getLang() {
        return mLang;
    }

    public String getText() {
        return mTranslatedText;
    }
}
