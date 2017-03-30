package ru.limon.yandextest;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by User on 3/16/2017.
 */

/**
 * Helper class for Translation via Yandex.translate API
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public QueryUtils() {
    }

    /**
     * Translates text given in Url with Yandex.translate API and making Translation object
     *
     * @param requestUrl Yandex.translate API query to translate text
     * @return Translation object that contains translation data
     */
    public static Translation fetchTranslationData(String requestUrl) {
        Log.i(LOG_TAG, "Translation constructor called");
        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making Http request.", e);
        }

        return extractFromJson(jsonResponse);
    }

    /**
     * Creates URL object from String
     *
     * @param requestUrl Url in String type
     * @return URL object
     */
    private static URL createUrl(String requestUrl) {
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Cannot create url ", e);
        }
        Log.i(LOG_TAG, "Url created. " + url.toString());

        return url;
    }

    /**
     * Makes HTTP request, receive input stream and making json string from it
     *
     * @param url URL object
     * @return jsonResponse String with text in JSON format
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving translation JSON response ", e);
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        Log.i(LOG_TAG, "Json response received. " + jsonResponse);

        return jsonResponse;
    }

    /**
     * Reads from input stream and make String
     *
     * @param inputStream
     * @return String which contains JSON data
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Extract data from JSON format String and make Translation object
     *
     * @param jsonResponse
     * @return Translation object
     */
    private static Translation extractFromJson(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        Translation translation = null;

        try {
            JSONObject rootJson = new JSONObject(jsonResponse);

            int responseCode = rootJson.getInt("code");
            String lang = rootJson.getString("lang");
            String translatedText = rootJson.getJSONArray("text").getString(0);

            translation = new Translation(lang, translatedText, responseCode);

            Log.i(LOG_TAG, "Translation data: " + translation.getText());
            Log.i(LOG_TAG, translation.getLang());
            Log.i(LOG_TAG, String.valueOf(translation.getResponseCode()));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with extracting JSON response ", e);
        }

        return translation;
    }
}
