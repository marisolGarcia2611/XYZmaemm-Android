package com.example.prueba;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import models.VolleyS;

public class MainActivity extends AppCompatActivity {

    public static final String TOKEN = "token";
    private EditText etEmail;
    private EditText etPass;
    String email, password;
    private Button btnIniciar;
    private VolleyS vs;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.email);
        etPass = findViewById(R.id.pass);
        btnIniciar = findViewById(R.id.next_button);



        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();

            }
        });
    }
    private void checkLogin() {
        email = etEmail.getText().toString();
        password = etPass.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            alertFail("Email and Password is requerid");
        }
        else{
            iniciarSesion(email,password);
        }
    }


    public void iniciarSesion(String mail, String pass) {

        vs = VolleyS.getInstance(this.getApplicationContext());
        requestQueue = vs.getRequestQueue();

        String url = "http://192.168.100.27:8000/api/login2";

        JSONObject data = new JSONObject();
        try {
            data.put("email", email);
            data.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String token = response.get("token").toString();
                            //Toast.makeText(MainActivity.this, "token: " + response.get("token").toString(), Toast.LENGTH_SHORT).show();
                            //SharedPreferences preferencias =  getSharedPreferences("sesion", Context.MODE_PRIVATE);
                            //SharedPreferences.Editor editor = preferencias.edit();
                            //editor.putString("token", token);
                            //editor.commit();

                            //String token1 = response.get("token").toString();
                            Intent intent = new Intent(MainActivity.this, alert.class);
                            intent.putExtra(TOKEN,token);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();

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