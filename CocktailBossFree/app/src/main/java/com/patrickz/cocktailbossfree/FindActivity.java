package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindActivity extends Activity
{
    private final static String LOGTAG = "FindActivity";
    private JSONObject clickedJson;
    private ArrayList<String> search;

    public static CocktailSearchHelper searchHelper;
    public static JSONArray searchResults;

    private Button button;

    private ArrayList<String> leftIngredients;

    private void disableObsolete()
    {
        leftIngredients = searchHelper.getLeftIngredients(searchResults);

        for (String key: ingredientsMapTop.keySet())
        {
            LinearLayout layout = ingredientsMapTop.get(key);

            if (leftIngredients.contains(key))
            {
                layout.setVisibility(View.VISIBLE);
                continue;
            }

            layout.setVisibility(View.GONE);
        }

        for (String key: ingredientsMapAll.keySet())
        {
            LinearLayout layout = ingredientsMapAll.get(key);

            if (leftIngredients.contains(key))
            {
                layout.setVisibility(View.VISIBLE);
                continue;
            }

            layout.setVisibility(View.GONE);
        }
    }

    public void onClickHandler(String title)
    {
        boolean clicked = clickedJson.getBoolean(title);

        if (clicked)
        {
            if (search.contains(title)) search.remove(title);

            ingredientsMapAll.get(title).setAlpha(1f);
            clickedJson.put(title, false);
        }
        else
        {
            if (! search.contains(title)) search.add(title);

            ingredientsMapAll.get(title).setAlpha(0.5f);
            clickedJson.put(title, true);
        }

        if (ingredientsMapTop.containsKey(title))
        {
            if (clicked) ingredientsMapTop.get(title).setAlpha(1f);
            else ingredientsMapTop.get(title).setAlpha(0.5f);
        }

//        searchResults = searchHelper.searchForCocktails(search, alcOptions);
//
//        disableObsolete();
//
//        String newTitle = "Search: " + searchResults.length();
//        button.setText(newTitle);
        update();
    }

    private void onClickHandlerChar(View view, String title)
    {
        boolean clicked = clickedJson.getBoolean(title);

        if (clicked)
        {
            if (search.contains(title)) search.remove(title);

            view.setAlpha(1f);
            clickedJson.put(title, false);
        }
        else
        {
            if (! search.contains(title)) search.add(title);

            view.setAlpha(0.5f);
            clickedJson.put(title, true);
        }

//        searchResults = searchHelper.searchForCocktails(search, alcOptions);
//
//        disableObsolete();
//
//        String newTitle = "Search: " + searchResults.length();
//        button.setText(newTitle);

        update();
    }

    private void addOnClickListener(View text, final String title)
    {
        text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (title.length() == 1)
                {
                    onClickHandlerChar(view, title);
                    return;
                }

                onClickHandler(title);
            }
        });
    }

    Map<String, LinearLayout> ingredientsMapTop = new HashMap<>();
    Map<String, LinearLayout> ingredientsMapAll = new HashMap<>();

    private void addItems(
        LinearLayout scroll,
        String title,
        String textColor,
        String color,
        String stroke,
        Map<String, LinearLayout> map)
    {
        int margins = SimpleLayout.getSize(10);

        LinearLayout.LayoutParams params = SimpleLayout.getLayoutParams(50, 50);
        params.setMargins(margins, margins, margins, margins);

        TextView test1 = new TextView(this);
        test1.setText(title);
        test1.setGravity(Gravity.CENTER);
        test1.setTextColor(Color.parseColor(textColor));
        test1.setBackground(SimpleLayout.roundedCorners(1000, color, 1, stroke));
        test1.setLayoutParams(params);

        clickedJson.put(title, false);

        if (title.length() > 1)
        {
            test1.setText("");

            LinearLayout layout = SimpleLayout.getLayoutMargins(this, 10, 0, 10, 0);
            layout.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.setOrientation(LinearLayout.VERTICAL);

            scroll.addView(layout);

            addOnClickListener(layout, title);

            layout.addView(test1);

            TextView disc = new TextView(this);
            disc.setText(title);
            disc.setGravity(Gravity.CENTER);
            disc.setLayoutParams(SimpleLayout.getLayoutParams(-2, -2));

            layout.addView(disc);

            map.put(title, layout);

            return;
        }

        addOnClickListener(test1, title);
        scroll.addView(test1);
    }

    private LinearLayout horizontalScroll(LinearLayout mainLayout, int titleId)
    {
        LinearLayout searchPart = SimpleLayout.getLayoutPadding(this, 0, 35, 0, 0);
        mainLayout.addView(searchPart);

        //
        // TextView
        //

        TextView textView = new TextView(this);
        textView.setText(titleId);
        textView.setTextSize(20f);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(textView, 10, 0, 10, 0);

        searchPart.addView(textView);

        //
        // Scroll
        //

        HorizontalScrollView scrollView = new HorizontalScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(-1, -2));

        searchPart.addView(scrollView);

        //
        // Layout for scrolling
        //

        LinearLayout scrollLayout = SimpleLayout.getLayout(this, -1, -1);
        scrollLayout.setGravity(Gravity.CENTER_VERTICAL);
        scrollLayout.setOrientation(LinearLayout.HORIZONTAL);

        scrollView.addView(scrollLayout);


        return scrollLayout;
    }

    private void createSearchButton(LinearLayout buttonLayout)
    {
        View.OnClickListener event = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (searchResults.length() == MainActivity.simpleCocktail.cocktails.length())
                {
                    Simple.toastL(getApplicationContext(), R.string.too_many_results);
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), FindResultsActivity.class);
                startActivity(intent);
            }
        };

        String title = Simple.getText(this, R.string.search) + ": " + searchResults.length();

//        button = SimpleLayout.createRoundButton(this, "Search: " + searchResults.length(), "#ff4d4d");
        button = SimpleLayout.createRoundButton(this, title);
        button.setOnClickListener(event);

        buttonLayout.addView(button);
    }

    private void reset()
    {
        search = new ArrayList<>();
        searchResults = searchHelper.searchForCocktails(search, alcOptions);

        disableObsolete();

        String newTitle = Simple.getText(this, R.string.search) + ": " + searchResults.length();
        button.setText(newTitle);

        for (String title: ingredientsMapAll.keySet())
        {
            LinearLayout layout = ingredientsMapAll.get(title);
            layout.setAlpha(1f);
            clickedJson.put(title, false);
        }

        for (String title: ingredientsMapTop.keySet())
        {
            LinearLayout layout = ingredientsMapTop.get(title);
            layout.setAlpha(1f);
            clickedJson.put(title, false);
        }
    }

    private void createResetButton(LinearLayout buttonLayout)
    {
        View.OnClickListener event = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                reset();
            }
        };

        Button button = SimpleLayout.createRoundButton(this, R.string.reset);
        button.setOnClickListener(event);

        buttonLayout.addView(button);
    }

    private Map<Integer, Boolean>  alcOptions;
    private Map<Integer, MenuItem> checkBoxMenu;

    private void manageOptions(MenuItem item)
    {
        if (item.isChecked()) return;

        item.setChecked(! item.isChecked());

        for (Integer key: checkBoxMenu.keySet())
        {
            MenuItem mItem = checkBoxMenu.get(key);
            alcOptions.put(key, true);

            if (mItem == item) continue;

            mItem.setChecked(false);
            alcOptions.put(key, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Simple.addMenuItem(menu, Simple.getText(this, R.string.search), R.drawable.search);

        MenuItem all          = Simple.addMenuItemCheck(menu, R.string.all_alcoholic);
        MenuItem nonAlcoholic = Simple.addMenuItemCheck(menu, R.string.non_alcoholic);
        MenuItem alcoholic    = Simple.addMenuItemCheck(menu, R.string.alcoholic);

        checkBoxMenu = new HashMap<>();
        checkBoxMenu.put(R.string.all_alcoholic, all);
        checkBoxMenu.put(R.string.non_alcoholic, nonAlcoholic);
        checkBoxMenu.put(R.string.alcoholic,     alcoholic);

        all.setChecked(true);

        return true;
    }

    private void update()
    {
        searchResults = searchHelper.searchForCocktails(search, alcOptions);
        disableObsolete();
        String newTitle = "Search: " + searchResults.length();
        button.setText(newTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (checkBoxMenu.containsValue(item))
        {
            manageOptions(item);

            reset();
            update();

            return super.onOptionsItemSelected(item);
        }

        IngredientsDialogCallback callback = new IngredientsDialogCallback()
        {
            @Override
            public void ingredientsSelected(String ingredient)
            {
                onClickHandler(ingredient);
            }
        };

        IngredientsDialog ingredientDialog = new IngredientsDialog(this, callback, leftIngredients);
        ingredientDialog.start();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        search = new ArrayList<>();
        clickedJson = new JSONObject();

        alcOptions = new HashMap<>();
        alcOptions.put(R.string.all_alcoholic, true);
        alcOptions.put(R.string.non_alcoholic, false);
        alcOptions.put(R.string.alcoholic,     false);

        searchHelper = new CocktailSearchHelper();
        searchResults = searchHelper.searchForCocktails(search, alcOptions);
        leftIngredients = searchHelper.getLeftIngredients(searchResults);

        JSONObject ingredients_db = MainActivity.simpleCocktail.ingredients_db;

        Map<String, Object> layout = SimpleAd.createContentLayout(this, R.string.search_by_ingredient);
        LinearLayout scrollLayout = (LinearLayout) layout.get("scrollLayout");

        //
        // Alphabet
        //

        LinearLayout aplh = horizontalScroll(scrollLayout, R.string.alphabet);

        String textColor = "#595959";
        String color     = "#ffffff";
        String stroke    = SimpleLayout.boxBorderColor;

        String abc[] = new String[]{
            "#", "A","B","C","D","E","F","G",
            "H","I","J","K","L","M","N","O",
            "P","Q","R","S","T","U","V","W",
            "X","Y","Z"
        };

        for (int inx = 0; inx < abc.length; inx++)
        {
            String title = abc[ inx % abc.length ];
            addItems(aplh, title, textColor, color, stroke, null);
        }

        //
        // Top Ingredients
        //

        JSONArray topIng = new JSONArray(Simple.getRaw(this, R.raw.top_ingredients));

        LinearLayout top = horizontalScroll(scrollLayout, R.string.top_ingredients);

        for (int inx = 0; inx < topIng.length(); inx++)
        {
            String key = topIng.getString(inx);

            textColor = "#ffffff";
            color     = ingredients_db.getJSONObject(key).getString("color");
            stroke    = SimpleLayout.boxBorderColor;

            addItems(top, key, textColor, color, stroke, ingredientsMapTop);
        }

        //
        // Ingredients
        //

        LinearLayout ing = horizontalScroll(scrollLayout, R.string.all_ingredients);

        String[] allInd = Simple.sortJsonKeys(ingredients_db);

        for (String key: allInd)
        {
            textColor = "#ffffff";
            color     = ingredients_db.getJSONObject(key).getString("color");
            stroke    = SimpleLayout.boxBorderColor;

            addItems(ing, key, textColor, color, stroke, ingredientsMapAll);
        }

        //
        // Buttons
        //

        LinearLayout buttonLayout = SimpleLayout.getLayoutPadding(this, 0, 35, 0, 0);

        createSearchButton(buttonLayout);
        createResetButton(buttonLayout);

        scrollLayout.addView(buttonLayout);
    }
}
