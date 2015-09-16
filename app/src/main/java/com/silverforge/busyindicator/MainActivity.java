package com.silverforge.busyindicator;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TimeUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.silverforge.controls.BusyIndicator;
import com.silverforge.controls.Circle;
import com.silverforge.controls.CircleAngleAnimation;

public class MainActivity extends AppCompatActivity {
    private BusyIndicator busyIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        busyIndicator = (BusyIndicator) findViewById(R.id.finiteBusyIndicator);

        busyIndicator.setMaxValue(102);
        new BusyIndicatorAsyncTask().execute();

//        Circle circle = (Circle) findViewById(R.id.circle);
//        CircleAngleAnimation animation = new CircleAngleAnimation(circle, 240);
//        animation.setDuration(1000);
//        circle.startAnimation(animation);
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

    class BusyIndicatorAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {

            for (int i = 0; i <= 102; i += 3) {
                final int a = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        busyIndicator.setValue(a);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
