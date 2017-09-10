package com.esh.group7.foundit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Adrian on 2017-09-10.
 */

public class Welcome extends AppCompatActivity {

    Button registerUser;
    Button scanTag;
    Button writeTag;

    public static DataHandler dh = new DataHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        registerUser = (Button) findViewById(R.id.buttonRegisterPerson);
        scanTag = (Button) findViewById(R.id.button3Read);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });

        scanTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ReceiveInfo.class);
                startActivity(i);
            }
        });

    }
}