package com.chathura.cardscaner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.microblink.blinkcard.MicroblinkSDK;
import com.microblink.blinkcard.entities.recognizers.Recognizer;
import com.microblink.blinkcard.entities.recognizers.RecognizerBundle;
import com.microblink.blinkcard.entities.recognizers.blinkcard.BlinkCardRecognizer;
import com.microblink.blinkcard.uisettings.ActivityRunner;
import com.microblink.blinkcard.uisettings.BlinkCardUISettings;


class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MicroblinkSDK.setLicenseFile("src/main/assets/MB_com.chathura.cardscaner_BlinkCard_Android_2021-01-13.mblic", this);
    }
}

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 0 ;
    private BlinkCardRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;

    @Override
    protected void onCreate(Bundle bundle) {
        MicroblinkSDK.setLicenseFile("MB_com.chathura.cardscaner_BlinkCard_Android_2021-01-13.mblic", this);
        super.onCreate(bundle);

        // setup views, as you would normally do in onCreate callback

        // create BlinkCardRecognizer
        mRecognizer = new BlinkCardRecognizer();

        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = new RecognizerBundle(mRecognizer);

        startScanning();
    }

    public void startScanning() {
        // Settings for BlinkCardActivity
        BlinkCardUISettings settings = new BlinkCardUISettings(mRecognizerBundle);

        // tweak settings as you wish

        // Start activity
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, settings);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // load the data into all recognizers bundled within your RecognizerBundle
                mRecognizerBundle.loadFromIntent(data);

                // now every recognizer object that was bundled within RecognizerBundle
                // has been updated with results obtained during scanning session

                // you can get the result by invoking getResult on recognizer
                BlinkCardRecognizer.Result result = mRecognizer.getResult();
                if (result.getResultState() == Recognizer.Result.State.Valid) {
                    // result is valid, you can use it however you wish

                    Context context = getApplicationContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, result.getCardNumber(), duration);
                    toast.show();
                }
            }
        }
    }

}

