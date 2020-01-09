package com.example.googlemaps2application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class OGPSActivity extends AppCompatActivity {

    EditText lonEditText,latEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogps);
        lonEditText=findViewById(R.id.lonEditText);
        latEditText=findViewById(R.id.latEditText);
    }



    public void setNback(View v) {
        double lat=Double.parseDouble(latEditText.getText().toString());
        double lon= Double.parseDouble(lonEditText.getText().toString());

        if (lon>=180 || lon<=-180 || lat>=90 || lat<=-90){
            if (lon>=180) lonEditText.setText("179");
            if (lon<=-180) lonEditText.setText("-179");
            if (lat>=90) latEditText.setText("89");
            if (lat<=-90) latEditText.setText("-89");
            return;
        }
        else{
            Intent intent = new Intent();
            intent.putExtra("lon", lon);
            intent.putExtra("lat", lat);
            setResult(RESULT_OK, intent);
            finish();
        }



    }

    final double Olat=53.838219;//48.87889993217496;
    final double Olng=27.475630;//2.367780036369033;

    public void set_default(View v) {
        lonEditText.setText(Double.toString(Olng));
        latEditText.setText(Double.toString(Olat));
    }
}
