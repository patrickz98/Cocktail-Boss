package com.patrickz.cocktailboss;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IHaveActivity extends Activity
{
    private static final String LOGTAG = "IHaveActivity";

    private LinearLayout bodyLayout;
    private Button searchButton;

    private IHaveManager ingredientsManager;

    public ArrayList<String> ownIngredients;

    private IHaveSearchHelper searchHelper;

    public static JSONArray searchResults;

    private void buildIngredient(final String ingredient)
    {
        int scrollPadd = SimpleLayout.scrollPadding;
        LinearLayout paddingLayout = SimpleLayout.getLayoutPadding(
            this,
            scrollPadd,
            0,
            scrollPadd,
            0);
        bodyLayout.addView(paddingLayout);

        LinearLayout row = SimpleLayout.getIngredientRow(this, ingredient);
        paddingLayout.addView(row);

        row.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                ingredientsManager.remove(ingredient);
                ownIngredients.remove(ingredient);

                updateScrollView();

                return true;
            }
        });
    }

    private void updateSearchButton()
    {
        searchResults = searchHelper.find(alcOptions);
        searchButton.setText(Simple.getText(this, R.string.search) + ": " + searchResults.length());
    }

    private void updateScrollView()
    {
        bodyLayout.removeAllViewsInLayout();

        String ingredients[] = ownIngredients.toArray(new String[]{});
        Arrays.sort(ingredients);

        for (String ingredient: ingredients)
        {
            buildIngredient(ingredient);
        }

        updateSearchButton();

        if (ownIngredients.isEmpty())
        {
            SimpleLayout.createNoneNote(this, bodyLayout, R.string.find_cocktails_own_ing);
        }
        else
        {
            SimpleLayout.createNote(this, bodyLayout, R.string.long_touch_note);
        }
    }

    private void addIngredient(String ingredient)
    {
        if (ownIngredients.contains(ingredient)) return;

        ingredientsManager.add(ingredient);
        ownIngredients.add(ingredient);

        updateScrollView();
    }

    private void setSavedStatus()
    {
        ownIngredients = new ArrayList<>();
        ingredientsManager = new IHaveManager(this);

        searchHelper = new IHaveSearchHelper(this);

        JSONArray tmp = ingredientsManager.getAll();

        for (int inx = 0; inx < tmp.length(); inx++)
        {
            ownIngredients.add(tmp.getString(inx));
        }

        updateScrollView();
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
        Simple.addMenuItem(menu, Simple.getText(this, R.string.add_ingredient), R.drawable.add);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (checkBoxMenu.containsValue(item))
        {
            manageOptions(item);

            updateSearchButton();

            return super.onOptionsItemSelected(item);
        }

        IngredientsDialogCallback callback = new IngredientsDialogCallback()
        {
            @Override
            public void ingredientsSelected(String ingredient)
            {
                addIngredient(ingredient);
            }
        };

        IngredientsDialog ingredientDialog = new IngredientsDialog(this, callback);
        ingredientDialog.start();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleLayout.createContentLayout(this, R.string.i_have_label);
        LinearLayout contentLayout = (LinearLayout) layout.get("contentLayout");

        contentLayout.removeAllViewsInLayout();

        ownIngredients = new ArrayList<>();

        alcOptions = new HashMap<>();
        alcOptions.put(R.string.all_alcoholic, true);
        alcOptions.put(R.string.non_alcoholic, false);
        alcOptions.put(R.string.alcoholic,     false);

        //
        // Container Layout
        //

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setGravity(Gravity.BOTTOM);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));

        contentLayout.addView(relativeLayout);

        //
        // Layout for the Scroll Content
        //

//        int buttonHeight = SimpleLayout.getSize(60);
        int buttonHeight = SimpleLayout.getSize(50);

        RelativeLayout.LayoutParams paramsContainer = new RelativeLayout.LayoutParams(-1, -1);
//        paramsContainer.setMargins(0, 0, 0, buttonHeight);
        paramsContainer.setMargins(0, 0, 0, 0);

        LinearLayout testLayout = new LinearLayout(this);
        testLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        testLayout.setOrientation(LinearLayout.VERTICAL);
        testLayout.setLayoutParams(paramsContainer);

//        testLayout.setPadding(0, 0, 0, buttonHeight);

        relativeLayout.addView(testLayout);

        //
        // scrollView
        //

//        LinearLayout scrollLayout = SimpleLayout.createScrollView(this, testLayout);
        bodyLayout = SimpleLayout.createScrollView(this, testLayout);
        bodyLayout.setPadding(0, 0, 0, buttonHeight + SimpleLayout.getSize(5));

//        int padding = SimpleLayout.scrollPadding;
//        bodyLayout = SimpleLayout.getLayoutPadding(this, -1, -1, padding, padding, padding, padding);
//
//        scrollLayout.addView(bodyLayout);

        //
        // Layout for the Button
        //

//        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(-1, buttonHeight);
//        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//
//        LinearLayout buttonLayout = new LinearLayout(this);
//        buttonLayout.setLayoutParams(buttonLayoutParams);
//
//        relativeLayout.addView(buttonLayout);
//
        //
        // Buttons
        //

        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(-1, buttonHeight);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        buttonParams.setMargins(
            SimpleLayout.scrollPadding,
            SimpleLayout.scrollPadding,
            SimpleLayout.scrollPadding,
            SimpleLayout.scrollPadding);


        searchButton = new Button(this);
        searchButton.setLayoutParams(buttonParams);
//        searchButton.setLayoutParams(SimpleLayout.getLayoutParamsMargin(-1, buttonHeight, 20, 20, 20, 20));
//        searchButton.setLayoutParams(SimpleLayout.getLayoutParamsMargin(-1, -1, 20, 20, 20, 20));
//        searchButton.setLayoutParams(SimpleLayout.getLayoutParamsMargin(-1, -1, 0, 0, 0, 0));
        searchButton.setText(R.string.search);
        searchButton.setAllCaps(false);
        searchButton.setTextSize(20f);
        searchButton.setTextColor(Color.WHITE);
//        searchButton.setBackground(SimpleLayout.roundedCorners(1000, "#4be782", 1, "#1ed760"));
//        searchButton.setBackground(SimpleLayout.roundedCorners(1000, "#ff4d4d", 1, "#b33b43"));
//        searchButton.setBackground(SimpleLayout.roundedCorners(1000, "#FF7F82", 1, "#B35F64"));
//        searchButton.setBackground(SimpleLayout.roundedCorners(1000, "#828282", 1, "#a1a1a1"));
        searchButton.setBackground(SimpleLayout.roundedCorners(1000, "#828282", 2, "#c3c3c3"));
//        searchButton.setBackground(SimpleLayout.roundedCorners(1, "#FF7F82", 1, "#B35F64"));

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (searchResults.length() == 0)
                {
                    Simple.toastL(getApplicationContext(), R.string.no_results);
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), IHaveResultsActivity.class);
                startActivity(intent);
            }
        });

//        buttonLayout.addView(searchButton);
        relativeLayout.addView(searchButton);

        //
        // Saved Status
        //

        setSavedStatus();
    }
}
