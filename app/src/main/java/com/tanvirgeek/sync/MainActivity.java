package com.tanvirgeek.sync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText editTextName;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    ArrayList<Contact> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        editTextName = findViewById(R.id.editTextName);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(adapter);
    }

    public void submitName(View view){
        String name = editTextName.getText().toString();
        saveToAppServer(name);
        editTextName.setText("");
    }

    private void readFromLocalStorage(){
        arrayList.clear();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = dbHelper.readfromLocalDatabase(database);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(DBContact.COL_NAME));
            int syncStatus = cursor.getInt(cursor.getColumnIndex(DBContact.COL_SYNC_STATUS));
            arrayList.add(new Contact(name,syncStatus));
        }

        adapter.notifyDataSetChanged();
        cursor.close();
        dbHelper.close();
    }

    private void saveToAppServer(final String name){

        if(checkNetworkConnection()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DBContact.SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String Response = jsonObject.getString("response");
                                if(Response.equals("OK")){
                                    saveToLocalStorage(name, DBContact.SYNC_STATUS_OK);
                                }else {
                                    saveToLocalStorage(name,DBContact.SYNC_STATUS_FAILED);
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    saveToLocalStorage(name, DBContact.SYNC_STATUS_FAILED);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    return params;
                }
            };

        }else {
            saveToLocalStorage(name,DBContact.SYNC_STATUS_FAILED);
        }

    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!= null && networkInfo.isConnected());
    }

    private void saveToLocalStorage(String name, int sync){
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        boolean isInserted = dbHelper.saveToLocalDatabase(name,DBContact.SYNC_STATUS_FAILED,database);
        if(isInserted){
            Toast.makeText(MainActivity.this,"data inserted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this,"data is not inserted", Toast.LENGTH_SHORT).show();
        }
        readFromLocalStorage();
        dbHelper.close();
    }
}
