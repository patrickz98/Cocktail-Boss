package com.patrickz.cocktailboss;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class IHaveSearchHelper
{
    private IHaveActivity activity;
    private JSONObject allCocktails;

    IHaveSearchHelper(IHaveActivity activity)
    {
        this.activity = activity;
        this.allCocktails = MainActivity.simpleCocktail.cocktails;
    }

    private boolean checkCocktail(JSONArray ingredients)
    {
        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            String ingredient = ingredients.getJSONObject(inx).getString("ingredient");
            if (! activity.ownIngredients.contains(ingredient)) return false;
        }

        return true;
    }

    private void applyOptions(JSONObject cocktail, Map<Integer, Boolean> alcOptions, JSONArray found)
    {
        if (alcOptions.get(R.string.all_alcoholic))
        {
            found.put(cocktail);
            return;
        }

        if (alcOptions.get(R.string.alcoholic) && CocktailHelper.isAlcoholic(cocktail))
        {
            found.put(cocktail);
            return;
        }

        if (alcOptions.get(R.string.non_alcoholic) && (! CocktailHelper.isAlcoholic(cocktail)))
        {
            found.put(cocktail);
        }
    }

    public JSONArray find(Map<Integer, Boolean> alcOptions)
//    public JSONArray find()
    {
        JSONArray matchingCocktails = new JSONArray();

        for (String id: allCocktails.keySet())
        {
            JSONObject cocktail = allCocktails.getJSONObject(id);
            JSONArray ingredients = cocktail.getJSONArray("ingredients");

            if (! checkCocktail(ingredients)) continue;

//            matchingCocktails.put(cocktail);
            applyOptions(cocktail, alcOptions, matchingCocktails);
        }

        return matchingCocktails;
    }
}
