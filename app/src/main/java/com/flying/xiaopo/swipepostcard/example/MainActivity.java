package com.flying.xiaopo.swipepostcard.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.flying.xiaopo.swipepostcard.R;
import com.flying.xiaopo.swipepostcard.SwipePostcard;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SwipePostcard postcard = (SwipePostcard) findViewById(R.id.postcards);
        List<Bean> data = new ArrayList<>();

        int[] resIds = new int[]{R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10, R.drawable.p11, R.drawable.p12};
        for (int i = 0; i < 12; i++) {
            Bean bean = new Bean(resIds[i], "世界上最好的金泰妍->" + i);
            data.add(bean);
        }

        PostcardAdapter adapter = new PostcardAdapter(this, data);
        if (postcard != null) {
            postcard.setAdapter(adapter);
            postcard.setMaxPostcardNum(3);
            postcard.setOffsetY(0);
            postcard.setMinDistance(200);
            postcard.setOnPostcardRunOutListener(new SwipePostcard.OnPostcardRunOutListener() {
                @Override
                public void onPostcardRunOut() {
                    Toast.makeText(MainActivity.this, "Run out!", Toast.LENGTH_SHORT).show();
                }
            });

            postcard.setOnPostcardDismissListener(new SwipePostcard.OnPostcardDismissListener() {
                @Override
                public void onPostcardDismiss(int direction) {
                    if (direction == SwipePostcard.DIRECTION_LEFT) {
                        Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
                    } else if (direction == SwipePostcard.DIRECTION_RIGHT) {
                        Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Toast.makeText(MainActivity.this, postcard.getMaxPostcardNum() + " ", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
