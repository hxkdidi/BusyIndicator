package com.silverforge.busyindicator;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.silverforge.controls.BusyIndicator;

import java.util.Random;

public class RectangleFragment extends Fragment {
    private BusyIndicator rectangleBusyIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rectangle, container, false);
        rectangleBusyIndicator = (BusyIndicator) view.findViewById(R.id.finiteRectangleBusyIndicator);

        rectangleBusyIndicator.setMaxValue(314);
        rectangleBusyIndicator.setSingleColor(Color.CYAN);
        rectangleBusyIndicator.setCustomText("perec");
        rectangleBusyIndicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new FiniteRectangleBusyIndicatorAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                return true;
            }
        });
        return view;
    }

    class FiniteRectangleBusyIndicatorAsync extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            final Random rand = new Random();
            int i = 0;

            FragmentActivity activity = getActivity();

            while (i < 315) {
                final int a = i;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    rectangleBusyIndicator.setValue(a);
                    }
                });

                i = i + rand.nextInt(60) + 1;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                rectangleBusyIndicator.setValue(314);
                }
            });

            return null;
        }
    }
}
