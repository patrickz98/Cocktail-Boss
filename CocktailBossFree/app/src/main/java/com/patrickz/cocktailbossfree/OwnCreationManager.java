package com.patrickz.cocktailbossfree;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class OwnCreationManager
{
    private static final String LOGTAG = "OwnCreationManager";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private Context context;

    OwnCreationManager(Context context)
    {
        this.context = context;
        this.settings = context.getSharedPreferences("own", Context.MODE_PRIVATE);
        this.editor = settings.edit();
    }

    public boolean addCocktail(JSONObject cocktail)
    {
        String name = cocktail.getString("name");
        String nameMd5 = Simple.md5(name);

        if ((settings.contains(nameMd5) || MainActivity.simpleCocktail.cocktails.has(nameMd5))) return false;

        cocktail.put("id", nameMd5);

        editor.putString(nameMd5, cocktail.toString());
        editor.apply();

        MainActivity.simpleCocktail.cocktails.put(nameMd5, cocktail);

        return true;
    }

    public boolean updateCocktail(JSONObject cocktail)
    {
        String oldId = cocktail.getString("id");

        String name = cocktail.getString("name");
        String newId = Simple.md5(name);
        cocktail.put("id", newId);

        if ((! oldId.equals(newId)) && (settings.contains(newId) || MainActivity.simpleCocktail.cocktails.has(newId)))
        {
            return false;
        }

        delete(oldId);
        editor.putString(newId, cocktail.toString());
        editor.apply();

        MainActivity.simpleCocktail.cocktails.put(newId, cocktail);

        FavoritesManager favoritesManager = new FavoritesManager(context);

        if (favoritesManager.includes(oldId))
        {
            favoritesManager.remove(oldId);
            favoritesManager.add(newId);
        }

        return true;
    }

    public void forceUpdate(JSONObject cocktail)
    {
        String name = cocktail.getString("name");
        String newId = Simple.md5(name);
        cocktail.put("id", newId);

        editor.putString(newId, cocktail.toString());
        editor.apply();

        MainActivity.simpleCocktail.cocktails.put(newId, cocktail);
    }

    public JSONArray getCocktails()
    {
        JSONArray cocktails = new JSONArray();

        for (String id: settings.getAll().keySet())
        {
            String stringJson = settings.getString(id, "OwnCreationManager: Error");
            JSONObject cocktail = new JSONObject(stringJson);
            cocktails.put(cocktail);
        }

        return cocktails;
    }

    public void delete(String id)
    {
        editor.remove(id);
        editor.apply();

        if (! MainActivity.simpleCocktail.cocktails.has(id)) return;

        MainActivity.simpleCocktail.cocktails.remove(id);
    }
}
