package com.example.smartplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;

    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void goData(String timeData) {
       /* Intent ResultIntent = new Intent(mContext, MainActivity.class);
        ResultIntent.putExtra("routeTime", timeData);
        setResult(android.app.Activity.RESULT_OK, ResultIntent);
        Toast.makeText(mContext, "$timeData, YEEEAH", Toast.LENGTH_LONG).show();*/
    }
}
