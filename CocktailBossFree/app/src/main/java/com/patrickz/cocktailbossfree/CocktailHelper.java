package com.patrickz.cocktailbossfree;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CocktailHelper
{
    public JSONObject cocktails;
    public JSONObject ingredients_db;

    CocktailHelper(Context context)
    {
        this.cocktails = new JSONObject(Simple.getRaw(context, R.raw.cocktail_recipes));
        this.ingredients_db = new JSONObject(Simple.getRaw(context, R.raw.ingredients_db));

        addOwnCocktails(context);
        addOwnIngredients(context);
    }

    private void addOwnCocktails(Context context)
    {
        OwnCreationManager ownCreationManager = new OwnCreationManager(context);
        JSONArray owns = ownCreationManager.getCocktails();

        for (int inx = 0; inx < owns.length(); inx++)
        {
            JSONObject cocktail = owns.getJSONObject(inx);
            cocktails.put(cocktail.getString("id"), cocktail);
        }
    }

    private void addOwnIngredients(Context context)
    {
        MyIngredientsManager myIngredientsManager = new MyIngredientsManager(context);
        JSONObject owns = myIngredientsManager.getIngredients();

        for (String name: owns.keySet())
        {
            JSONObject cocktail = owns.getJSONObject(name);
            ingredients_db.put(name, cocktail);
        }
    }

    public String getColor(String ingredient)
    {
        return ingredients_db.getJSONObject(ingredient).getString("color");
    }

    private JSONArray getColors(JSONArray ingredients)
    {
        JSONArray colorArray = new JSONArray();

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            colorArray.put(ingredients_db.getJSONObject(ingredients.getString(inx)).getString("color"));
        }

        return colorArray;
    }

    private JSONArray getIngredients(JSONObject cocktail)
    {
        JSONArray ingredients = cocktail.getJSONArray("ingredients");

        JSONArray ingredientsArray = new JSONArray();

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject part = ingredients.getJSONObject(inx);

            if (!part.has("amount")) continue;

            ingredientsArray.put(part.getString("ingredient"));
        }

        return ingredientsArray;
    }

    private JSONArray getSizes(JSONObject cocktail)
    {
        JSONArray ingredients = cocktail.getJSONArray("ingredients");

        float sum = 0.0f;

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject part = ingredients.getJSONObject(inx);

            if (!part.has("amount")) continue;

            sum += part.getInt("amount");
        }

        JSONArray relativeSizes = new JSONArray();

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject part = ingredients.getJSONObject(inx);

            if (!part.has("amount")) continue;

            float size = (float) part.getInt("amount");
            float relSize = size / sum;

            relativeSizes.put("" + relSize);
        }

        return relativeSizes;
    }

    private JSONArray getUnities(JSONObject cocktail)
    {
        JSONArray ingredients = cocktail.getJSONArray("ingredients");

        JSONArray sizesUnity = new JSONArray();

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject part = ingredients.getJSONObject(inx);
            sizesUnity.put(part.getString("unit"));
        }

        return sizesUnity;
    }

    public JSONObject transformCocktail(JSONObject cocktail)
    {
        JSONArray ingredients = getIngredients(cocktail);
        JSONArray colors = getColors(ingredients);
        JSONArray sizes = getSizes(cocktail);
        JSONArray unities = getUnities(cocktail);

        JSONObject json = new JSONObject();
        json.put("ingredients", ingredients.length());
        json.put("items", ingredients);
        json.put("color", colors);
        json.put("sizes", sizes);
        json.put("unities", unities);

        return json;
    }

    public static String getIngredientString(JSONObject json)
    {
        String title = "";

        double amount = json.optDouble("amount", 1);
        if (amount % 1 == 0) title += Math.round(amount) + " ";
        else title += amount + " ";

        title += json.getString("unit") + " ";
        title += json.getString("ingredient");

        return title;
    }

    public static String getAllIngredientString(JSONObject cocktail)
    {
        StringBuilder str = new StringBuilder();

        JSONArray ingredients = cocktail.getJSONArray("ingredients");

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject obj = ingredients.getJSONObject(inx);
            str.append(getIngredientString(obj));
            str.append("\n");
        }

        return str.toString();
    }

    public static boolean isAlcoholic(JSONObject cocktail)
    {
        JSONObject ingredients_db = MainActivity.simpleCocktail.ingredients_db;
        JSONArray ingredients = cocktail.getJSONArray("ingredients");

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            String ingredient = ingredients.getJSONObject(inx).getString("ingredient");

            if (ingredients_db.getJSONObject(ingredient).getBoolean("alcoholic")) return true;
        }

        return false;
    }
}
