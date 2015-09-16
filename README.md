
# README #


Contents
--------

- [What is this repository for?](#what-is-this-repository-for)
- [How do I get set up?](#how-do-i-get-set-up)
- [Configuration](#configuration)


## What is this repository for? ##

The BusyIndicator is a progress indicator with finite and infinite state.

## How do I get set up? ##

Soon...


##### Issues/contact #####

Please do not hesitate to raise any issue you find related to ElasticRawClient [here](https://github.com/silverforge/ElasticRawClient/issues)

## Configuration ##

#### Minimal busy indicator #####

```xml
<com.silverforge.controls.BusyIndicator
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:layout_centerHorizontal="true"
    android:layout_centerVertical="true"/>
```

![MinimalBusyIndicatorScreenShot](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/minimal_busy_indicator.png)


#### Pimped BusyIndicator ####

```xml
<com.silverforge.controls.BusyIndicator
    app:bigpoint_color="@color/bigpoint_small"
    app:smallpoint_color="@color/smallpoint_small"
    app:bigpoint_count="12"
    app:background_is_visible="true"
    app:background_color="@color/busy_background_large"
    app:background_shape="circle"
    android:layout_width="50dp"
    android:layout_height="50dp"
    android:layout_centerHorizontal="true"
    android:layout_centerVertical="true"/>
```

![PimpedBusyIndicatorScreenShot](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/pimped_busy_indicator.png)


#### Progress indicator #####


```xml
<com.silverforge.controls.BusyIndicator
    android:id="@+id/finiteBusyIndicator"
    app:bigpoint_count="20"
    app:infinite="false"
    app:max_value="100"
    app:background_is_visible="true"
    app:percentage_is_visible="true"
    app:bigpoint_color="@color/bigpoint_large"
    app:smallpoint_color="@color/smallpoint_large"
    android:layout_width="300dp"
    android:layout_height="300dp"
    android:layout_centerHorizontal="true"
    android:layout_centerVertical="true"/>
```

```java
        busyIndicator = (BusyIndicator) findViewById(R.id.finiteBusyIndicator);

        // NOTE : Set the maximum value
        busyIndicator.setMaxValue(102);

        new BusyIndicatorAsyncTask().execute();

...

    class BusyIndicatorAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {

            for (int i = 0; i <= 102; i += 3) {
                final int a = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        
                        // NOTE : You can set the current value of BusyIndicator
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
```

![ProgressIndicatorScreenShot](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/progress_indicator.png)




