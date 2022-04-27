package com.example.prueba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.VolleyS;

public class alert extends AppCompatActivity {
    String acceso;
    private Button permitir;
    private VolleyS vs;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        permitir = findViewById(R.id.permitir);
        permitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();

            }
        });
    }
    private void check() {
        acceso = "1";
        DarAcceso(acceso);
    }


    public void DarAcceso(String acceso) {

        vs = VolleyS.getInstance(this.getApplicationContext());
        requestQueue = vs.getRequestQueue();

        String url = "http://192.168.100.27:8000/api/access/2";

        JSONObject data = new JSONObject();
        try {
            data.put("acceso", acceso);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.get("message").toString();
                            Toast.makeText(alert.this, "message: " + response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            SharedPreferences preferencias =  getSharedPreferences("sesion", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencias.edit();
                            editor.putString("message", message);
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(alert.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) ;

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }

    private void alertFail(String s) {
        new AlertDialog.Builder(this
        ).setTitle("Failed")
                .setIcon(R.drawable.ic_warning)
                .setMessage(s)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .show();
    }
}