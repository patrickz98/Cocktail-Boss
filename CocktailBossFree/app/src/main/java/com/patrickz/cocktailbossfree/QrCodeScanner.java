package com.patrickz.cocktailbossfree;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.zxing.Result;

import org.json.simple.JSONArray;
import org.json.simple.JSONException;
import org.json.simple.JSONObject;

import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends Activity implements ZXingScannerView.ResultHandler
{
    private final static String LOGTAG = "QrCodeScanner";
    private final static int MY_PERMISSIONS_REQUEST_CAMERA = 1234;

    private static ZXingScannerView mScannerView;
    private boolean qrStart = false;

    private LinearLayout contentLayout;

    public void startScanner()
    {
        mScannerView = new ZXingScannerView(this);

        mScannerView.
setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setFlash(false);
        mScannerView.startCamera();

        mScannerView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -1));

        qrStart = true;

        contentLayout.addView(mScannerView);
    }

    public void nuke()
    {
        if (mScannerView != null)
        {
            contentLayout.removeAllViewsInLayout();

            mScannerView.stopCamera();
            mScannerView = null;
        }
    }

    private boolean checkScan(String qrResult)
    {
        JSONObject test;

        try
        {
            test = new JSONObject(qrResult);
        }
        catch (JSONException exc)
        {
            // exc.printStackTrace();
            return false;
        }

        if (! test.has("id")) return false;
        if (! test.has("name")) return false;
        if (! test.has("glass")) return false;
        if (! test.has("ingredients")) return false;
        if (! test.has("description")) return false;

        return true;
    }

    private void addNewIngredients(String qrResult)
    {
        JSONObject json = new JSONObject(qrResult);
        JSONArray ingredients = json.getJSONArray("ingredients");

        for (int inx = 0; inx < ingredients.length(); inx++)
        {
            JSONObject ingredientPart = ingredients.getJSONObject(inx);
            String ingredient = ingredientPart.getString("ingredient");

            if (MainActivity.simpleCocktail.ingredients_db.has(ingredient)) continue;

            MyIngredientsManager myIngredients = new MyIngredientsManager(this);
            myIngredients.addIngredient(ingredient, ingredientPart.getJSONObject("ingredientInfo"));
        }
    }

    @Override
    public void handleResult(Result rawResult)
    {
        String qrResult = rawResult.getText();

        ToneGenerator toneG = new ToneGenerator(
            AudioManager.STREAM_NOTIFICATION,
            ToneGenerator.MAX_VOLUME);

        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

        if (checkScan(qrResult))
        {
//            stopScanner();

            addNewIngredients(qrResult);

            Intent intent = new Intent(getApplicationContext(), AddActivity.class);
            intent.putExtra("cocktail", qrResult);
            startActivity(intent);

            finish();

            return;
        }

        Simple.toastS(this, R.string.scan_error);
        recreate();
    }

    private void checkPermission()
    {
        int compat = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (compat == PackageManager.PERMISSION_GRANTED)
        {
            startScanner();
            return;
        }

        // Should we show an explanation?
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
//        {
//            // Show an expanation to the user *asynchronously* -- don't block
//            // this thread waiting for the user's response! After the user
//            // sees the explanation, try again to request the permission.
//        }
//        else
//        {
//            // No explanation needed, we can request the permission.
//        }

        ActivityCompat.requestPermissions(
            this,
            new String[]{ Manifest.permission.CAMERA },
            MY_PERMISSIONS_REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode != MY_PERMISSIONS_REQUEST_CAMERA) return;

        // If request is cancelled, the result arrays are empty.
        if ((grantResults.length > 0) && (grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED))
        {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.

            startScanner();
            return;
        }
            // permission denied, boo! Disable the
            // functionality that depends on this permission.

        Log.d(LOGTAG, "Camera permission denied");
        SimpleLayout.createNoneNote(this, contentLayout, R.string.camera_settings);

//             AlertDialog.Builder builder = new AlertDialog.Builder(this);
//             builder.setMessage("Please alw");
//             builder.setCancelable(false);
//             builder.setPositiveButton("OK", null);
//
//             AlertDialog alert = builder.create();
//             alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Map<String, Object> layout = SimpleAd.createContentLayout(this, R.string.scanner);
        contentLayout = (LinearLayout) layout.get("contentLayout");
        contentLayout.removeAllViewsInLayout();

        checkPermission();
    }
}