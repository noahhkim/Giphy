package com.example.android.giffinder.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.giffinder.R;
import com.example.android.giffinder.adapter.GifAdapter;
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

import static com.example.android.giffinder.ui.MainActivity.SEARCH_KEY;

public class ResultsFragment extends Fragment {
    @BindView(R.id.search_results)
    TextView searchResultsText;
    @BindView(R.id.rv_query_results)
    RecyclerView resultsRecyclerView;

    private String mSearchInput;
    private GPHApi mClient;
    private GifAdapter mGifAdapter;
    private List<String> mImageUrlList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, rootView);

        // Initialize GPHApi client
        mClient = new GPHApiClient(getString(R.string.giphy_api_key));

        // Initialize the adapter
        mGifAdapter = new GifAdapter(getContext(), mImageUrlList);

        // Retrieve search string from MainActivity
        Bundle args = getArguments();
        if (args != null) {
            mSearchInput = args.getString(SEARCH_KEY);
        } else {
            mSearchInput = "";
        }

        setSearchResultsText();

        getGifSearchResults();

        initViews();

        return rootView;
    }

    private void setSearchResultsText() {
        String searchResult = "Search results for " + "\"" + mSearchInput + "\"";
        searchResultsText.setText(searchResult);
    }

    private void getGifSearchResults() {
        /// Gif Search
        mClient.search(mSearchInput, MediaType.gif, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
//                ContentValues[] bulkToInsert;
//                List<ContentValues> mValueList = new ArrayList<>();

                if (result == null) {
                    // Toast message for no results found
                    Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
                } else {
                    if (result.getData() != null) {
                        mImageUrlList.clear();
                        for (Media gif : result.getData()) {
                            String imageUrl = gif.getImages().
                                    getFixedHeight().getGifUrl();
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put(GifContract.GifEntry.COLUMN_URL, imageUrl);
//                            mValueList.add(contentValues);
                            mImageUrlList.add(imageUrl);
                            mGifAdapter.notifyDataSetChanged();
                            Timber.d("images: " + imageUrl);
                            Timber.d("image list size: " + mImageUrlList.size());

                        }
//                        bulkToInsert = new ContentValues[mValueList.size()];
//                        mValueList.toArray(bulkToInsert);
//                        getContext().getContentResolver().bulkInsert(GifContract.GifEntry.CONTENT_URI, bulkToInsert);
                    } else {
                        Timber.e("Giphy error: No results found");
                    }
                }
            }
        });
    }

    private void initViews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        resultsRecyclerView.setLayoutManager(gridLayoutManager);
        resultsRecyclerView.setAdapter(mGifAdapter);
    }
}
