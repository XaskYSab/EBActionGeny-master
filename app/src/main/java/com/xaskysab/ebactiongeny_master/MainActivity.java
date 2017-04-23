package com.xaskysab.ebactiongeny_master;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xaskysab.gan.ParamActionGeny;

import java.util.List;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        say(new User("myuser"));
    }

    public void say(@ParamActionGeny User aUser){


        Intent main2 = new Intent(this,Main2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bundle",new User$Action(aUser,null,0));
        main2.putExtra("bundle",bundle);

        startActivityForResult(main2,0);



    }
}
