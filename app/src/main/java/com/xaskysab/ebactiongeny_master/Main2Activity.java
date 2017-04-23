package com.xaskysab.ebactiongeny_master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        User$Action action = (User$Action) getIntent().getBundleExtra("bundle").get("bundle");
        User user = action.getAUser();

        if(action.getCode()==0){

            Toast.makeText(this,"from MainActivity--"+user.getName(),Toast.LENGTH_SHORT).show();

        }

    }
}
