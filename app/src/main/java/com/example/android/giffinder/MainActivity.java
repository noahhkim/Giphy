package com.example.android.giffinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.search_results)
    TextView searchResultsText;
    @BindView(R.id.rv_query_results)
    RecyclerView resultsRecyclerView;
    @BindView(R.id.no_results_view)
    TextView noResultsView;
//    @BindView(R.id.search_input)
//    EditText mSearchEditText;
//    @BindView(R.id.search_icon)
//    ImageView mSearchIcon;
    @BindView(R.id.loading_circle)
    ProgressBar progressCircle;

    private GPHApi mClient;
    private GifAdapter mGifAdapter;
    private List<String> mGifs = new ArrayList<>();

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

        initRecyclerView();

        // Read EditText entry and populate search results
//        imageButtonOnClickListener();

        // Make entire search bar clickable
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });

        // Query string from searchview
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        String searchResult = getString(R.string.results_for) + query.trim() + "\"";
        searchResultsText.setText(searchResult);
    }

    private void getGifSearchResults(String query) {
        // Clear recent list of gifs
        mGifs.clear();

        // Show progress bar
        noResultsView.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);

        // Gif Search
        mClient.search(query, MediaType.gif, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                // Hide progress bar
                progressCircle.setVisibility(View.GONE);
                if (result == null || result.getData().size() <= 0) {

                    // If there's no results, show no empty view
                    resultsRecyclerView.setVisibility(View.GONE);
                    noResultsView.setVisibility(View.VISIBLE);
                } else {
                    if (result.getData() != null) {
                        for (Media media : result.getData()) {
                            // Parse through results to retrieve gif url string
                            String imageUrl = media.getImages().
                                    getFixedWidth().getGifUrl();

                            // Add urls to arraylist and notify change to adapter
                            mGifs.add(imageUrl);
                            mGifAdapter.setGifs(mGifs);

                            // If there's results, show recyclerview
                            resultsRecyclerView.setVisibility(View.VISIBLE);
                            noResultsView.setVisibility(View.GONE);
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
        // Clear recent list of gifs
        mGifs.clear();
    }
}
