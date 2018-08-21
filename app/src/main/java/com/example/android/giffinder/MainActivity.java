package com.example.android.giffinder;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
    @BindView(R.id.search_icon)
    ImageView mSearchIcon;
    @BindView(R.id.loading_circle)
    ProgressBar progressCircle;
    @BindView(R.id.empty_view)
    TextView emptyView;

    private GPHApi mClient;
    private GifAdapter mGifAdapter;
    private List<Gif> mGifs = new ArrayList<>();

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

        // Initialize recyclerview and gridlayoutmanager
        initRecyclerView();

        // Customize searchview
        customizeSearchView();

        // Set OnClickListener for search icon
        mSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss virtual keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mSearchIcon.getWindowToken(), 0);
                }
                String query = String.valueOf(searchView.getQuery());
                setSearchResultsText(query);
                getGifSearchResults(query);
            }
        });

        // Set listener for searchView
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
        if (!query.isEmpty()) {
            searchResultsText.setText(searchResult);
        }
    }

    private void getGifSearchResults(String query) {
        // Clear recent list of gifs
        mGifs.clear();

        // Show progress bar
        emptyView.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);

        // Gif Search
        mClient.search(query, MediaType.gif, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                // Hide progress bar
                progressCircle.setVisibility(View.GONE);
                if (result == null) {
                    // If results are null, show server unavail view
                    resultsRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(getString(R.string.server_unavailable));
                } else {
                    if (result.getData() != null && result.getData().size() > 0) {
                        for (Media media : result.getData()) {
                            // Parse through results to retrieve gif url string
                            String imageUrl = media.getImages().
                                    getFixedWidth().getGifUrl();

                            // Create new Gif object
                            Gif gif = new Gif(imageUrl);

                            // Add gif to arraylist and notify change to adapter
                            mGifs.add(gif);
                            mGifAdapter.setGifs(mGifs);

                            // If there's results, show recyclerview
                            resultsRecyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                    } else {
                        // If there's no results, show no gif results view
                        resultsRecyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText(getString(R.string.no_gifs_found));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear recent list of gifs
        mGifs.clear();
    }

    // Check network connectivity
    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetwork != null && wifiNetwork.isConnected()) {
                return true;
            }
            NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileNetwork != null && mobileNetwork.isConnected()) {
                return true;
            }
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected(this)) {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(getString(R.string.no_intenet_connection));
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    private void customizeSearchView() {
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(Color.LTGRAY);
        ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(Color.LTGRAY);
    }
}
