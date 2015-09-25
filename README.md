
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

#### Dark rectangle background ####

###### Busy indicator ######

```xml
<com.silverforge.controls.BusyIndicator
    app:background_is_visible="true"
    app:background_color="@color/dark_background"
    app:bigpoint_color="@color/dark_bigpoint"
    app:smallpoint_color="@color/dark_smallpoint"

    android:layout_width="300dp"
    android:layout_height="150dp"
    android:layout_margin="10dp"
    />
```

###### Progress indicator ######

```xml
<com.silverforge.controls.BusyIndicator
    android:id="@+id/finiteRectangleBusyIndicator"

    app:background_is_visible="true"
    app:background_shape="rounded_rectangle"
    app:background_color="@color/dark_background"
    app:bigpoint_color="@color/dark_bigpoint"
    app:smallpoint_color="@color/dark_smallpoint"
    app:bigpoint_count="20"

    app:infinite="false"
    app:max_value="100"
    app:percentage_is_visible="true"
    app:percentage_decimal_places="1"

    android:layout_width="300dp"
    android:layout_height="150dp"
    android:layout_margin="10dp"
    />
```

###### UI ######

![DarkRect](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/dark_rect.png)

![DarkRectFull](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/dark_rect_full.png)


#### Light circle background ####

###### Busy indicator ######

```xml
<com.silverforge.controls.BusyIndicator
    app:background_is_visible="true"
    app:background_shape="circle"
    app:background_color="@color/light_background"
    app:bigpoint_color="@color/light_bigpoint"
    app:smallpoint_color="@color/light_smallpoint"

    android:layout_width="300dp"
    android:layout_height="150dp"
    android:layout_margin="10dp"
    />
```

###### Progress indicator ######

```xml
<com.silverforge.controls.BusyIndicator
    android:id="@+id/finiteCircleBusyIndicator"

    app:background_is_visible="true"
    app:background_shape="circle"
    app:background_color="@color/light_background"
    app:bigpoint_color="@color/light_bigpoint"
    app:smallpoint_color="@color/light_smallpoint"
    app:bigpoint_count="20"

    app:infinite="false"
    app:max_value="100"
    app:percentage_is_visible="true"
    app:percentage_decimal_places="1"

    android:layout_width="300dp"
    android:layout_height="150dp"
    android:layout_margin="10dp"
    />
```

###### UI ######

![LightCircle](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/light_circle.png)

![LightCircleFull](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/light_circle_full.png)


#### Transparent background ####

###### Busy indicator ######

```xml
<com.silverforge.controls.BusyIndicator
    app:bigpoint_color="@color/trans_bigpoint"
    app:smallpoint_color="@color/light_smallpoint"

    android:layout_width="300dp"
    android:layout_height="150dp"
    android:layout_margin="10dp"
    />
```

###### Progress indicator ######

```xml
<com.silverforge.controls.BusyIndicator
    android:id="@+id/finiteTransBusyIndicator"

    app:bigpoint_color="@color/trans_bigpoint"
    app:smallpoint_color="@color/trans_smallpoint"
    app:bigpoint_count="20"

    app:infinite="false"
    app:max_value="100"
    app:percentage_is_visible="true"
    app:percentage_decimal_places="1"

    android:layout_width="300dp"
    android:layout_height="150dp"
    android:layout_margin="10dp"
    />
```

###### UI ######

![Trans](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/trans.png)

![TransFull](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/trans_full.png)


#### How to control progress indicator from code #####

Basically just set the *maxValue()* of busyIndicator and after that set the current value via *setValue()* frequently like the *// NOTE* indicates in the code below.

*Note : You can set the maxValue with xml attribute as well like* **app:max_value="756"**

For example if you have a progress from 0 to 756, you just set *maxValue()* to 756 and via *setValue()* inform the busyIndicator about the progress. BusyIndicator will do the percentage calculations.

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




