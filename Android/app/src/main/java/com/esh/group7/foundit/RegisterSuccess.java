package com.esh.group7.foundit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Adrian on 2017-09-09.
 */

public class RegisterSuccess extends AppCompatActivity {

    Button continueButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registersuccess);

        continueButton = (Button) findViewById(R.id.buttonRead);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ReceiveInfo.class);
                startActivity(i);
            }
        });

    }
}
