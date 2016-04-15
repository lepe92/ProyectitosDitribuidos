package com.example.edwin.ubicarpersona;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Array;

public class Opciones extends AppCompatActivity {
String opcion, ubicacion;
    int resID;
    Dialog builder;
    ImageView imageView;
    // String mDrawableName = "bus142";
    //int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lsm=(ListView)findViewById(R.id.listView2);



        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            opcion = null;
        } else {
            String temp[]=extras.getString("opciones").split("!");
            opcion=temp[1]+",Mostrar mapa";
            Log.i("mensaje", opcion);
            ubicacion=temp[0];
            Log.i("mensaje", "ubicacion "+temp[0]);
            String mDrawableName = ubicacion;
            resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
        }

        String [] opciones=opcion.split(",");

        final ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,opciones);
        lsm.setAdapter(itemsAdapter);


        lsm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Estamos trabajando en ello", Toast.LENGTH_LONG).show();
                if (position ==itemsAdapter.getCount()-1) {
                    builder = new Dialog(Opciones.this);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.WHITE));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            //nothing;
                        }
                    });
                    imageView = new ImageView(Opciones.this);
                    imageView.setImageResource(resID);
                    //imageView.setImageResource(R.drawable.ic_launcher);
                    Log.i("mensaje","mostrando imagen");

                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
                }
            }
        });
    }

}
