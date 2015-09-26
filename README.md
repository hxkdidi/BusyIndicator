
# README #


Contents
--------

- [What is this repository for?](#what-is-this-repository-for)
- [How do I get set up?](#how-do-i-get-set-up)
- [Configuration](#configuration)


## What is this repository for? ##

The BusyIndicator is a progress indicator with determined and indeterminate state. 

Current version is [![Download](https://api.bintray.com/packages/silverforge/maven/busyindicator/images/download.svg) ](https://bintray.com/silverforge/maven/busyindicator/_latestVersion)

[![BusyIndicatorDemo](https://j.gifs.com/KRoYzb.gif)](https://youtu.be/rOqh9h-DEDw)

## How do I get set up? ##

```groovy
dependencies {

...

    compile 'com.silverforge.controls:busyindicator:1.0.0'
}

```

##### Issues/contact #####

Please do not hesitate to raise any issue you find related to ElasticRawClient [here](https://github.com/silverforge/ElasticRawClient/issues)

## Configuration ##

You can control the busyindicator behavior or look & feel with the following attributes:

###### Indeterminate ######
* **bigpoint_color** - The foreground color of the points (circles) on outer radius. By default it is gray.
* **smallpoint_color** - The foreground color of the point (circle) on the inner radius. By default it is black.
* **bigpoint_count** - The count of points (circles) on outer radius. By default it is 4.
* **background_is_visible** - *true* of *false*. If true you have a light gray rectangle background with rounded corner by default .
* **background_color** - The background color if visible. By default it is light gray.
* **background_shape** - *rounded_rectangle* or *circle* The shape of background.

###### Determined ######

All of the attributes mentioned in previous section plus

* **infinite** - *true* of *false*. If true the busyindicator is active otherwise the load indicator is active.
* **percentage_is_visible** - *true* of *false*. If true the percentage text appears.
* **percentage_decimal_places** - Could be : [0, 1, 2]. The decimal places of the percentage text.
* **max_value** - The max value of the progress. 

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


###### UI ######

![DarkRect](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/dark_rect.png)

![DarkRectFull](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/dark_rect_full.png)

![LightCircle](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/light_circle.png)

![LightCircleFull](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/light_circle_full.png)

![Trans](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/trans.png)

![TransFull](https://raw.githubusercontent.com/silverforge/BusyIndicator/master/assets/trans_full.png)


