package com.tute.estacionamientofrlp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tute on 24/9/2016.
 */
public class DeshacerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private ArrayList<String> ite, pateSelComp, pateComp;
    String pateSelect;
    private Session session;
    private TextView tv1,tv2;
    private CheckBox c0,c1,c2,c3,c4,c5,c6;
    private Button  b0;
    private Spinner spi;
    double montoacomp;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deshacer);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        c0 = (CheckBox)findViewById(R.id.checkBox0);
        c1 = (CheckBox)findViewById(R.id.checkBox1);
        c2 = (CheckBox)findViewById(R.id.checkBox2);
        c3 = (CheckBox)findViewById(R.id.checkBox3);
        c4 = (CheckBox)findViewById(R.id.checkBox4);
        c5 = (CheckBox)findViewById(R.id.checkBox5);
        c6 = (CheckBox)findViewById(R.id.checkBox6);
        b0 = (Button)findViewById(R.id.comprardia_button);
        spi = (Spinner)findViewById(R.id.spinner);
        montoacomp=0;

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        ite = new ArrayList<String>();

        for (int h = 0; h < Constantes.cCod.size(); h++ ) {
            String pateCodigo = Constantes.cCod.get(h).toString();
            ite.add(pateCodigo);
        }

        if (ite.size() == 0) {
            ite.add(getResources().getString(R.string.noPat));
        }

        session = new Session(DeshacerActivity.this);

        if (!session.getLoggedIn()) {
            logoutUser();
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);

        tv1.setText("Semana: "+Constantes.semana);
        tv2.setText("Saldo: $ "+ String.format("%.2f",Double.parseDouble(Constantes.cSaldo)));

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ite);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi.setAdapter(adapter);

        if(Constantes.cPosSpi!=null) {
            ArrayAdapter patesadapter = (ArrayAdapter) spi.getAdapter();
            int spinnerPosition = patesadapter.getPosition(Constantes.cPosSpi);
            spi.setSelection(spinnerPosition);
        }

        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final JSONArray jArrComp;
                //JSONArray jArrInfo;

                try {
                    jArrComp = new JSONArray(Constantes.cCompSem);
                    for (int h = 0; h < jArrComp.length(); h++ ){
                        JSONObject codi = jArrComp.getJSONObject(h);
                        String pate = codi.getString("codigo");

                        if (String.valueOf(spi.getItemAtPosition(position)).equals(pate)) {
                            Log.e("IF SPI", "estoy en el if");
                            pateSelComp = new ArrayList<String>();
                            pateComp = new ArrayList<String>();
                            pateSelect = String.valueOf(spi.getItemAtPosition(position));
                            c0.setChecked(false);
                            c0.setEnabled(true);
                            c0.setTextColor(getResources().getColor(R.color.Black));
                            c1.setChecked(false);
                            c1.setEnabled(true);
                            c1.setTextColor(getResources().getColor(R.color.Black));
                            c2.setChecked(false);
                            c2.setEnabled(true);
                            c2.setTextColor(getResources().getColor(R.color.Black));
                            c3.setChecked(false);
                            c3.setEnabled(true);
                            c3.setTextColor(getResources().getColor(R.color.Black));
                            c4.setChecked(false);
                            c4.setEnabled(true);
                            c4.setTextColor(getResources().getColor(R.color.Black));
                            c5.setChecked(false);
                            c5.setEnabled(true);
                            c5.setTextColor(getResources().getColor(R.color.Black));
                            c6.setChecked(false);
                            c6.setEnabled(true);
                            c6.setTextColor(getResources().getColor(R.color.Black));

                            String AR = codi.getString("semana");
                            infloCB(AR);

                        }  else if (String.valueOf(spi.getItemAtPosition(position)).equals(getResources().getString(R.string.noPat))) {

                            Log.e("IF SPI", "estoy en el else del if ");

                            c0.setChecked(false);
                            c0.setEnabled(false);
                            c0.setTextColor(getResources().getColor(R.color.Gray));
                            c1.setChecked(false);
                            c1.setEnabled(false);
                            c1.setTextColor(getResources().getColor(R.color.Gray));
                            c2.setChecked(false);
                            c2.setEnabled(false);
                            c2.setTextColor(getResources().getColor(R.color.Gray));
                            c3.setChecked(false);
                            c3.setEnabled(false);
                            c3.setTextColor(getResources().getColor(R.color.Gray));
                            c4.setChecked(false);
                            c4.setEnabled(false);
                            c4.setTextColor(getResources().getColor(R.color.Gray));
                            c5.setChecked(false);
                            c5.setEnabled(false);
                            c5.setTextColor(getResources().getColor(R.color.Gray));
                            c6.setChecked(false);
                            c6.setEnabled(false);
                            c6.setTextColor(getResources().getColor(R.color.Gray));
                            tv1.setText("Semana: "+Constantes.semana);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        b0.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      //Log.e("BOTON", String.valueOf(pateComp));

                                      if (pateComp != null) {
                                          if (!pateComp.isEmpty()) {
                                              Collections.sort(pateComp);
                                              Log.e("ORDENADO", String.valueOf(pateComp));
                                              String costoCompra = String.valueOf(pateComp.size() * Constantes.precioticket); //Multipico por 3 debido a que es el costo de un ticket del estacionamiento
                                              Log.e("ORDENADO", costoCompra);
                                              double cc = Double.parseDouble(costoCompra);
                                              double s = Double.parseDouble(Constantes.cSaldo);
                                              if (cc <= s) {
                                                  Log.e("enviarCompra", pateSelect);
                                                  Log.e("enviarCompra", Constantes.cUid);
                                                  Log.e("enviarCompra", String.valueOf(pateComp));
                                                  enviarDeshacer(pateSelect, Constantes.cUid, pateComp);
                                              } else {
                                                  Snackbar.make(v, "Su saldo actual es insuficiente", Snackbar.LENGTH_LONG)
                                                          .show();
                                              }
                                          } else if (pateComp.isEmpty()) {
                                              Snackbar.make(v, "No selecciono ningún día", Snackbar.LENGTH_LONG)
                                                      .show();
                                          }
                                      } else {
                                          Snackbar.make(v, "No posee patentes", Snackbar.LENGTH_LONG).show();
                                      }
                                  }
                              }
        );

        c0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c0.isChecked()&& c0.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(0))) {
                        Log.e("ERROR3.2", String.valueOf(pateSelComp.get(0)));
                        pateComp.add(pateSelComp.get(0));
                        Log.e("SEMANA A COMPRAR!", String.valueOf(Arrays.asList(pateComp)));
                        Log.e("BOX MARTES", "ENTRE");
                    }

                    montoacomp = (pateComp.size()*Constantes.precioticket);

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(0))) {
                                pateComp.remove(pateSelComp.get(0));
                            }
                        }
                    }
                    Log.e("ERROR3.2", String.valueOf(pateComp));
                    montoacomp = (pateComp.size()*Constantes.precioticket);
                    c6.setChecked(false);
                }
            }
        });

        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c1.isChecked()&& c1.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(1))) {
                        Log.e("ERROR3.2", String.valueOf(pateSelComp.get(1)));
                        pateComp.add(pateSelComp.get(1));
                        Log.e("SEMANA A COMPRAR!", String.valueOf(Arrays.asList(pateComp)));
                        Log.e("BOX MARTES", "ENTRE");
                    }

                    montoacomp = (pateComp.size()*Constantes.precioticket);

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(1))) {
                                pateComp.remove(pateSelComp.get(1));
                            }
                        }
                    }

                    Log.e("ERROR3.2", String.valueOf(pateComp));
                    montoacomp = (pateComp.size()* Constantes.precioticket);
                    c6.setChecked(false);
                }
            }
        });

        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c2.isChecked()&& c2.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(2))) {
                        Log.e("ERROR3.2", String.valueOf(pateSelComp.get(2)));
                        pateComp.add(pateSelComp.get(2));
                        Log.e("SEMANA A COMPRAR!", String.valueOf(Arrays.asList(pateComp)));
                        Log.e("BOX MARTES", "ENTRE");
                    }

                    montoacomp = (pateComp.size()*Constantes.precioticket);

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(2))) {
                                pateComp.remove(pateSelComp.get(2));
                            }
                        }
                    }
                    Log.e("ERROR3.2", String.valueOf(pateComp));
                    montoacomp = (pateComp.size()*Constantes.precioticket);
                    c6.setChecked(false);
                }
            }
        });

        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c3.isChecked()&& c3.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(3))) {
                        Log.e("ERROR3.2", String.valueOf(pateSelComp.get(3)));
                        pateComp.add(pateSelComp.get(3));
                        Log.e("SEMANA A COMPRAR!", String.valueOf(Arrays.asList(pateComp)));
                        Log.e("BOX MARTES", "ENTRE");
                    }

                    montoacomp = (pateComp.size()*Constantes.precioticket);

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(3))) {
                                pateComp.remove(pateSelComp.get(3));
                            }
                        }
                    }

                    Log.e("ERROR3.2", String.valueOf(pateComp));
                    montoacomp = (pateComp.size()*Constantes.precioticket);
                    c6.setChecked(false);

                }
            }
        });

        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c4.isChecked()&& c4.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(4))) {
                        Log.e("ERROR3.2", String.valueOf(pateSelComp.get(4)));
                        pateComp.add(pateSelComp.get(4));
                        Log.e("SEMANA A COMPRAR!", String.valueOf(Arrays.asList(pateComp)));
                        Log.e("BOX MARTES", "ENTRE");
                    }

                    montoacomp = (pateComp.size()*Constantes.precioticket);

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(4))) {
                                pateComp.remove(pateSelComp.get(4));
                            }
                        }
                    }
                    Log.e("ERROR3.2", String.valueOf(pateComp));
                    montoacomp = (pateComp.size()*Constantes.precioticket);
                    c6.setChecked(false);
                }
            }
        });

        c5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c5.isChecked()&& c5.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(5))) {
                        Log.e("ERROR3.2", String.valueOf(pateSelComp.get(5)));
                        pateComp.add(pateSelComp.get(5));
                        Log.e("SEMANA A COMPRAR!", String.valueOf(Arrays.asList(pateComp)));
                        Log.e("BOX MARTES", "ENTRE");
                    }

                    montoacomp = (pateComp.size()*Constantes.precioticket);


                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(5))) {
                                pateComp.remove(pateSelComp.get(5));
                            }
                        }
                    }
                    Log.e("ERROR3.2", String.valueOf(pateComp));
                    montoacomp = (pateComp.size()*Constantes.precioticket);
                    c6.setChecked(false);

                }
            }
        });

        c6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c6.isChecked()&& c6.isEnabled()) {

                    for (int i = 0; i < pateComp.size(); i++) {
                        pateComp.remove(pateSelComp.get(i));
                    }

                    if (!c0.isChecked()&& c0.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(0))) {
                            Log.e("BOX 6", String.valueOf(Arrays.asList(pateComp)));
                            Log.e("BOX 6", "ENTRE AL 6");
                            pateComp.add(pateSelComp.get(0));
                            c0.setChecked(true);
                        }
                    }
                    if (!c1.isChecked()&& c1.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(1))) {
                            Log.e("BOX 6", String.valueOf(Arrays.asList(pateComp)));
                            Log.e("BOX 6", "ENTRE AL 6");
                            pateComp.add(pateSelComp.get(1));
                            c1.setChecked(true);
                        }
                    }
                    if (!c2.isChecked()&& c2.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(2))) {
                            Log.e("BOX 6", String.valueOf(Arrays.asList(pateComp)));
                            Log.e("BOX 6", "ENTRE AL 6");
                            pateComp.add(pateSelComp.get(2));
                            c2.setChecked(true);
                        }
                    }
                    if (!c3.isChecked()&& c3.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(3))) {
                            Log.e("BOX 6", String.valueOf(Arrays.asList(pateComp)));
                            Log.e("BOX 6", "ENTRE AL 6");
                            pateComp.add(pateSelComp.get(3));
                            c3.setChecked(true);
                        }
                    }
                    if (!c4.isChecked()&& c4.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(4))) {
                            Log.e("BOX 6", String.valueOf(Arrays.asList(pateComp)));
                            Log.e("BOX 6", "ENTRE AL 6");
                            pateComp.add(pateSelComp.get(4));
                            c4.setChecked(true);
                        }
                    }
                    if (!c5.isChecked()&& c5.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(5))) {
                            Log.e("BOX 6", String.valueOf(Arrays.asList(pateComp)));
                            Log.e("BOX 6", "ENTRE AL 6");
                            pateComp.add(pateSelComp.get(5));
                            c5.setChecked(true);
                        }
                    }

                }
            }
        });

    }

    public void infloCB (String data){

        final JSONArray jArrData;

        try {

            jArrData = new JSONArray(data);

            for (int i = 0; i < jArrData.length(); i++ ) {

                JSONObject dia = jArrData.getJSONObject(i);
                String nmdia = dia.getString("cale_dia");
                String fechadia = dia.getString("cale_fecha");
                pateSelComp.add(fechadia);  // Guardo las fechas de los checkbox dentro de un array (pateSelComp).

                int habil = dia.getInt("cale_dia_habil");
                int descomp = dia.getInt("deshabilitable");
                int psblcomp = dia.getInt("comprable");
                int comp = dia.getInt("comprado");
                int comprador = dia.getInt("comprador");

                if (nmdia != null) {

                    switch (i) {
                        case 0:

                            c0.setText(nmdia);
                            switch (habil) {
                                case 0:
                                    c0.setEnabled(false);   //no es habil --> checkbox deshabilitado
                                    c0.setTextColor(getResources().getColor(R.color.Gray));
                                    break;
                                case 1:
                                    switch (descomp) {
                                        case 0:
                                            if (comp == 0) {
                                                if (psblcomp == 0) {   //Estoy parado en dias anteriores al actual
                                                    c0.setEnabled(false);
                                                    c0.setTextColor(getResources().getColor(R.color.Gray));
                                                }
                                            } else {
                                                c0.setEnabled(false);
                                                c0.setChecked(true);
                                                c0.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                        case 1:
                                            if (comp == 1 && comprador == 1) {
                                                c0.setEnabled(false);
                                                c0.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            } else if (comp == 1 && comprador == 0) {
                                                c0.setEnabled(false);
                                                c0.setChecked(true);
                                                c0.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                    }
                            }
                            break;

                        case 1:  c1.setText(nmdia);
                            switch (habil) {
                                case 0:
                                    c1.setEnabled(false);   //no es habil --> checkbox deshabilitado
                                    c1.setTextColor(getResources().getColor(R.color.Gray));
                                    break;
                                case 1:
                                    switch (descomp) {
                                        case 0:
                                            if (comp == 0) {
                                                if (psblcomp == 0) {
                                                    c1.setEnabled(false);
                                                    c1.setTextColor(getResources().getColor(R.color.Gray));
                                                }
                                            } else {
                                                c1.setEnabled(false);
                                                c1.setChecked(true);
                                                c1.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                        case 1:
                                            if (comp == 1 && comprador == 1) {
                                                c1.setEnabled(false);
                                                c1.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            } else if (comp == 1 && comprador == 0) {
                                                c1.setEnabled(false);
                                                c1.setChecked(true);
                                                c1.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                    }
                            }
                            break;
                        case 2:  c2.setText(nmdia);
                            switch (habil) {
                                case 0:
                                    c2.setEnabled(false);   //no es habil --> checkbox deshabilitado
                                    c2.setTextColor(getResources().getColor(R.color.Gray));
                                    break;
                                case 1:
                                    switch (descomp) {
                                        case 0:
                                            if (comp == 0) {
                                                if (psblcomp == 0) {
                                                    c2.setEnabled(false);
                                                    c2.setTextColor(getResources().getColor(R.color.Gray));
                                                }
                                            } else {
                                                c2.setEnabled(false);
                                                c2.setChecked(true);
                                                c2.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                        case 1:
                                            if (comp == 1 && comprador == 1) {
                                                c2.setEnabled(false);
                                                c2.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            } else if (comp == 1 && comprador == 0) {
                                                c2.setEnabled(false);
                                                c2.setChecked(true);
                                                c2.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                    }
                            }
                            break;
                        case 3:  c3.setText(nmdia);
                            switch (habil) {
                                case 0:
                                    c3.setEnabled(false);   //no es habil --> checkbox deshabilitado
                                    c3.setTextColor(getResources().getColor(R.color.Gray));
                                    break;
                                case 1:
                                    switch (descomp) {
                                        case 0:
                                            if (comp == 0) {
                                                if (psblcomp == 0) {
                                                    c3.setEnabled(false);
                                                    c3.setTextColor(getResources().getColor(R.color.Gray));
                                                }
                                            } else {
                                                c3.setEnabled(false);
                                                c3.setChecked(true);
                                                c3.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                        case 1:
                                            if (comp == 1 && comprador == 1) {
                                                c3.setEnabled(false);
                                                c3.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            } else if (comp == 1 && comprador == 0) {
                                                c3.setEnabled(false);
                                                c3.setChecked(true);
                                                c3.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                    }
                            }
                            break;
                        case 4:  c4.setText(nmdia);
                            switch (habil) {
                                case 0:
                                    c4.setEnabled(false);   //no es habil --> checkbox deshabilitado
                                    c4.setTextColor(getResources().getColor(R.color.Gray));
                                    break;
                                case 1:
                                    switch (descomp) {
                                        case 0:
                                            if (comp == 0) {
                                                if (psblcomp == 0) {
                                                    c4.setEnabled(false);
                                                    c4.setTextColor(getResources().getColor(R.color.Gray));
                                                }
                                            } else {
                                                c4.setEnabled(false);
                                                c4.setChecked(true);
                                                c4.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                        case 1:
                                            if (comp == 1 && comprador == 1) {
                                                c4.setEnabled(false);
                                                c4.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            } else if (comp == 1 && comprador == 0) {
                                                c4.setEnabled(false);
                                                c4.setChecked(true);
                                                c4.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                    }
                            }
                            break;
                        case 5:  c5.setText(nmdia);
                            switch (habil) {
                                case 0:
                                    c5.setEnabled(false);   //no es habil --> checkbox deshabilitado
                                    c5.setTextColor(getResources().getColor(R.color.Gray));
                                    break;
                                case 1:
                                    switch (descomp) {
                                        case 0:
                                            if (comp == 0) {
                                                if (psblcomp == 0) {
                                                    c5.setEnabled(false);
                                                    c5.setTextColor(getResources().getColor(R.color.Gray));
                                                }
                                            } else {
                                                c5.setEnabled(false);
                                                c5.setChecked(true);
                                                c5.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                        case 1:
                                            if (comp == 1 && comprador == 1) {

                                                c5.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            } else if (comp == 1 && comprador == 0) {
                                                c5.setEnabled(false);
                                                c5.setChecked(true);
                                                c5.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                    }
                            }
                            break;

                        default: Log.e("ERROR","ESTOY EN EL DEFAUUULT");
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void enviarDeshacer(final String pate, final String uid, final ArrayList fechas ){

        Log.e("ERROR", pate);
        Log.e("ERROR", uid);
        Log.e("ERROR", String.valueOf(fechas));

        String tag_string_req = "req_login";

        pDialog.setMessage("Deshaciendo compras ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("ERROR1", String.valueOf(response));
                    boolean error = jObj.getBoolean("error");
                    Log.e("ERROR2", String.valueOf(error));
                    String uSaldo = jObj.getString("saldo");
                    Log.e("ERROR3", uSaldo);
                    if (!error) {
                        Intent intent = new Intent(DeshacerActivity.this,
                                GetCompras.class);
                        intent.putExtra("saldo", uSaldo);
                        intent.putExtra("uid", uid);
                        intent.putExtra("posicion", pateSelect);
                        startActivity(intent);
                        finish();
                    } else if (error){
                        Log.e("ERROR","Su compra no fue realizada");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "deshacer");
                params.put("pers_id", uid);
                params.put("pers_pate", pate);
                JSONArray jsonArray = new JSONArray(fechas);
                params.put("cod_fecha", jsonArray.toString());

                Log.e("ERROR123", String.valueOf(params));

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_nav_1:
                Intent intent = new Intent(DeshacerActivity.this,
                        CompraActivity.class);
                intent.putExtra("saldo", Constantes.cSaldo);
                intent.putExtra("uid", Constantes.cUid);
                intent.putExtra("selectSpi", Constantes.cPosSpi);
                intent.putExtra("semcomp", Constantes.cCompSem);
                intent.putExtra("codigo", Constantes.cCod);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nav_2:
                intent = new Intent(DeshacerActivity.this,
                        DeshacerActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nav_3:
                intent = new Intent(DeshacerActivity.this,
                        HistorialesActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nav_5:
                intent = new Intent(DeshacerActivity.this,
                        GestPatesActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nav_6:
                intent = new Intent(DeshacerActivity.this,
                        GestCuentaActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (dl.isDrawerOpen(GravityCompat.START))
            dl.closeDrawer(GravityCompat.START);


        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (id) {
            case R.id.action_settings:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }


    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(DeshacerActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
