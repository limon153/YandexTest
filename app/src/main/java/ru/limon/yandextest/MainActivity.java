package ru.limon.yandextest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;

    private String mInputtedText;

    private TextView mInitTextView;

    private TextView mTranslatedTextView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mEditText = (EditText) findViewById(R.id.text_input);

        mInitTextView = (TextView) findViewById(R.id.initial_text);
        mTranslatedTextView = (TextView) findViewById(R.id.translated_text);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mInputtedText = mEditText.getText().toString();
                new TranslationTask().execute(mInputtedText);
            }
        });
    }

    public class TranslationTask extends AsyncTask<String, Void, Translation> {

        private final String URL_BASE_TRANSLATE = "https://translate.yandex.net/api/v1.5/tr.json/" +
                "translate" +
                "?key=trnsl.1.1.20170316T144016Z.dfd9ef1db041a961.4765cc29df0380a021bf257eb4c1ee2b3cbc1f57";

        private String mLang = "&lang=ru-en";

        private String mText = "&text=";

        private String mUrl;

        @Override
        protected Translation doInBackground(String... inputtedText) {
            if (inputtedText.length > 0) {
                mText += inputtedText[0];
            }

            mUrl = URL_BASE_TRANSLATE + mText + mLang;

            return QueryUtils.fetchTranslationData(mUrl);
        }

        @Override
        protected void onPostExecute(Translation translation) {
            mInitTextView.setText(mInputtedText);
            mTranslatedTextView.setText(translation.getText());
        }
    }
}
