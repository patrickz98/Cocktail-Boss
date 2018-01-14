package com.patrickz.cocktailboss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class CocktailDetails extends Activity
{
    private final static String LOGTAG = "CocktailDetails";

    private JSONObject cocktail;
    private JSONObject transformedCocktail;

    private boolean ownCocktail;
    private boolean randomCocktail;

    private int glassSize = 200;

    private TextView alcoholPercentView = null;

    private Map<String, Object> layout;

    private boolean alcoholic;

    public static String getRandomCocktail()
    {
        JSONObject cocktails = MainActivity.simpleCocktail.cocktails;

        String[] keys = cocktails.keySet().toArray(new String[]{});

        int max = keys.length;
        int min = 0;

        Random random = new Random();
        int rand = random.nextInt(max - min + 1) + min;

        return cocktails.getJSONObject(keys[ rand ]).toString();
    }

    private RelativeLayout cocktailView(JSONObject transformedCocktail)
    {
        int widthHeight = SimpleLayout.getSize(150);
        int margins     = SimpleLayout.getSize(5);

        RelativeLayout.LayoutParams parmas = new RelativeLayout.LayoutParams(widthHeight, widthHeight);
        parmas.setMargins(margins, margins, margins, margins);

        RelativeLayout layout = new RelativeLayout(this);
        layout.setGravity(Gravity.CENTER);
        layout.setBackground(SimpleLayout.roundedCorners(1000, "#ffffff", 1, SimpleLayout.boxBorderColor));
        layout.setLayoutParams(parmas);

        JSONObject layoutJson = new JSONObject();
        layoutJson.put("padding",     SimpleLayout.getSize(10));
        layoutJson.put("glassWidth",  SimpleLayout.getSize(50));
        layoutJson.put("glassHeight", SimpleLayout.getSize(100));

        LinearLayout testLayout = new LinearLayout(this);
        testLayout.setGravity(Gravity.CENTER);
        testLayout.setLayoutParams(SimpleLayout.getLayoutParams(-1, -1));

        layout.addView(testLayout);

        CocktailView view = new CocktailView(this, transformedCocktail, layoutJson);

        testLayout.addView(view);

        return layout;
    }

    private RelativeLayout favoritesButton(int size, String color)
    {
        final FavoritesManager favoritesManager = new FavoritesManager(this);
        final String uuid = cocktail.getString("id");

        //
        // Layout
        //

        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(
            SimpleLayout.getSize(size),
            SimpleLayout.getSize(size)));

        layout.setBackground(SimpleLayout.roundedCorners(1000, color));

        //
        // Center Layout
        //

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));

        layout.addView(linearLayout);

        //
        // ImageView
        //

        final ImageView imgView = new ImageView(this);
        imgView.setLayoutParams(SimpleLayout.getLayoutParams(size/2, size/2));
        imgView.setBackgroundResource(R.drawable.star_empty);

        if (favoritesManager.includes(uuid)) imgView.setBackgroundResource(R.drawable.star_filled);

        linearLayout.addView(imgView);


        final Context context = getApplicationContext();
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean includes = favoritesManager.includes(uuid);

                if (includes)
                {
                    Simple.toastL(context, Simple.getText(context, R.string.remove_favorits) + ": " + cocktail.getString("name"));

                    favoritesManager.remove(uuid);
                    imgView.setBackgroundResource(R.drawable.star_empty);
                }
                else
                {
                    Simple.toastL(context, Simple.getText(context, R.string.add_favorites) + cocktail.getString("name"));
                    favoritesManager.add(uuid);
                    imgView.setBackgroundResource(R.drawable.star_filled);
                }
            }
        });

        return layout;
    }

    private void getCocktailLayout(LinearLayout layout)
    {
        LinearLayout cocktailLayout = SimpleLayout.getLayoutPadding(this, -2, -2, 15, 0, 15, 0);
        cocktailLayout.setGravity(Gravity.CENTER);
        cocktailLayout.setOrientation(LinearLayout.HORIZONTAL);

        layout.addView(cocktailLayout);

        RelativeLayout cocktailView = cocktailView(transformedCocktail);
        cocktailLayout.addView(cocktailView);
    }

    private void cocktailFavoriteButton(LinearLayout layout)
    {
        //
        // cocktailView
        //

        getCocktailLayout(layout);

        //
        // addFavo
        //

        LinearLayout favoLayout = SimpleLayout.getLayoutPadding(this, -2, -2, 15, 0, 15, 0);
        favoLayout.setGravity(Gravity.CENTER);
        favoLayout.setOrientation(LinearLayout.HORIZONTAL);

        layout.addView(favoLayout);

        favoLayout.addView(favoritesButton(100, "#606060"));
    }

    private void createCocktailView(LinearLayout scrollLayout)
    {
        LinearLayout layout = SimpleLayout.getLayoutPadding(this, 0, 30, 0, 30);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        scrollLayout.addView(layout);

        cocktailFavoriteButton(layout);

//        if (! ownCocktail)
//        {
//            cocktailFavoriteButton(layout);
//        }
//        else
//        {
//            getCocktailLayout(layout);
//        }
    }

    private void addChart(LinearLayout layout, final String title)
    {
        final PieChart mChart = new PieChart(this);

        mChart.setUsePercentValues(false);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        // mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDrawEntryLabels(true);
        // mChart.setEntryLabelTextSize(10f);
        mChart.setEntryLabelTextSize(10f);
        mChart.setEntryLabelColor(Color.WHITE);

        mChart.setTransparentCircleAlpha(110);
        mChart.setTransparentCircleColor(Color.WHITE);

        mChart.setDrawHoleEnabled(true);
        mChart.setDrawCenterText(true);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        // mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // mChart.setCenterTextSize(30f);
        mChart.setCenterTextSize(15f);
        mChart.setCenterText(title);

//        mChart.animateY(900, Easing.EasingOption.EaseInOutQuad);
//        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//        mChart.animateY(2000, Easing.EasingOption.EaseInOutQuad);
//        mChart.spin(2000, 0, 360, Easing.EasingOption.EaseInOutQuad);

        JSONArray ingredients = transformedCocktail.getJSONArray("items");
        JSONArray sizes       = transformedCocktail.getJSONArray("sizes");

        List<PieEntry> list = new ArrayList<>();

        final JSONObject itemSize = new JSONObject();

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            int size = Math.round(Float.valueOf(sizes.getString(inx)) * 100);

            String label = ingredients.getString(inx);

            itemSize.put(label, size);

            PieEntry tmp = new PieEntry(size, label);
            list.add(tmp);
        }

        mChart.setHighlightPerTapEnabled(true);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                PieEntry entry = (PieEntry) e;
                String label = entry.getLabel();
                String newText = label + "\n" + itemSize.get(label) + "%";
                mChart.setCenterText(newText);
            }

            @Override
            public void onNothingSelected()
            {
                mChart.setCenterText(title);
            }
        });

        PieData data = new PieData();
        PieDataSet dataSet = new PieDataSet(list, "Test");
        dataSet.setColors(Simple.jsonToColorArray(transformedCocktail.getJSONArray("color")));
        // dataSet.setValueTextSize(30f);
        dataSet.setValueTextSize(15f);
        dataSet.setValueTextColor(Color.WHITE);

        data.setDataSet(dataSet);

        mChart.setData(data);

        int margins = SimpleLayout.getSize(20);

//        PieChart.LayoutParams parms = new PieChart.LayoutParams(-1, 750);
//        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(-1, 1000);
//        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(-1, 750);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, SimpleLayout.getSize(350));
        params.setMargins(margins, margins, margins, margins);

        mChart.setLayoutParams(params);

        layout.addView(mChart);
    }

    private int getCombinedColor(JSONObject tranformedCocktail)
    {
        int color;

        JSONArray colors = tranformedCocktail.getJSONArray("color");
        JSONArray sizes  = tranformedCocktail.getJSONArray("sizes");

        int redBucket   = 0;
        int greenBucket = 0;
        int blueBucket  = 0;

        for (int inx = 0; inx < colors.length(); inx++)
        {
            int   colorTmp = Color.parseColor(colors.getString(inx));
            float size     = Float.valueOf(sizes.getString(inx));

            redBucket   += (int) (((float) Color.red(colorTmp))   * size);
            greenBucket += (int) (((float) Color.green(colorTmp)) * size);
            blueBucket  += (int) (((float) Color.blue(colorTmp))  * size);
        }

        color = Color.rgb(
            redBucket,
            greenBucket,
            blueBucket);

        return color;
    }

    private void setUpToolbar(Map<String, Object> layout)
    {
        Toolbar toolbar = (Toolbar) layout.get("toolbar");
        toolbar.setTitle(cocktail.getString("name"));

        int mixColor = getCombinedColor(transformedCocktail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            int darkColor = Simple.getDarkColor(mixColor);

            window.setStatusBarColor(darkColor);
        }

        toolbar.setBackgroundColor(mixColor);
    }

    private void addButton(LinearLayout scrollLayout, final JSONObject cocktail, String title)
    {
        Button addFavorite = new Button(this);
        addFavorite.setText(title);
        addFavorite.setBackgroundColor(Color.CYAN);
        addFavorite.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

        addFavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String uuid = cocktail.getString("id");
                Toast.makeText(getApplicationContext(), uuid, Toast.LENGTH_SHORT).show();

                final FavoritesManager favoritesManager = new FavoritesManager(
                    getApplicationContext());

                favoritesManager.add(uuid);
            }
        });

        scrollLayout.addView(addFavorite);
    }

    private ShoppingListManager shoppingList;

    private void addRowListener(LinearLayout row, final String ingredient)
    {
        row.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Simple.toastL(
                    getApplicationContext(),
                    Simple.getText(getApplicationContext(), R.string.add_to_shopping) + ingredient);
                shoppingList.addToList(ingredient);
            }
        });

    }

    private void createIngredientsBox(LinearLayout scrollLayout)
    {
        LinearLayout layout = SimpleLayout.getBox(this, R.string.ingredients);
        scrollLayout.addView(layout);

        JSONArray ingredients = cocktail.getJSONArray("ingredients");

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject ingredientPart = ingredients.getJSONObject(inx);
            LinearLayout row = SimpleLayout.getIngredientRow(this, ingredientPart);

            addRowListener(row, ingredientPart.getString("ingredient"));

            layout.addView(row);
        }

        TextView note = new TextView(this);
        note.setText(R.string.note_touch_add_shopping);
        note.setGravity(Gravity.END);
        note.setTextColor(Color.GRAY);
        note.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(note, 0, 10, 0, 0);

        layout.addView(note);
    }

    private void createDescription(LinearLayout scrollLayout)
    {
        LinearLayout layout = SimpleLayout.getBox(this, R.string.description);
        scrollLayout.addView(layout);

        String descriptionText = cocktail.optString("description", Simple.getText(this, R.string.mix_all_ingredients));

        TextView description = new TextView(this);
        // description.setText(cocktail.getString("description"));
        description.setText(descriptionText);
        description.setTextSize(15f);
        description.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        layout.addView(description);
    }

    private TextView bulletView()
    {
        TextView bulletView = new TextView(this);
        bulletView.setPadding(0, SimpleLayout.getSize(40), 0, 0);
        bulletView.setTextSize(20f);
        bulletView.setTypeface(Typeface.MONOSPACE);
        bulletView.setTextColor(Color.BLACK);
        bulletView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

        return bulletView;
    }

    private void updateBulletView(TextView bulletView)
    {
        JSONArray items      = transformedCocktail.getJSONArray("items");
        JSONArray sizes      = transformedCocktail.getJSONArray("sizes");
        JSONArray sizesUnity = transformedCocktail.getJSONArray("unities");

        StringBuilder newText = new StringBuilder();

        for (int inx = 0; inx < transformedCocktail.getInt("ingredients"); inx++)
        {
            newText.append("&#8226; ");

            float size = Float.valueOf(sizes.getString(inx));

            int newSize = Math.round(glassSize * size);

//            if (newSize >= 10) newSize = 5 * (Math.round(newSize / 5));
            if (newSize >= 10) newSize = (newSize + 4) / 5 * 5;

            String formattedString = String.format(Locale.ROOT, "%-3d", newSize);
            newText.append(formattedString.replace(" ", "&#160;"));

            newText.append(" ");

            newText.append(sizesUnity.getString(inx));

            newText.append(" ");
            newText.append(items.getString(inx));
            newText.append("<br/>");
        }

        bulletView.setText(Html.fromHtml(newText.toString()));
    }

    private void createSeekBar(LinearLayout layout)
    {
        //
        // Size View
        //

        final TextView sizeView = new TextView(this);
        sizeView.setGravity(Gravity.CENTER);
        sizeView.setTextColor(Color.BLACK);
        sizeView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        SimpleLayout.setPadding(sizeView, 0, 75, 0, 75);

        layout.addView(sizeView);

        //
        // SeekBar Layout
        //

        LinearLayout seekBarLayout = SimpleLayout.getLayout(this);
        seekBarLayout.setGravity(Gravity.CENTER_VERTICAL);
        seekBarLayout.setOrientation(LinearLayout.HORIZONTAL);

        layout.addView(seekBarLayout);

        //
        // Description
        //

        TextView description = new TextView(this);
        description.setText(R.string.glass_size);
        description.setGravity(Gravity.CENTER);
        description.setLayoutParams(SimpleLayout.getLayoutParams(-2, -1));

        seekBarLayout.addView(description);

        //
        // SeekBar
        //

        SeekBar seekBar = new SeekBar(this);
        seekBar.setLayoutParams(SimpleLayout.getLayoutParams(-1, -1));

        seekBarLayout.addView(seekBar);

        //
        // bulletView
        //

        final TextView bulletView = bulletView();
        layout.addView(bulletView);

        //
        // Event
        //

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                int size = progress * 10;
                sizeView.setText(size + " ml");

                glassSize = size;

                updateBulletView(bulletView);
                updateAlcoholPercentView();

                if (progress <= 9) return;
                sizeView.setTextSize(Math.round(progress * 0.9));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {}
        });

        seekBar.setProgress(glassSize / 10);
    }

    private void createCalculator(LinearLayout scrollLayout)
    {
        LinearLayout layout = SimpleLayout.getBox(this, R.string.calculator);
        scrollLayout.addView(layout);

        createSeekBar(layout);
    }

    private void updateAlcoholPercentView()
    {
        JSONArray items = transformedCocktail.getJSONArray("items");
        JSONArray sizes = transformedCocktail.getJSONArray("sizes");

        float size = 0.0f;

        for (int inx = 0; inx < transformedCocktail.getInt("ingredients"); inx++)
        {
            String item = items.getString(inx);

            JSONObject ingredient = MainActivity.simpleCocktail.ingredients_db.getJSONObject(item);
            if (! ingredient.getBoolean("alcoholic")) continue;

            float itemsSize = Float.valueOf(sizes.getString(inx));
            float boozePart = itemsSize * ((float) ingredient.getDouble("alcohol_content"));

            size += boozePart;
        }

        if (alcoholPercentView == null) return;

        if (! alcoholic)
        {
            alcoholPercentView.setText(R.string.non_alcoholic);
            return;
        }

        alcoholPercentView.setText("~" + Math.round(size) + "%  (" + Math.round(glassSize * (size / 100f)) + "ml)");
    }

    private void createBoozePercent(LinearLayout scrollLayout)
    {
        LinearLayout layout = SimpleLayout.getBox(this, R.string.alcohol_percent);
        scrollLayout.addView(layout);

        alcoholPercentView = new TextView(this);
        alcoholPercentView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));
        layout.addView(alcoholPercentView);

        updateAlcoholPercentView();
    }

    private void createRawJsonBox(LinearLayout scrollLayout)
    {
        LinearLayout layout = SimpleLayout.getBox(this, "Json");
        scrollLayout.addView(layout);

        TextView text = new TextView(this);
        text.setTypeface(Typeface.MONOSPACE);
        text.setText(cocktail.toString(2));
        text.setLayoutParams(SimpleLayout.getLayoutParams(-1, -2));

        layout.addView(text);
    }

    private String textToShare()
    {
        StringBuilder text = new StringBuilder();

        text.append("*");
        text.append(cocktail.getString("name"));
        text.append("*\n\n");

        JSONArray ingredients = cocktail.getJSONArray("ingredients");

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject ingredientPart = ingredients.getJSONObject(inx);
            String ingredient = ingredientPart.getString("ingredient");

            String title = "-  ";

            double amount = ingredientPart.optDouble("amount", 1);
            if (amount % 1 == 0) title += Math.round(amount) + " ";
            else title += amount + " ";

            title += ingredientPart.getString("unit") + " ";
            title += ingredient;

            text.append(title);
            text.append("\n");
        }

        text.append("\n");
        text.append("*Description:* ");
        text.append(cocktail.getString("description"));

        return text.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (randomCocktail)
        {
            Simple.addMenuItem(menu, Simple.getText(this, R.string.random), R.drawable.random);
        }

        Simple.addMenuItem(menu, Simple.getText(this, R.string.share), R.drawable.share);

        if (! ownCocktail) return true;

        Simple.addMenuItem(menu, Simple.getText(this, R.string.edit),   R.drawable.edit);
        Simple.addMenuItem(menu, Simple.getText(this, R.string.qrCode), R.drawable.qrcode);
        Simple.addMenuItem(menu, Simple.getText(this, R.string.delete), R.drawable.garbage);

        return true;
    }

    private void deleteAction()
    {
        DialogInterface.OnClickListener yesEvent = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Simple.toastS(getApplicationContext(), "Delete: " + cocktail.getString("name"));

                String id = cocktail.getString("id");

                OwnCreationManager manager = new OwnCreationManager(getApplicationContext());
                manager.delete(id);

                // Bug --> Update == favorite delete
                FavoritesManager favoritesManager = new FavoritesManager(getApplicationContext());
                favoritesManager.remove(id);

                finish();
            }
        };

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(Simple.getText(this, R.string.delete) + ": " + cocktail.getString("name") + "?");
        dialog.setMessage(R.string.sure_delete_cocktail);
        dialog.setPositiveButton(R.string.yes, yesEvent);
        dialog.setNegativeButton(R.string.no,  null);
        dialog.show();
    }

    private void editAction()
    {
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra("cocktail", cocktail.toString());

        startActivity(intent);

        finish();
    }

    private void openQrActivity()
    {
        Intent intent = new Intent(getApplicationContext(), ShareQrActivity.class);
        intent.putExtra("cocktail", cocktail.toString());

        startActivity(intent);
    }

    private void random()
    {
        main(new JSONObject(getRandomCocktail()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String title = item.getTitle().toString();

        if (Simple.getText(this, R.string.share).equals(title))  Simple.shareIntent(this, textToShare());
        if (Simple.getText(this, R.string.delete).equals(title)) deleteAction();
        if (Simple.getText(this, R.string.edit).equals(title))   editAction();
        if (Simple.getText(this, R.string.qrCode).equals(title)) openQrActivity();

        if (Simple.getText(this, R.string.random).equals(title)) random();

        return super.onOptionsItemSelected(item);
    }

    private void main(JSONObject newCocktail)
    {
        cocktail = newCocktail;
        transformedCocktail = MainActivity.simpleCocktail.transformCocktail(newCocktail);
        alcoholic = CocktailHelper.isAlcoholic(cocktail);

        LinearLayout scrollLayout = (LinearLayout) layout.get("scrollLayout");

        scrollLayout.removeAllViewsInLayout();

        // Toolbar Color
        setUpToolbar(layout);

        // CocktailView
        createCocktailView(scrollLayout);

        // TextView
        createIngredientsBox(scrollLayout);

        // ## AdMarker MEDIUM_RECTANGLE this --> scrollLayout ##

        createDescription(scrollLayout);

        // Chart
        addChart(scrollLayout, cocktail.getString("name"));

        // ## AdMarkerBroke MEDIUM_RECTANGLE this --> scrollLayout ##

        // Calculator
        createCalculator(scrollLayout);

        // Booze Percent
        createBoozePercent(scrollLayout);

        // JSON
        // createRawJsonBox(scrollLayout);
    }

    private boolean checkOwn(String id)
    {
        OwnCreationManager ownCreationManager = new OwnCreationManager(this);

        JSONArray ownCocktails = ownCreationManager.getCocktails();

        for (int inx = 0; inx < ownCocktails.length(); inx++)
        {
            JSONObject cocktail = ownCocktails.getJSONObject(inx);

            if (id.equals(cocktail.getString("id"))) return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        JSONObject intentCocktail = new JSONObject(intent.getStringExtra("cocktail"));

        ownCocktail = checkOwn(intentCocktail.getString("id"));

        randomCocktail = intent.getBooleanExtra("random", false);

        shoppingList = new ShoppingListManager(this);
        layout = SimpleLayout.createContentLayout(this, intentCocktail.getString("name"));

        main(intentCocktail);
    }
}
