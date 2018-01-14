package com.patrickz.cocktailbossfree;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MyIngredientsManager
{
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private OwnCreationManager ownCreationManager;

    private ShoppingListManager shoppingList;

    private IHaveManager myBarManager;

    MyIngredientsManager(Context context)
    {
        this.settings = context.getSharedPreferences("MyIngredientsActivity", Context.MODE_PRIVATE);
        this.editor = settings.edit();
        this.ownCreationManager = new OwnCreationManager(context);
        this.shoppingList = new ShoppingListManager(context);
        this.myBarManager = new IHaveManager(context);
    }

    private void commit()
    {
        editor.apply();
        editor.commit();
    }

    public boolean addIngredient(String name, JSONObject ingredient)
    {
        if (settings.contains(name)) return false;
        if (MainActivity.simpleCocktail.ingredients_db.has(name)) return false;

//        editor.putString(name, ingredient.toString());
//        MainActivity.simpleCocktail.ingredients_db.put(name, ingredient);
//
//        commit();
        update(name, ingredient);

        return true;
    }

    public void update(String name, JSONObject ingredient)
    {
        editor.putString(name, ingredient.toString());
        MainActivity.simpleCocktail.ingredients_db.put(name, ingredient);

        commit();
    }

    private void replaceIngredientFromCocktails(String oldName, String newName)
    {
        JSONArray cocktails = ownCreationManager.getCocktails();

        for (int inx = 0; inx < cocktails.length(); inx++)
        {
            JSONObject cocktail = cocktails.getJSONObject(inx);
            JSONArray ingredients = cocktail.getJSONArray("ingredients");

            JSONArray newIngredients = new JSONArray();

            for (int iny = 0; iny < ingredients.length(); iny++)
            {
                JSONObject ingredientPart = ingredients.getJSONObject(iny);
                String ingredientName = ingredientPart.getString("ingredient");

                if (ingredientName.equals(oldName))
                {
                    ingredientPart.put("ingredient", newName);
                }

                newIngredients.put(ingredientPart);
            }

            cocktail.put("ingredients", newIngredients);
            ownCreationManager.forceUpdate(cocktail);
        }
    }

    public void replace(String oldName, String newName)
    {
        replaceIngredientFromCocktails(oldName, newName);

        editor.remove(oldName);
        commit();

        shoppingList.removeFromList(oldName);
        myBarManager.remove(oldName);

        if (! MainActivity.simpleCocktail.ingredients_db.has(oldName)) return;
        MainActivity.simpleCocktail.ingredients_db.remove(oldName);
    }

    private void removeIngredientFromCocktails(String ingredient)
    {
        JSONArray cocktails = ownCreationManager.getCocktails();

        for (int inx = 0; inx < cocktails.length(); inx++)
        {
            JSONObject cocktail = cocktails.getJSONObject(inx);
            JSONArray ingredients = cocktail.getJSONArray("ingredients");

            JSONArray newIngredients = new JSONArray();

            for (int iny = 0; iny < ingredients.length(); iny++)
            {
                JSONObject ingredientPart = ingredients.getJSONObject(iny);
                String ingredientName = ingredientPart.getString("ingredient");

                if (ingredientName.equals(ingredient)) continue;

                newIngredients.put(ingredientPart);
            }

            cocktail.put("ingredients", newIngredients);
            ownCreationManager.forceUpdate(cocktail);
        }
    }

    public void remove(String ingredient)
    {
        // Problem: Cocktails with this ingredient

        removeIngredientFromCocktails(ingredient);

        editor.remove(ingredient);
        commit();

        shoppingList.removeFromList(ingredient);
        myBarManager.remove(ingredient);

        if (! MainActivity.simpleCocktail.ingredients_db.has(ingredient)) return;
        MainActivity.simpleCocktail.ingredients_db.remove(ingredient);
    }

    public JSONObject getIngredients()
    {
        JSONObject ingredients = new JSONObject();

        for (String name: settings.getAll().keySet())
        {
            String stringJson = settings.getString(name, "MyIngredientsManager: Error");
            JSONObject ingredient = new JSONObject(stringJson);
            ingredients.put(name, ingredient);
        }

        return ingredients;
    }
}
