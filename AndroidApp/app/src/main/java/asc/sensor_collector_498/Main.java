package asc.sensor_collector_498;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Environment;
import java.io.FileOutputStream;
import java.io.*;//File;
import java.util.*;
import android.widget.TextView;


public class Main extends ActionBarActivity implements SensorEventListener{

    //private static final String TAG = activity_main.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor aSensor , gSensor , mSensor, lSensor;
    String aName;// = "MPL Accelerometer";
    String gName;// = "MPL Gyroscope";
    String mName;// = "AKM Magnetic Field";
    String lName;
    int acounter =0;
    int gcounter =0;
    int mcounter =0;
    int lcounter=0;
    int limit=0;
    int steps=0;


    TextView mag_c;
    TextView acc_c;
    TextView gyro_c;
    TextView writer_c;
    TextView steps_c;
    int internal_lim = 4000;  //7000 = 35

    long stime;
    String fname= "south_to_west.csv"; //stable

    String aprev="";
    String mprev="";
    String gprev="";
    String lprev="";
    float medium;

    List<Statistic> data  = new ArrayList<Statistic>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String bacon;
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensor=  mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        lSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this,aSensor,SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,gSensor,SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,lSensor,SensorManager.SENSOR_DELAY_FASTEST);
        aName=aSensor.getName();
        gName=gSensor.getName();
        mName=mSensor.getName();
        lName=lSensor.getName();

        acc_c = (TextView) findViewById(R.id.acc_c);
        gyro_c = (TextView) findViewById(R.id.gyro_c);
        mag_c = (TextView) findViewById(R.id.mag_c);
        writer_c = (TextView) findViewById(R.id.writerstat);
        steps_c = (TextView) findViewById(R.id.StepC);


        Log.i("MNAME", getFilesDir().getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        String op= event.sensor.getName();

        if (op.equals(aName) && gcounter <internal_lim)
        {

            if(acounter==1)
            {
                stime=event.timestamp;
            }
            String a_x_y_z=Float.toString((event.values[0])) + ", ";
            a_x_y_z = a_x_y_z + Float.toString((event.values[1])) + ", ";
            a_x_y_z = a_x_y_z + Float.toString((event.values[2])) + ", ";
            aprev = a_x_y_z;
            if ((acounter > 0) && (gcounter > 0) && (mcounter > 0) && (lcounter > 0) ){
                Statistic astat = new Statistic();
                astat.accel_data = aprev;
                astat.gyro_data = gprev;
                astat.mag_data = mprev;
                astat.light=lprev;
                medium = event.timestamp - stime;
                medium = medium/1000000;
                astat.timestamp = Math.round(medium);
                data.add(astat);


                acc_c.setText(Float.toString(acounter));
            }
            acounter++;

        }

        else if(op.equals(gName) && gcounter <internal_lim)
        {

            String g_x_y_z=Float.toString((event.values[0])) + ", ";
            g_x_y_z = g_x_y_z + Float.toString((event.values[1])) + ", ";
            g_x_y_z = g_x_y_z + Float.toString((event.values[2])) + ", ";
            gprev=g_x_y_z;
            if ((acounter > 0) && (gcounter > 0) && (mcounter > 0) & (lcounter>0) ){
                Statistic astat = new Statistic();
                astat.accel_data = aprev;
                astat.gyro_data = gprev;
                astat.mag_data = mprev;
                astat.light=lprev;

                medium = event.timestamp - stime;
                medium = medium/1000000;
                astat.timestamp = Math.round(medium);
                data.add(astat);
            }
            gcounter++;
            gyro_c.setText(Float.toString(gcounter));

        }
        else if(op.equals(lName) && gcounter <internal_lim)
        {

            String l_x_y_z=Float.toString(Math.round(event.values[0]));
            lprev=l_x_y_z;
            if ((acounter > 0) && (gcounter > 0) && (mcounter > 0) & (lcounter>0) ){
                Statistic astat = new Statistic();
                astat.accel_data = aprev;
                astat.gyro_data = gprev;
                astat.mag_data = mprev;
                astat.light=lprev;

                medium = event.timestamp - stime;
                medium = medium/1000000;
                astat.timestamp = Math.round(medium);
                data.add(astat);
            }
            lcounter++;

        }
        else if(op.equals(mName) && gcounter <internal_lim)
        {

            String m_x_y_z=Float.toString((event.values[0]))+", ";
            m_x_y_z = m_x_y_z + Float.toString((event.values[1])) + ", ";
            m_x_y_z = m_x_y_z + Float.toString((event.values[2])) + ", ";
            mprev=m_x_y_z;
            if ((acounter > 0) && (gcounter > 0) && (mcounter > 0) & (lcounter>0) ){
                Statistic astat = new Statistic();
                astat.accel_data = aprev;
                astat.gyro_data = gprev;
                astat.mag_data = mprev;
                astat.light=lprev;

                medium = event.timestamp - stime;
                medium = medium/1000000;
                astat.timestamp = Math.round(medium);
                data.add(astat);
            }
            mcounter++;
            mag_c.setText(Float.toString(gcounter));

        }
        else if(gcounter >= (internal_lim-1) && limit == 0)
        {
            Log.i("Pasing","Passed to Parseit");
            //ParseIt();
            spawnit_new();
            limit=1;
        }


    }

    public class Statistic{
        public String accel_data;
        public String gyro_data;
        public String mag_data;
        public String light;
        public float timestamp;


    }
    public void spawnit_new() {
        writer_c.setText("NOW WRITINGGGG!!!");
        String setupstring = "TS, ACCEL_X, ACCEL_Y, ACCEL_Z, GYRO_X, GYRO_Y, GYRO_Z, MAG_X, MAG_Y, MAG_Z, LIGHT\n";
        File myFile = new File("/sdcard/" + fname);

        try {
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            fOut.write(setupstring.getBytes());
            for (int a = 0; a < data.size(); a++) {
                String writing = Float.toString(data.get(a).timestamp) + ", ";
                writing = writing + data.get(a).accel_data + data.get(a).gyro_data;
                writing=writing+ data.get(a).mag_data + data.get(a).light+"\n";
                fOut.write(writing.getBytes());

            }
            fOut.close();

            writer_c.setText("Done to " + myFile.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        //mSensorManager.unregisterListener(this);
    }
}
