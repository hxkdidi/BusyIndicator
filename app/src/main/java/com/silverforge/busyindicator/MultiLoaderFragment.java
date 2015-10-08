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

public class MultiLoaderFragment extends Fragment {
    private BusyIndicator multiBusyIndicator1;
    private BusyIndicator multiBusyIndicator2;
    private BusyIndicator multiBusyIndicator3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_loader, container, false);
        multiBusyIndicator1 = (BusyIndicator) view.findViewById(R.id.finiteMultiBusyIndicator1);
        multiBusyIndicator2 = (BusyIndicator) view.findViewById(R.id.finiteMultiBusyIndicator2);
        multiBusyIndicator3 = (BusyIndicator) view.findViewById(R.id.finiteMultiBusyIndicator3);

        multiBusyIndicator1.setMaxValue(83);
        multiBusyIndicator2.setMaxValue(530);
        multiBusyIndicator3.setMaxValue(962);

        new FiniteMultiBusyIndicatorAsync1().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new FiniteMultiBusyIndicatorAsync2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new FiniteMultiBusyIndicatorAsync3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return view;
    }

    class FiniteMultiBusyIndicatorAsync1 extends AsyncTask {
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
                    multiBusyIndicator1.setValue(a);
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
                    multiBusyIndicator1.setValue(83);
                }
            });

            return null;
        }
    }

    class FiniteMultiBusyIndicatorAsync2 extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            final Random rand = new Random();
            int i = 0;

            FragmentActivity activity = getActivity();

            while (i < 531) {
                final int a = i;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    multiBusyIndicator2.setValue(a);
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
                    multiBusyIndicator2.setValue(530);
                }
            });

            return null;
        }
    }

    class FiniteMultiBusyIndicatorAsync3 extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            final Random rand = new Random();
            int i = 0;

            FragmentActivity activity = getActivity();

            while (i < 963) {
                final int a = i;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    multiBusyIndicator3.setValue(a);
                    }
                });

                i = i + rand.nextInt(230) + 1;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiBusyIndicator3.setValue(962);
                }
            });

            return null;
        }
    }
}
