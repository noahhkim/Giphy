package com.example.android.giffinder.ui;

import android.app.ActionBar;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.giffinder.R;
import com.example.android.giffinder.adapter.GifAdapter;
import com.example.android.giffinder.data.GifViewModel;
import com.example.android.giffinder.data.room.Gif;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.search_results)
    TextView searchResultsText;
    @BindView(R.id.rv_query_results)
    RecyclerView resultsRecyclerView;
//    @BindView(R.id.search_input)
//    EditText mSearchEditText;
//    @BindView(R.id.search_icon)
//    ImageView mSearchIcon;

    private GPHApi mClient;
    private GifAdapter mGifAdapter;
    private GifViewModel mGifViewModel;

    public static final String SEARCH_KEY = "search_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        // Initialize toolbar
        setSupportActionBar(mToolbar);

        // Initialize GPHApi client
        mClient = new GPHApiClient(getString(R.string.giphy_api_key));

        // Initialize the adapter
        mGifAdapter = new GifAdapter(this);

        // Get a ViewModel from the ViewModelProvider
        mGifViewModel = ViewModelProviders.of(this).get(GifViewModel.class);

        // Add an observer for the LiveData returned by getAllWords()
        mGifViewModel.getAllGifs().observe(this, new Observer<List<Gif>>() {
            @Override
            public void onChanged(@Nullable List<Gif> gifs) {
                // Update the cached copy of the words in the adapter
                mGifAdapter.setGifs(gifs);
            }
        });

        initRecyclerView();

        // Read EditText entry and populate search results
//        imageButtonOnClickListener();

        // Make entire search bar clickable
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setIconified(false);
            }
        });

        // Query string from searchview
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setSearchResultsText(query);
                getGifSearchResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setSearchResultsText(String query) {
        String searchResult = "Search results for " + "\"" + query.trim() + "\"";
        searchResultsText.setText(searchResult);
    }

    private void getGifSearchResults(String query) {
        /// Gif Search
        mClient.search(query, MediaType.gif, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                if (result == null) {
                    // Toast message for no results found
                    Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                } else {
                    if (result.getData() != null) {
                        mGifViewModel.deleteAll();
                        for (Media media : result.getData()) {
                            String imageUrl = media.getImages().
                                    getFixedWidth().getGifUrl();
                            Gif gif = new Gif(imageUrl);
                            mGifViewModel.insert(gif);
                        }
                    } else {
                        Timber.e("Giphy error: No results found");
                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        resultsRecyclerView.setLayoutManager(gridLayoutManager);
        resultsRecyclerView.setAdapter(mGifAdapter);
    }

//    private void imageButtonOnClickListener() {
//        mSearchIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////
//                // Dismiss virtual keyboard
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
//                }
//            }
//        });
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGifViewModel.deleteAll();
    }
}
