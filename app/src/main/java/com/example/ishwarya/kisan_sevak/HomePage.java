package com.example.ishwarya.kisan_sevak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;

public class HomePage extends AppCompatActivity {
    GridLayout gridLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        gridLayout=(GridLayout)findViewById(R.id.grid);
        setSingleEvent(gridLayout);

    }

    private void setSingleEvent(GridLayout gridLayout) {
        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView cardView=(CardView)gridLayout.getChildAt(i);
            final int finalI= i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI==1)
                    {
                        Intent intent = new Intent(HomePage.this,GetInfo.class);
                        startActivity(intent);
                    }
                    if(finalI==0)
                    {
                        Intent intent = new Intent(HomePage.this,MainActivity.class);
                        startActivity(intent);
                    }
                    /*
                    if(finalI==2)
                    {
                        Intent intent = new Intent(MainActivity.this,main4.class);
                        startActivity(intent);
                    }
                    **/
                }
            });
        }
    }
}
