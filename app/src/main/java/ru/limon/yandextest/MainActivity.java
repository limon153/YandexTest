package ru.limon.yandextest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

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
        setupActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Main field to entering text to translate
        mEditText = (EditText) findViewById(R.id.text_input);

        mInitTextView = (TextView) findViewById(R.id.initial_text);
        mTranslatedTextView = (TextView) findViewById(R.id.translated_text);

        // Translation when text inputted is changed
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
                if (!TextUtils.isEmpty(mInputtedText)) {
                    new TranslationTask().execute(mInputtedText);
                }
            }
        });

        //Action bar buttons listeners
        TextView leftLanguageSelectTextView = (TextView) findViewById(R.id.baldezh);
        Button changeLanguageBtn = (Button) findViewById(R.id.knopka);
        TextView rightLanguageSelectTExtView = (TextView) findViewById(R.id.bertie);

        leftLanguageSelectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Baldezh activated", Toast.LENGTH_SHORT).show();
            }
        });

        changeLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Knopka activated", Toast.LENGTH_SHORT).show();
            }
        });

        rightLanguageSelectTExtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Bertie activated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Using Toolbar like an Action bar
     */
    private void setupActionBar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(mainToolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * Class for translating with Yandex translate API on background thread
     */
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
