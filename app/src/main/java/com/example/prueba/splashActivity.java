package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView =(ImageView) findViewById(R.id.imageView);
        lanzartheah();
    }
    private void lanzartheah() {
        //Aqui nos creamos el hilo
        Thread time = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{//Aqui pase lo que pase pasara a la siguiente pantallla
                    Intent intent = new Intent(splashActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        //Aqui lo ejecuto
        time.start();
    }
}