package com.example.android.giffinder;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_input)
    EditText mSearchEditText;
    @BindView(R.id.search_icon)
    ImageView mSearchIcon;

    public static final String SEARCH_KEY = "search_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        // Initialize toolbar
        setSupportActionBar(mToolbar);

        // Read EditText entry and populate search results
        loadSearchResultsOnClick();
    }

    private void loadSearchResultsOnClick() {
        mSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve edit text string and add as args to fragment
                Bundle args = new Bundle();
                String searchInput = mSearchEditText.getText().toString().trim();
                if (searchInput.isEmpty()) {
                    return;
                } else {
                    args.putString(SEARCH_KEY, searchInput);
                }

                // Load results fragment
                loadFragment(args);
            }
        });
    }

    private void loadFragment(Bundle bundle) {
        Fragment resultsFrag = new ResultsFragment();
        resultsFrag.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, resultsFrag)
                .commit();
    }
}
