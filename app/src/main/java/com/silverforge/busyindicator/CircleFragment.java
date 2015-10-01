package com.silverforge.busyindicator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silverforge.controls.BusyIndicator;

import java.util.Random;

public class CircleFragment extends Fragment {

    private BusyIndicator infiniteCircleBusyIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        BusyIndicator circleBusyIndicator = (BusyIndicator) view.findViewById(R.id.finiteCircleBusyIndicator);
        infiniteCircleBusyIndicator = (BusyIndicator) view.findViewById(R.id.infiniteCircleBusyIndicator);

        circleBusyIndicator.setMaxValue(102);
        new BusyIndicatorAsyncTask(getActivity(), circleBusyIndicator).execute();
        new InfiniteBusyModifier().execute();

        return view;
    }    

    class InfiniteBusyModifier extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            final Random rand = new Random();

            while (true) {
                final int a = rand.nextInt(3) + 1;

                getActivity().runOnUiThread(new Runnable() {
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

}
