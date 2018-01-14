package com.patrickz.cocktailboss;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.json.simple.JSONArray;

import java.util.Map;

public class FindResultsActivity extends Activity
{
    private static final String LOGTAG = "FindResultsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleLayout.createContentLayout(this, R.string.results);
        LinearLayout scrollLayout = (LinearLayout) layout.get("scrollLayout");


        JSONArray array = FindActivity.searchResults;
        CocktailBunch.createEntries(this, scrollLayout, Simple.sortJSONArray(array));
    }
}
