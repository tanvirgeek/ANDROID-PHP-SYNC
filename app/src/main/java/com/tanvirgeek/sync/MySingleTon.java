package com.tanvirgeek.sync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleTon {
    private static MySingleTon mInstance;
    private RequestQueue requestQueue;
    private static Context mctx;

    private MySingleTon(Context context){
        mctx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());

        return requestQueue;
    }

    public static synchronized MySingleTon getInstance(Context context){
        if (mInstance == null){
            mInstance = new MySingleTon(context);

        }
        return mInstance;
    }

    public<T> void addToRquestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
