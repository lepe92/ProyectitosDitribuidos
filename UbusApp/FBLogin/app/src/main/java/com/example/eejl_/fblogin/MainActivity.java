package com.example.eejl_.fblogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.Profile;

public class MainActivity extends AppCompatActivity {
TextView yacuenta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        yacuenta= (TextView) findViewById(R.id.link_signup);

        yacuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(MainActivity.this, SignUp.class);
                startActivity(m);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Profile p= Profile.getCurrentProfile();
        if(p!=null){
            Log.d("bienvenido", p.getName());
            Intent ventanitaRuta=  new Intent(this, RutasMenu.class);
            startActivity(ventanitaRuta);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Profile p= Profile.getCurrentProfile();
        if(p!=null){
            Log.d("bienvenido", p.getName());
            Intent ventanitaRuta=  new Intent(this, RutasMenu.class);
            startActivity(ventanitaRuta);
        }
    }
}
