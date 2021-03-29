package com.example.mostafaproject;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class detection<listtrue> extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    double ax, ay, az;   // these are the acceleration in x,y and z axis
    Button startt;
    Button stopp;
    int flag = 0;
    int flag2 = 0;


    Button loct;

    ArrayList<Double> listtrue = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);



        sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        startt = (Button) findViewById(R.id.start);

        stopp = (Button) findViewById(R.id.stop);
        startt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = 1;
            }
        });
        stopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;



            }
        });




    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }
    public void onSensorChanged(SensorEvent event) {
        ArrayList<Double> list = new ArrayList<Double>();


        double[] n1 = {-0.1393740326166153
                ,-0.3823525309562683
                ,-0.10573074221611023
                ,0.03102598525583744
                ,0.1865704357624054
                ,-0.12958674132823944
                ,-0.02669701911509037
                ,-0.09757155179977417
                ,0.07276324927806854
                ,-0.007527017500251532
                ,-0.05284155532717705
                ,0.1432662457227707
                ,-0.016473019495606422
                ,-0.04964655637741089
                ,-0.011797741055488586
                ,-0.03606902062892914
                ,-0.054758552461862564
                ,-0.012436742894351482
                ,-0.011148016899824142
                ,0.03619244322180748
                ,0.043795254081487656
                ,0.09194398671388626
                ,0.22533643245697021
                ,-0.1621757447719574
                ,0.15477897226810455
                ,-0.17978957295417786
                ,0.12090124189853668
                ,-0.09144901484251022
                ,0.07602345198392868
                ,1.3025943189859394
                ,0.1486019790172577
                ,-0.06200055405497551
                ,0.05487126111984253
                ,-0.00305401673540473
                ,-0.007259555626660585
                ,-0.0011477423831820488
                ,-0.058008018881082535
                ,0.07005944848060608
                ,-0.0075377412140369415
                ,-0.28314903378486633
                ,0.1118074506521225
                ,0.036766257137060165
                ,-0.1099800169467926
                ,-0.18021556735038757
                ,-0.030967744067311287
                ,-0.11487900465726852
                ,-0.6143096089363098
                ,-0.11318575590848923
                ,0.7848330140113831
                ,-0.6036595106124878
                ,0.23911625146865845
                ,0.3066479563713074
                ,0.10605644434690475
                ,0.17244726419448853
                ,-0.12489000707864761
                ,-0.45519858598709106
                ,0.019939256832003593
                ,0.21995696425437927
                ,-0.4948166310787201
                ,0.032080259174108505
                ,0.1828949898481369
                ,0.0809224471449852
                ,-0.25951671600341797
                ,-0.18453001976013184
                ,0.17783744633197784
                ,0.008437257260084152
        };
        if (flag == 1){

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                ax = event.values[0];
                ay = event.values[1];
                az = event.values[2];
                list.add(ax);
                list.add(ay);
                list.add(az);
                Double[] item = list.toArray(new Double[list.size()]);
                DTN dtn = new DTN(n1, item);
              if(dtn.warpingDistance>0.069500&& dtn.warpingDistance<0.079) {
                  Toast.makeText(detection.this, "pump" + dtn, Toast.LENGTH_SHORT).show();
 if(dtn.warpingDistance>0.06942&& dtn.warpingDistance<0.079) {
                    Toast.makeText(detection.this, "pump" + dtn, Toast.LENGTH_SHORT).show();



               }
               else {
                    Toast.makeText(detection.this, "baaad" + dtn, Toast.LENGTH_SHORT).show();



                }
               }
               else {
                    Toast.makeText(detection.this, "baaad" + dtn, Toast.LENGTH_SHORT).show();



                }
            }



        /* for (int i = 0; i < list.size(); i += 3) {


            System.out.println("Orientation X (Roll) :");
            System.out.println(list.get(i));
            System.out.println("Orientation y (Roll) :");
            System.out.println(list.get(i + 1));
            System.out.println("Orientation z (Roll) :");
            System.out.println(list.get(i + 2));777


        }
 */


        }
    }
    class DTN {


        protected double[] seq1;
        protected Double[] seq2;
        protected int[][] warpingPath;

        protected int n;
        protected int m;
        protected int K;

        protected double warpingDistance;

        public DTN(double[] sample, Double[] templete) {
            seq1 = sample;
            seq2 = templete;

            n = seq1.length;
            m = seq2.length;
            K = 1;

            warpingPath = new int[n + m][2];        // max(n, m) <= K < n + m
            warpingDistance = 0.0;

            this.compute();
        }

        public DTN(double list, double listtrue) {

        }


        public void compute() {
            double accumulatedDistance = 0.0;

            double[][] d = new double[n][m];        // local distances
            double[][] D = new double[n][m];        // global distances

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    d[i][j] = distanceBetween(seq1[i], seq2[j]);
                }
            }

            D[0][0] = d[0][0];

            for (int i = 1; i < n; i++) {
                D[i][0] = d[i][0] + D[i - 1][0];
            }

            for (int j = 1; j < m; j++) {
                D[0][j] = d[0][j] + D[0][j - 1];
            }

            for (int i = 1; i < n; i++) {
                for (int j = 1; j < m; j++) {
                    accumulatedDistance = Math.min(Math.min(D[i - 1][j], D[i - 1][j - 1]), D[i][j - 1]);
                    accumulatedDistance += d[i][j];
                    D[i][j] = accumulatedDistance;
                }
            }
            accumulatedDistance = D[n - 1][m - 1];

            int i = n - 1;
            int j = m - 1;
            int minIndex = 1;

            warpingPath[K - 1][0] = i;
            warpingPath[K - 1][1] = j;

            while ((i + j) != 0) {
                if (i == 0) {
                    j -= 1;
                } else if (j == 0) {
                    i -= 1;
                } else {        // i != 0 && j != 0
                    double[] array = {D[i - 1][j], D[i][j - 1], D[i - 1][j - 1]};
                    minIndex = this.getIndexOfMinimum(array);

                    if (minIndex == 0) {
                        i -= 1;
                    } else if (minIndex == 1) {
                        j -= 1;
                    } else if (minIndex == 2) {
                        i -= 1;
                        j -= 1;
                    }
                } // end else
                K++;
                warpingPath[K - 1][0] = i;
                warpingPath[K - 1][1] = j;
            } // end while
            warpingDistance = accumulatedDistance / K;

            this.reversePath(warpingPath);
        }


        protected void reversePath(int[][] path) {
            int[][] newPath = new int[K][2];
            for (int i = 0; i < K; i++) {
                for (int j = 0; j < 2; j++) {
                    newPath[i][j] = path[K - i - 1][j];
                }
            }
            warpingPath = newPath;
        }

        public double getDistance() {
            return warpingDistance;
        }


        protected double distanceBetween(double p1, double p2) {
            return (p1 - p2) * (p1 - p2);
        }

        protected int getIndexOfMinimum(double[] array) {
            int index = 0;
            double val = array[0];

            for (int i = 1; i < array.length; i++) {
                if (array[i] < val) {
                    val = array[i];
                    index = i;
                }
            }
            return index;
        }

        public String toString() {
            String retVal = "Warping Distance: " + warpingDistance + "\n";
            retVal += "Warping Path: {";
            for (int i = 0; i < K; i++) {
                retVal += "(" + warpingPath[i][0] + ", " + warpingPath[i][1] + ")";
                retVal += (i == K - 1) ? "}" : ", ";

            }
            return retVal;
        }


    }
}