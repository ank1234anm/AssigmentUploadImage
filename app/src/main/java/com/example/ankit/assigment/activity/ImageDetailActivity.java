package com.example.ankit.assigment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.ankit.assigment.R;
import com.example.ankit.assigment.utilities.TouchImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ImageDetailActivity extends AppCompatActivity implements  View.OnClickListener{
    private TouchImageView imgPrscrbd;
    private FloatingActionButton fabCross;



    private ContentLoadingProgressBar progressPaginationLoading ;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        findViews();
        getIntentValue();
    }

    private void getIntentValue() {
        uri = getIntent().getStringExtra("uri");
        if(uri !=null)
        {
            Glide.with(this)
                    .load(uri).placeholder(R.drawable.actor)
                    .into(imgPrscrbd);
        }
    }

    private void findViews() {
        imgPrscrbd = (TouchImageView)findViewById( R.id.imgPrscrbd );
        fabCross = (FloatingActionButton)findViewById( R.id.fabCross );
        fabCross.setOnClickListener( this );

    }


        @Override
        public void onClick (View v){
            if (v == fabCross) {
                finish();
            }
        }
    }
