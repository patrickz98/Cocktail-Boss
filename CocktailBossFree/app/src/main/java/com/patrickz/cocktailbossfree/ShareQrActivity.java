package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.EnumMap;
import java.util.Map;

public class ShareQrActivity extends Activity
{
    private final static String LOGTAG = "ShareQrActivity";

    private Bitmap qrBitmap(String text, int widHei)
    {
        widHei = SimpleLayout.getSize(widHei);

        try
        {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, widHei, widHei, hints);

            final int widthm  = matrix.getWidth();
            final int heightm = matrix.getHeight();

            int[] pixels = new int[widthm * heightm];

            for (int y = 0; y < heightm; y++)
            {
                int offset = y * widthm;

                for (int x = 0; x < widthm; x++)
                {
                    pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(widthm, heightm, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthm, 0, 0, widthm, heightm);

            return bitmap;
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }

        return null;
    }

    private void addQrView(LinearLayout contentLayout, String text)
    {
        LinearLayout layout = SimpleLayout.getLayoutPadding(this, -1, -1, 30, 30, 30, 30);
        layout.setGravity(Gravity.CENTER);

        contentLayout.addView(layout);

        LinearLayout box = SimpleLayout.getBox(this);
        box.setLayoutParams(SimpleLayout.getLayoutParams(-2, -2));
        SimpleLayout.setPadding(box, 10, 10, 10, 10);

        layout.addView(box);

        ImageView image = new ImageView(this);
        image.setLayoutParams(SimpleLayout.getLayoutParams(-2, -2));
        image.setImageBitmap(qrBitmap(text, 300));

        box.addView(image);
    }

    private void checkIngredients(JSONObject shareCocktail)
    {
        JSONArray ingredients = shareCocktail.getJSONArray("ingredients");

        JSONObject myIngredients = (new MyIngredientsManager(this)).getIngredients();

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject ingredientPart = ingredients.getJSONObject(inx);
            String ingredient = ingredientPart.getString("ingredient");

            if (! myIngredients.has(ingredient)) continue;

            ingredientPart.put("ingredientInfo", myIngredients.getJSONObject(ingredient));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String cocktail = intent.getStringExtra("cocktail");

        JSONObject shareCocktail = new JSONObject(cocktail);

        Map<String, Object> layout = SimpleAd.createContentLayout(this, shareCocktail.getString("name"));
        LinearLayout contentLayout = (LinearLayout) layout.get("contentLayout");
        contentLayout.removeAllViewsInLayout();

        checkIngredients(shareCocktail);

        addQrView(contentLayout, shareCocktail.toString());
    }
}
