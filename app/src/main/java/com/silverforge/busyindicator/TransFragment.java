package com.silverforge.busyindicator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silverforge.controls.BusyIndicator;

import java.util.Random;

public class TransFragment extends Fragment {
    private BusyIndicator transBusyIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trans, container, false);
        transBusyIndicator = (BusyIndicator) view.findViewById(R.id.finiteTransBusyIndicator);

        transBusyIndicator.setMaxValue(83);
//        new BusyIndicatorAsyncTask(getActivity(), transBusyIndicator).execute();
        new FiniteTransparentBusyIndicatorAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }

    class FiniteTransparentBusyIndicatorAsync extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            final Random rand = new Random();
            int i = 0;

            FragmentActivity activity = getActivity();

            while (i < 84) {
                final int a = i;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    transBusyIndicator.setValue(a);
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
                    transBusyIndicator.setValue(83);
                }
            });

            return null;
        }
    }
}
