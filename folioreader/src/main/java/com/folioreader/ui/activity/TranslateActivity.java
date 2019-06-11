package com.folioreader.ui.activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.folioreader.R;
import com.folioreader.Constants;
import android.widget.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TranslateActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    TextView trans;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);



        Intent intent = getIntent();
        message = intent.getStringExtra(Constants.SELECTED_WORD);
        trans = findViewById(R.id.lay_trans);
        try {
            run("https://translation.googleapis.com/language/translate/v2?key=AIzaSyA-vHyWSougg79UpAVLx-NyaIM82rX4dRo&q="+message+"&target=zh-cn");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void run(String url) throws Exception{

        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                trans.setText("None");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray translations = data.getJSONArray("translations");

                    trans.setText(message + "\n\n" + translations.getJSONObject(0).getString("translatedText").toString());

                    Log.i("SD",translations.getJSONObject(0).getString("translatedText"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    trans.setText("Failed");
                }


            }
        });
        Thread.currentThread().join(5000);
    }

}
