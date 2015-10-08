package com.silverforge.busyindicator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.silverforge.controls.BusyIndicator;

import java.util.Random;

public class MultiLoaderFragment extends Fragment {
    private BusyIndicator multiBusyIndicator1;
    private BusyIndicator multiBusyIndicator2;
    private BusyIndicator multiBusyIndicator3;
    private TextView loadingStatusText;
    private Button multiLaunchButton;

    boolean busy1Done;
    boolean busy2Done;
    boolean busy3Done;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_loader, container, false);
        multiBusyIndicator1 = (BusyIndicator) view.findViewById(R.id.finiteMultiBusyIndicator1);
        multiBusyIndicator2 = (BusyIndicator) view.findViewById(R.id.finiteMultiBusyIndicator2);
        multiBusyIndicator3 = (BusyIndicator) view.findViewById(R.id.finiteMultiBusyIndicator3);

        loadingStatusText = (TextView) view.findViewById(R.id.loadingStatusText);
        multiLaunchButton = (Button) view.findViewById(R.id.multiLaunchButton);

        multiBusyIndicator1.setMaxValue(83);
        multiBusyIndicator2.setMaxValue(530);
        multiBusyIndicator3.setMaxValue(962);

        multiLaunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingStatusText.setText("loading...");
                busy1Done = false;
                busy2Done = false;
                busy3Done = false;

                multiBusyIndicator1.setValue(0);
                multiBusyIndicator2.setValue(0);
                multiBusyIndicator3.setValue(0);

                new FiniteMultiBusyIndicatorAsync1().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new FiniteMultiBusyIndicatorAsync2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new FiniteMultiBusyIndicatorAsync3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

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
                    busy1Done = true;
                    if (busy1Done && busy2Done && busy3Done)
                        loadingStatusText.setText("done");
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

                i = i + rand.nextInt(90) + 1;

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
                    busy2Done = true;
                    if (busy1Done && busy2Done && busy3Done)
                        loadingStatusText.setText("done");
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

                i = i + rand.nextInt(190) + 1;

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
                    busy3Done = true;
                    if (busy1Done && busy2Done && busy3Done)
                        loadingStatusText.setText("done");
                }
            });

            return null;
        }
    }
}
