package com.patrickz.cocktailboss;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class SearchActivity extends Activity
{
    private final static String LOGTAG = "SearchActivity";

    private JSONArray foundCocktails;
    private LinearLayout scroll;

    private void clickListener(String text)
    {
        if (text.equals(""))
        {
            scroll.removeAllViews();
            scroll.removeAllViewsInLayout();

            return;
        }

        JSONObject cocktails = MainActivity.simpleCocktail.cocktails;

//        OwnCreationManager ownManager = new OwnCreationManager(this);
//        JSONArray ownCocktails = ownManager.getCocktails();
//
//        for (int inx = 0; inx < ownCocktails.length(); inx++)
//        {
//            JSONObject cocktail = ownCocktails.getJSONObject(inx);
//            cocktails.put(cocktail.getString("id"), cocktail);
//        }

        JSONArray tmpJson = new JSONArray();

        for (String key: cocktails.keySet())
        {
            JSONObject json = cocktails.getJSONObject(key);
            String name = json.getString("name").toLowerCase();

            if (! name.contains(text.toLowerCase())) continue;

            tmpJson.put(json);
        }

        foundCocktails = Simple.sortJSONArray(tmpJson);

        createResults();
    }

    private void createSearchBar(LinearLayout contentLayout)
    {
        //
        // Parent layout
        //

        LinearLayout roundedLayout = SimpleLayout.getLayoutMargins(this, 20, 10, 20, 10);
        SimpleLayout.setPadding(roundedLayout, 20, 0, 20, 0);
        roundedLayout.setBackground(SimpleLayout.roundedCorners(1000, "#ffffff", 1, SimpleLayout.boxBorderColor));

        contentLayout.addView(roundedLayout);

        //
        // Input field
        //

        EditText editText = new EditText(this);
        editText.setHint(R.string.search);
        editText.setMaxHeight(1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        roundedLayout.addView(editText);

        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.length() <= 1) return;
                clickListener(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {}
        });
    }

    private void createResults()
    {
        scroll.removeAllViews();
        scroll.removeAllViewsInLayout();

        CocktailBunch.createEntries(this, scroll, foundCocktails);

        foundCocktails = new JSONArray();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleLayout.createContentLayout(this, R.string.search);
        LinearLayout contentLayout = (LinearLayout) layout.get("contentLayout");

        contentLayout.removeAllViewsInLayout();

        foundCocktails = new JSONArray();

        createSearchBar(contentLayout);

        scroll = SimpleLayout.createScrollView(this, contentLayout);
    }
}
