package com.sukinsan.pebble.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sukinsan.pebble.entity.Cache;
import com.sukinsan.pebble.entity.Weather;
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
public class GetWeatherTask extends AsyncTask<Void,Void,Void> {
    public final static String TAG = GetWeatherTask.class.getSimpleName();
    public Context context;
    Weather weather = new Weather();

    public GetWeatherTask(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            String url = String.format("http://api.openweathermap.org/data/2.5/forecast/daily?cnt=1&units=metrics&lat=49.4402308&lon=32.0667807&units=metric");

            HttpUriRequest request = new HttpGet(url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
                JSONObject item = json.getJSONArray("list").getJSONObject(0);

                JSONObject city = json.getJSONObject("city");
                JSONObject coord = city.getJSONObject("coord");
                JSONObject temp = item.getJSONObject("temp");
                JSONObject weat = item.getJSONArray("weather").getJSONObject(0);

                weather.setCity(city.getString("name"));
                weather.setCountry(city.getString("country"));
                weather.setTempDay(temp.getDouble("day"));
                weather.setTempMax(temp.getDouble("max"));
                weather.setTempMin(temp.getDouble("min"));
                weather.setTempEvening(temp.getDouble("eve"));
                weather.setTempMorning(temp.getDouble("morn"));
                weather.setTempNight(temp.getDouble("night"));
                weather.setLat(coord.getDouble("lat"));
                weather.setLon(coord.getDouble("lon"));
                weather.setStatus(weat.getString("main"));
                weather.setDescription(weat.getString("description"));
                weather.setIco(weat.getString("icon"));
                weather.setLastUpdate(System.currentTimeMillis());

                SystemUtils.getCache(context,new Cache.CallBack() {
                    @Override
                    public boolean run(Cache cache){
                        cache.setWeather(weather);
                        return true;
                    }
                });

            }

        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
