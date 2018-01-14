package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.json.simple.JSONArray;

import java.util.Map;

public class IHaveResultsActivity extends Activity
{
    private static final String LOGTAG = "IHaveResultsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleAd.createContentLayout(this, R.string.possible_cocktails);
        LinearLayout scrollLayout = (LinearLayout) layout.get("scrollLayout");


        JSONArray array = IHaveActivity.searchResults;
        CocktailBunch.createEntries(this, scrollLayout, Simple.sortJSONArray(array));
    }
}
