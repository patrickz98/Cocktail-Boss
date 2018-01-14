package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsDialog
{
    private final static String LOGTAG = "IngredientsDialog";

    private Activity activity;
    private Context context;

    public static final int dialogSize = 300;

    private Dialog dialog;

    private IngredientsDialogCallback doneEvent;

    private ArrayList<String> leftIngredients;

    IngredientsDialog(Activity activity, IngredientsDialogCallback doneEvent)
    {
        this.activity = activity;
        this.context = activity.getApplicationContext();

        this.doneEvent = doneEvent;
        this.leftIngredients = null;
    }

    IngredientsDialog(Activity activity, IngredientsDialogCallback doneEvent, ArrayList<String> leftIngredients)
    {
        this.activity = activity;
        this.context = activity.getApplicationContext();

        this.doneEvent = doneEvent;
        this.leftIngredients = leftIngredients;
    }

    private void addResultsRow(final String found, LinearLayout resultsView)
    {
        if ((leftIngredients != null) && (! leftIngredients.contains(found))) return;

        TextView text = new TextView(context);
        text.setText(found);
        text.setTextSize(18f);
        text.setTextColor(Color.BLACK);
        text.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(text, 20, 5, 20, 5);

        text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doneEvent.ingredientsSelected(found);
                dialog.cancel();
            }
        });

        resultsView.addView(text);
    }

    private void findResults(String search, LinearLayout resultsView)
    {
        resultsView.removeAllViews();

        // JSONObject allIngredient = MainActivity.simpleCocktail.statistics;
        JSONObject allIngredient = MainActivity.simpleCocktail.ingredients_db;

        ArrayList<String> tmp = new ArrayList<>();

        for (String ingredient: allIngredient.keySet())
        {
            String lowIngredient = ingredient.toLowerCase();

            if (! lowIngredient.contains(search.toLowerCase())) continue;

            tmp.add(ingredient);
        }

        String[] found = tmp.toArray(new String[]{});
        Arrays.sort(found);

        for (String key: found)
        {
            addResultsRow(key, resultsView);
        }
    }

    private void setSearchListener(EditText editText, final LinearLayout resultsView)
    {
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                findResults(charSequence.toString(), resultsView);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {}
        });

        findResults("", resultsView);
    }

    public void start()
    {
        dialog = new Dialog(activity);
        dialog.setCanceledOnTouchOutside(false);

        LinearLayout layout = SimpleLayout.getLayoutMargins(context, 10, 0, 10, 0);
        layout.setBackgroundColor(Color.WHITE);

        // Disable EditText auto focus
        layout.setFocusableInTouchMode(true);

        TextView text = new TextView(context);
        text.setText(R.string.search_ingredient);
        text.setTextSize(20f);
        text.setTextColor(Color.BLACK);
        text.setLayoutParams(SimpleLayout.getLayoutParams(dialogSize, -2));

        SimpleLayout.setPadding(text, 20, 10, 20, 10);

        layout.addView(text);

        EditText editText = new EditText(context);
        editText.setHint(R.string.search);
        editText.setHintTextColor(Color.GRAY);
        editText.setMaxHeight(1);
        editText.setTextColor(Color.BLACK);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        editText.setLayoutParams(SimpleLayout.getLayoutParams(dialogSize, -2));

        editText.clearFocus();

        SimpleLayout.setPadding(editText, 20, 10, 20, 10);

        layout.addView(editText);

        LinearLayout resultsView = SimpleLayout.getLayout(context, dialogSize, dialogSize);
        resultsView.setBackgroundColor(Color.WHITE);
        layout.addView(resultsView);

        LinearLayout resultsViewScroll = SimpleLayout.createScrollView(activity, resultsView);

        setSearchListener(editText, resultsViewScroll);

        dialog.setContentView(layout);
        dialog.show();
    }
}
