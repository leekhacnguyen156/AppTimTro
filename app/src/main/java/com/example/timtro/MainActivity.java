package com.example.timtro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.timtro.Activity.vLoad;
import com.example.timtro.ModelApp.appAccountDetails;

import java.text.SimpleDateFormat;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    public static ProgressDialog load;
    public static final String DATE_FORMAT = "HH:mm:ss dd-MM-yyyy";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private ImageView imageViewLoGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewLoGo = (ImageView) findViewById( R.id.imageViewLoGo );
        Animation animation = AnimationUtils.loadAnimation( MainActivity.this, R.anim.anim_rotate );
        imageViewLoGo.startAnimation( animation );

        Paper.init(this);
        if(Paper.book().read(appAccountDetails.matinh) == null){
            Paper.book().write(appAccountDetails.matinh, "84");
        }

        Thread time = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(6000);
                            finish();
                            Intent intent = new Intent(MainActivity.this, vLoad.class);
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        time.start();
    }
}