package com.example.android.giffinder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.giffinder.MainActivity.SEARCH_KEY;

public class ResultsFragment extends Fragment {
    @BindView(R.id.search_results)
    TextView searchResultsText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();
        if (args != null) {
            // Show query input from user
            setSearchResultsText(args);
        }
        return rootView;
    }

    private void setSearchResultsText(Bundle args) {
        String searchInput = args.getString(SEARCH_KEY);
        String searchResult = "Search results for " + "\"" + searchInput + "\"";
        searchResultsText.setText(searchResult);
    }
}
