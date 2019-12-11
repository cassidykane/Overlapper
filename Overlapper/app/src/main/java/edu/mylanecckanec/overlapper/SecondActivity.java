package edu.mylanecckanec.overlapper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class SecondActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    Button backButton;
    ImageView firstImageView;
    ImageView secondImageView;
    SeekBar opacitySeekBar;
    String firstUrl;
    String secondUrl;
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        backButton = (Button) findViewById(R.id.backButton);
        firstImageView = (ImageView) findViewById(R.id.imageView1);
        secondImageView = (ImageView) findViewById(R.id.imageView2);
        opacitySeekBar = (SeekBar) findViewById(R.id.opacitySeekBar);

        opacitySeekBar.setOnSeekBarChangeListener(this);
        Picasso.get().load(firstUrl).into(firstImageView);
        Picasso.get().load(secondUrl).into(secondImageView);
        firstImageView.setImageAlpha(50);
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putInt("opacity",opacitySeekBar.getProgress());

        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int opacity = savedValues.getInt("opacity",50);
        opacitySeekBar.setProgress(opacity);
        firstImageView.setImageAlpha(opacity);

        Intent intent = getIntent();
        firstUrl = intent.getExtras().getString(MainActivity.FIRST_URL);
        secondUrl = intent.getExtras().getString(MainActivity.SECOND_URL);

        Picasso.get().load(firstUrl).into(firstImageView);
        Picasso.get().load(secondUrl).into(secondImageView);
    }

    public void backClick(View v) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //int opacity = progress / 100;
        firstImageView.setImageAlpha(progress);
        Log.e("progress", String.valueOf(progress));
        Log.e("alpha", String.valueOf(firstImageView.getImageAlpha()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
