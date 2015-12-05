package io.bloc.android.blocly.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import io.bloc.android.blocly.BloclyApplication;
import io.bloc.android.blocly.R;
import io.bloc.android.blocly.ui.adapter.ItemAdapter;

/**
 * Created by Steven on 11/24/2015.
 */
public class BloclyActivity extends Activity {

    private ItemAdapter itemAdapter;
    private static String TAG = BloclyActivity.class.getSimpleName();
    private ImageView backgroundImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocly);

        itemAdapter = new ItemAdapter();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_activity_blocly);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);




        String imageUrl = BloclyApplication.getSharedDataSource().getBackgroundImageUrl();
        backgroundImageView = (ImageView)findViewById(R.id.iv_activity_blocly_background);
        ImageLoadingListener listener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.e(TAG, "onLoadingFailed: " + failReason.toString() + " for URL: " + imageUri);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                backgroundImageView.setImageBitmap(loadedImage);
                backgroundImageView.setVisibility(View.VISIBLE);
                Log.i(TAG, "onLoadingCompleted: for URL: " + imageUri, new Throwable("Throwable test"));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                // Attempt a retry
                ImageLoader.getInstance().loadImage(imageUri, this);
            }
        };
        ImageLoader.getInstance().loadImage(imageUrl, listener);
    }
}
