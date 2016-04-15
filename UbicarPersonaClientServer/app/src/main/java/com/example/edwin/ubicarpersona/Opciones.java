package com.example.edwin.ubicarpersona;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;

public class Opciones extends AppCompatActivity {
String opcion, ubicacion;
    // String mDrawableName = "bus142";
    //int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lsm=(ListView)findViewById(R.id.listView2);

        lsm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Estamos trabajando en ello",Toast.LENGTH_LONG).show();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            opcion = null;
        } else {
            String temp[]=extras.getString("opciones").split("!");
            opcion=temp[1];
            Log.i("mensaje", opcion);
            ubicacion=temp[0];
            Log.i("mensaje", "ubicacion "+temp[0]);
        }

        String [] opciones=opcion.split(",");

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,opciones);
        lsm.setAdapter(itemsAdapter);
    }

}
