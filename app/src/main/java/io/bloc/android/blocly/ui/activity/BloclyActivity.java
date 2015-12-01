package io.bloc.android.blocly.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import io.bloc.android.blocly.BloclyApplication;
import io.bloc.android.blocly.R;

/**
 * Created by Steven on 11/24/2015.
 */
public class BloclyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocly);

        String title = BloclyApplication.getSharedDataSource().getFeeds().get(0).getTitle();
        TextView textView = (TextView)findViewById(R.id.myLabel);
        textView.setText(title);

        Toast.makeText(this, title, Toast.LENGTH_LONG).show();
    }
}
