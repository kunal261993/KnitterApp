package com.example.knitter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class PostDetails extends AppCompatActivity {
    CardView cardView;
    TextView userId,postId,title,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4885ed"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        cardView=(CardView) findViewById(R.id.card_view_details);
        cardView.setRadius(30);
        cardView.setCardElevation(10);
        userId=(TextView)findViewById(R.id.usedIdDetail);
        postId=(TextView)findViewById(R.id.postIdDetail);
        title=(TextView)findViewById(R.id.titleDetail);
        description=(TextView)findViewById(R.id.descriptionDetail);
        userId.setText( "USER ID : "+getIntent().getStringExtra("userId"));
        postId.setText( "POST ID : "+getIntent().getStringExtra("postId"));
        title.setText( getIntent().getStringExtra("title"));
        description.setText( getIntent().getStringExtra("description"));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
