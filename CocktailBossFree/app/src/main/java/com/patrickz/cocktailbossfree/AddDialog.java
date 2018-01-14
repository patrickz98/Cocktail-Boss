package com.patrickz.cocktailbossfree;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AddDialog
{
    private AddActivity addActivity;
    private Context context;

    private static int size = 0;
    private static String unity = "";
    private static String ingredient = "";

    private Dialog dialog;

    private SeekBar seekBar;

    private View updateView = null;

    AddDialog(AddActivity addActivity)
    {
        this.addActivity = addActivity;
        this.context = addActivity.getApplicationContext();
    }

    private void createSizeView(LinearLayout sizeView, final TextView sizeTextView)
    {
        LinearLayout layout = SimpleLayout.getLayoutPadding(context, 0, 25, 0, 0);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        sizeView.addView(layout);

        TextView text = new TextView(context);
        text.setText(R.string.size);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(Color.BLACK);
        text.setLayoutParams(SimpleLayout.getLayoutParams(-2, -1));

        layout.addView(text);

        seekBar = new SeekBar(context);
        seekBar.setMax(500);
        seekBar.setLayoutParams(SimpleLayout.getLayoutParams(-1, -1));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                if (progress >= 10) progress = (progress + 4) / 5 * 5;

                // Ugly Hack
                if (progress == 0) progress = 1;

                size = progress;
                sizeTextView.setText(size + " " + unity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {}
        });

        seekBar.setProgress(100);

        layout.addView(seekBar);
    }

    private void addSpinner(LinearLayout sizeView, final TextView sizeTextView)
    {
        LinearLayout layout = SimpleLayout.getLayoutPadding(context, 0, 25, 0, 0);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        sizeView.addView(layout);

        TextView text = new TextView(context);
        text.setText(R.string.unit);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(Color.BLACK);
        text.setLayoutParams(SimpleLayout.getLayoutParams(-2, -1));

        layout.addView(text);

        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("ml");
        spinnerArray.add("part");
        spinnerArray.add("slice");
        spinnerArray.add("twist");
        spinnerArray.add("l");
        spinnerArray.add("gr");
        spinnerArray.add("splash");
        spinnerArray.add("pint");
        spinnerArray.add("glass");
        spinnerArray.add("scoop");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArray);

        Spinner spinner = new Spinner(context);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setLayoutParams(SimpleLayout.getLayoutParams(-1, -1));
        spinner.setBackgroundColor(Color.WHITE);

        //
        spinner.setDrawingCacheBackgroundColor(Color.WHITE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                unity = parent.getItemAtPosition(pos).toString();
                sizeTextView.setText(size + " " + unity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {}
        });

        layout.addView(spinner);
    }

    private void finish()
    {
//        "unit": "ml",
//        "amount": 120,
//        "ingredient": "Ginger beer",

        JSONObject json = new JSONObject();
        json.put("unit", unity);
        json.put("amount", size);
        json.put("ingredient", ingredient);

        if (updateView == null)
        {
            addActivity.addRow(json);
            return;
        }

        addActivity.updateRow(updateView, json);
    }

    private void createIngredientButtons(LinearLayout sizeView)
    {
        LinearLayout layout = SimpleLayout.getLayoutPadding(context, 0, 25, 0, 0);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        sizeView.addView(layout);

        Button cancelButton = new Button(context);
        cancelButton.setText(R.string.cancel);
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setLayoutParams(SimpleLayout.getLayoutParams(-2, -1));

        SimpleLayout.setPadding(cancelButton, 30, 15, 30, 15);

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.cancel();
            }
        });

        layout.addView(cancelButton);

        Button okButton = new Button(context);
        okButton.setText(R.string.ok);
        okButton.setGravity(Gravity.CENTER);
        okButton.setLayoutParams(SimpleLayout.getLayoutParams(-2, -1));

        SimpleLayout.setPadding(okButton, 30, 15, 30, 15);

        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
                dialog.cancel();
            }
        });

        layout.addView(okButton);
    }

    private void addIngredientSizeLayout(LinearLayout sizeView)
    {
        TextView sizeTextView = new TextView(context);
        sizeTextView.setText("");
        sizeTextView.setGravity(Gravity.CENTER);
        sizeTextView.setTextSize(20f);
        sizeTextView.setTextColor(Color.BLACK);
        sizeTextView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));
        sizeTextView.setBackgroundColor(Color.WHITE);

        sizeTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                seekBar.setProgress(size + 1);
            }
        });

        SimpleLayout.setPadding(sizeTextView, 0, 25, 0, 25);

        sizeView.addView(sizeTextView);

        addSpinner(sizeView, sizeTextView);
        createSizeView(sizeView, sizeTextView);

        createIngredientButtons(sizeView);
    }

    private void setIngredientSize(String selectedIngredient)
    {
        ingredient = selectedIngredient;

        LinearLayout layout = SimpleLayout.getLayoutMargins(context, 10, 0, 10, 0);
        layout.setBackgroundColor(Color.WHITE);

        LinearLayout sizeView = SimpleLayout.getLayoutPadding(
            context,
            IngredientsDialog.dialogSize,
            -1,
            20,
            10,
            20,
            10);

        sizeView.setGravity(Gravity.CENTER_HORIZONTAL);
        sizeView.setBackgroundColor(Color.WHITE);

        layout.addView(sizeView);

        addIngredientSizeLayout(sizeView);

        dialog = new Dialog(addActivity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layout);
        dialog.show();
    }

    public void editIngredient(JSONObject ingredient, View view)
    {
        updateView = view;
        setIngredientSize(ingredient.getString("ingredient"));
        seekBar.setProgress(ingredient.getInt("amount"));
    }

    public void addIngredient()
    {
        IngredientsDialogCallback callback = new IngredientsDialogCallback()
        {
            @Override
            public void ingredientsSelected(String ingredient)
            {
                setIngredientSize(ingredient);
            }
        };

        IngredientsDialog ingredientsDialog = new IngredientsDialog(addActivity, callback);
        ingredientsDialog.start();
    }
}
