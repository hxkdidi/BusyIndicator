package com.silverforge.busyindicator;

import android.app.Notification;
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

public class CircleFragment extends Fragment {

    private BusyIndicator infiniteCircleBusyIndicator;
    private BusyIndicator circleBusyIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        circleBusyIndicator = (BusyIndicator) view.findViewById(R.id.finiteCircleBusyIndicator);
        infiniteCircleBusyIndicator = (BusyIndicator) view.findViewById(R.id.infiniteCircleBusyIndicator);

        new InfiniteBusyModifier().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        circleBusyIndicator.setMaxValue(102);
        circleBusyIndicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new FiniteCircleBusyIndicatorAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                return true;
            }
        });

        return view;
    }    

    class InfiniteBusyModifier extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            final Random rand = new Random();

            FragmentActivity activity = getActivity();

            while (true) {
                final int a = rand.nextInt(3) + 1;

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        infiniteCircleBusyIndicator.setAngleModifier(a);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class FiniteCircleBusyIndicatorAsync extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            final Random rand = new Random();
            int i = 0;

            FragmentActivity activity = getActivity();

            while (i < 103) {
                final int a = i;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    circleBusyIndicator.setValue(a);
                    }
                });

                i = i + rand.nextInt(20) + 1;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                circleBusyIndicator.setValue(103);
                }
            });

            return null;
        }
    }
}
