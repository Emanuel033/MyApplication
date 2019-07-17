package com.badillo.emanuel.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //boton ppara dirigirse al longlatactivity
        Button btnLongLat = (Button) findViewById(R.id.button2);
        btnLongLat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LongLatActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        //boton para dirigirse al direccionactivity
        Button btnDireccion = (Button) findViewById(R.id.button3);
        btnDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LocationApiActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        //boton ppara dirigirse al longlatactivity
        Button btnLocationApi = (Button) findViewById(R.id.button1);
        btnLocationApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DireccionActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        //boton para dirigirse al direccionactivity
        Button btnAutoUpdatePosition = (Button) findViewById(R.id.button);
        btnAutoUpdatePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AutoUpdatePositionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
