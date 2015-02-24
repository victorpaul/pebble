package com.sukinsan.pebble.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sukinsan.pebble.entity.Cache;
import com.sukinsan.pebble.utils.SystemUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Victor on 2/24/2015.
 */
public class GetWeather extends AsyncTask<Void,Void,Void> {
    public Context context;

    public GetWeather(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            String url = String.format("api.openweathermap.org/data/2.5/forecast/daily?cnt=1&units=metrics&lat=49.4402308&lon=32.0667807&units=metric");
            HttpUriRequest request = new HttpGet(url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));

                JSONObject city = json.getJSONObject("city");
                JSONObject temp = json.getJSONArray("list").getJSONObject(0).getJSONObject("temp");

                final String location = city.getString("country") + ", " + city.getString("name");
                final String temperature = temp.getString("min") + "/" + temp.getString("max")+ " C";

                SystemUtils.getCache(context,new Cache.CallBack() {
                    @Override
                    public void run(Cache cache){
                        cache.setWeatherLocation(location);
                        cache.setLastWeatherStatus(temperature);
                    }
                },true);

            }

        } catch (Exception e) {
            Log.i("TAG", e.getMessage());
        }

        return null;
    }
}
