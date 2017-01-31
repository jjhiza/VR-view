package com.google.devrel.vrviewapp.com.google.devrel.viewapp;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by jay on 1/30/17.
 */

public class ImageLoaderTask extends AsyncTask<AssetManager, Void, Bitmap> {

    public static final String TAG = "ImageLoader Task";
    private final String assetName;
    private final WeakReference<VrPanoramaView> viewReference;
    private final VrPanoramaView.Options viewOptions;

    private static WeakReference<Bitmap> lastBitmap = new WeakReference<>(null);
    private static String lastName;

    public ImageLoaderTask(VrPanoramaView view, VrPanoramaView.Options viewOptions, String assetName) {
        viewReference = new WeakReference<>(view);
        this.viewOptions = viewOptions;
        this.assetName = assetName;
    }

    @Override
    protected Bitmap doInBackground(AssetManager... params) {

        AssetManager assetManager = params[0];

        if (assetName.equals(lastName) && lastBitmap.get() != null) {
            return lastBitmap.get();
        }

        try (InputStream inputStream = assetManager.open(assetName)) {
            Bitmap b = BitmapFactory.decodeStream(inputStream);
            lastBitmap = new WeakReference<>(b);
            lastName = assetName;
            return b;

        } catch (IOException e) {
            Log.e(TAG, "Could not decode default bitmap: " + e);
            return null;
        }
    }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            final VrPanoramaView vw = viewReference.get();

            if (vw != null && bitmap != null) {
                vw.loadImageFromBitmap(bitmap, viewOptions);
            }
        }
    }
