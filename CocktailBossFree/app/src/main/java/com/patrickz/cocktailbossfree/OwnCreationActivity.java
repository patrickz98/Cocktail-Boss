package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.json.simple.JSONArray;

import java.util.Map;

public class OwnCreationActivity extends Activity
{
    private static final String LOGTAG = "OwnCreationActivity";

    private LinearLayout scrollLayout;
    private OwnCreationManager manager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Simple.addMenuItem(menu, Simple.getText(this, R.string.add_cocktail), R.drawable.add);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        scrollLayout.removeAllViewsInLayout();

        JSONArray cocktails = manager.getCocktails();

        if (cocktails.length() <= 0)
        {
            SimpleLayout.createNoneNote(this, scrollLayout, R.string.no_items);
            return;
        }

        CocktailBunch.createOwnCocktailsEntries(this, scrollLayout, Simple.sortJSONArray(cocktails));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleAd.createContentLayout(this, R.string.own_creations);
        scrollLayout = (LinearLayout) layout.get("scrollLayout");

        manager = new OwnCreationManager(this);
    }
}
