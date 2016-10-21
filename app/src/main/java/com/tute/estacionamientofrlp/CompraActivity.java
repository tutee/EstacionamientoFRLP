package com.tute.estacionamientofrlp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Actividad para inflar las compras del usuario por patente para una misma semana.
 */
    public class CompraActivity extends AppCompatActivity implements Serializable, NavigationView.OnNavigationItemSelectedListener {
    private TextView tv1,tv2,tv3;
    private Session session;
    private CheckBox c0,c1,c2,c3,c4,c5,c6;
    private Button  b0;
    private Spinner spi;
    private ArrayList<String> ite, pateSelComp, pateComp;
    String pateSelect, uidg, saldog;
    private ProgressDialog pDialog;
    ActionBarDrawerToggle toggle;
    double montoacomp;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
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
        tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user_email = (TextView)hView.findViewById(R.id.email);
        nav_user_email.setText("Email: "+VarGlobales.email);
        TextView nav_user_nombre = (TextView)hView.findViewById(R.id.nom);
        nav_user_nombre.setText("Nombre: "+VarGlobales.apellido+" "+VarGlobales.nombre);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        ite = new ArrayList<String>();

        session = new Session(CompraActivity.this);

        if (!session.getLoggedIn()) {
            logoutUser();
        }

        final String saldo = (String) getIntent().getExtras().getSerializable("saldo");
        final String uid = (String) getIntent().getExtras().getSerializable("uid");
        ArrayList cod = (ArrayList) getIntent().getStringArrayListExtra("codigo");
        final String compSem = getIntent().getStringExtra("semcomp");
        final String posSpi = getIntent().getStringExtra("selectSpi");

        saldog = saldo;
        uidg = uid;
        /* Inflo el spinner con las patentes del usuario. */
        for (int h = 0; h < cod.size(); h++ ) {
            String pateCodigo = cod.get(h).toString();
            ite.add(pateCodigo);
        }
        /* Si el usuario no posee ninguna patente, el spinner muestra que no posee patentes.*/
        if (ite.size() == 0) {
            ite.add(getResources().getString(R.string.noPat));
        }

        tv1.setText("Semana: "+ VarGlobales.semana);
        tv2.setText("Saldo: $ "+ String.format("%.2f",Double.parseDouble(saldo)));


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ite);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi.setAdapter(adapter);

        if(posSpi!=null) {
            ArrayAdapter patesadapter = (ArrayAdapter) spi.getAdapter();
            int spinnerPosition = patesadapter.getPosition(posSpi);
            spi.setSelection(spinnerPosition);
        }
        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final JSONArray jArrComp;
                //JSONArray jArrInfo;

                try {
                        jArrComp = new JSONArray(compSem);
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
                                tv1.setText("Semana: "+ VarGlobales.semana);
                                tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));

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
        /* Tomo compras realizadas por el usuario y las ordeno. */
                                      if (pateComp != null) {
                                          if (!pateComp.isEmpty()) {
                                              Collections.sort(pateComp);
                                              /* Multipico la cantidad de compras por la constante de saldo por compra. */
                                              String costoCompra = String.valueOf(pateComp.size() * VarGlobales.precioticket);
                                              double cc = Double.parseDouble(costoCompra);
                                              double s = Double.parseDouble(saldo);
                                              /* Si el monto de las compras es menor al saldo del usuario, realiza la compra. */
                                              if (cc <= s) {
                                                  /* La actividad a cargar es 1 = CompraActivity luego del intent.*/
                                                  VarGlobales.actacargar = 1;
                                                  enviarCompra(pateSelect, uid, pateComp);
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
        /* Lógica de los checkboxs para generar los datos a enviar. */
        c0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c0.isChecked()&& c0.isEnabled()) {
                    if(!pateComp.contains(pateSelComp.get(0))) {
                        pateComp.add(pateSelComp.get(0));
                    }
                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(0))) {
                                pateComp.remove(pateSelComp.get(0));
                            }
                        }
                    }
                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}
                    c6.setChecked(false);
                }
            }
        });

        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c1.isChecked()&& c1.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(1))) {
                        pateComp.add(pateSelComp.get(1));
                    }

                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(1))) {
                                pateComp.remove(pateSelComp.get(1));
                            }
                        }
                    }

                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}
                    c6.setChecked(false);

                }
            }
        });

        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c2.isChecked()&& c2.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(2))) {
                        pateComp.add(pateSelComp.get(2));
                    }

                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(2))) {
                                pateComp.remove(pateSelComp.get(2));
                            }
                        }
                    }
                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}
                    c6.setChecked(false);
                }
            }
        });

        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c3.isChecked()&& c3.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(3))) {
                        pateComp.add(pateSelComp.get(3));
                    }

                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(3))) {
                                pateComp.remove(pateSelComp.get(3));
                            }
                        }
                    }

                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}
                    c6.setChecked(false);

                }
            }
        });

        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c4.isChecked()&& c4.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(4))) {
                        pateComp.add(pateSelComp.get(4));
                    }

                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}

                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(4))) {
                                pateComp.remove(pateSelComp.get(4));
                            }
                        }
                    }
                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}
                    c6.setChecked(false);
                }
            }
        });

        c5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c5.isChecked()&& c5.isEnabled()) {

                    if(!pateComp.contains(pateSelComp.get(5))) {
                        pateComp.add(pateSelComp.get(5));
                    }
                    montoacomp = (pateComp.size()* VarGlobales.precioticket);

                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}


                } else {
                    if (!pateSelComp.isEmpty()) {
                        for (int i = 0; i < pateComp.size(); i++) {
                            String dsc = pateComp.get(i);
                            if (dsc.equals(pateSelComp.get(5))) {
                                pateComp.remove(pateSelComp.get(5));
                            }
                        }
                    }
                    montoacomp = (pateComp.size()* VarGlobales.precioticket);
                    tv3.setText("Costo: $ "+ String.format("%.2f",montoacomp));
                    if (montoacomp > Double.parseDouble(saldog)) {
                        tv3.setTextColor(getResources().getColor(R.color.Red));
                    } else {tv3.setTextColor(getResources().getColor(R.color.Black));}
                    c6.setChecked(false);

                }
            }
        });
        /* Checkbox que checkea todos los hábiles a la vez. */
        c6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c6.isChecked()&& c6.isEnabled()) {
                    /* Checkea si el checkbox no está checkeado y está habilitado. */
                    if (!c0.isChecked()&& c0.isEnabled()){
                        /* Si no está el día en la lista de compras, se agrega. */
                        if(!pateComp.contains(pateSelComp.get(0))) {
                            pateComp.add(pateSelComp.get(0));
                            /* Se checkea el checkbox no checkeado. */
                            c0.setChecked(true);
                        }
                    }
                    if (!c1.isChecked()&& c1.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(1))) {
                            pateComp.add(pateSelComp.get(1));
                            c1.setChecked(true);
                        }
                    }
                    if (!c2.isChecked()&& c2.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(2))) {
                            pateComp.add(pateSelComp.get(2));
                            c2.setChecked(true);
                        }
                    }
                    if (!c3.isChecked()&& c3.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(3))) {
                            pateComp.add(pateSelComp.get(3));
                            c3.setChecked(true);
                        }
                    }
                    if (!c4.isChecked()&& c4.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(4))) {
                            pateComp.add(pateSelComp.get(4));
                            c4.setChecked(true);
                        }
                    }
                    if (!c5.isChecked()&& c5.isEnabled()){
                        if(!pateComp.contains(pateSelComp.get(5))) {
                            pateComp.add(pateSelComp.get(5));
                            c5.setChecked(true);
                        }
                    }

                }
            }
        });

        /* El SwipeRefresh permite hacer slide para recargar los datos.*/
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // El Refresh listener actua un disparador cuando se realiza la acción. */
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* Se llama a la acción refresh saldo. */
                refreshsaldo(VarGlobales.cUid);
                /* Cuando termina, se setea en falso para que corte. */
                swipeContainer.setRefreshing(false);
            }
        });
        /* Se setea el color del ícono que gira. */
        swipeContainer.setColorSchemeResources(R.color.OliveDrab);
    }

    /* Función que infla los checkboxs. */
    public void infloCB (String data){
        final JSONArray jArrData;
        try {
            jArrData = new JSONArray(data);
            for (int i = 0; i < jArrData.length(); i++ ) {

                JSONObject dia = jArrData.getJSONObject(i);
                String nmdia = dia.getString("cale_dia");
                String fechadia = dia.getString("cale_fecha");
                /* Guardo las fechas de los checkboxs dentro de un array. */
                pateSelComp.add(fechadia);
                int habil = dia.getInt("cale_dia_habil");
                int descomp = dia.getInt("deshabilitable");
                int psblcomp = dia.getInt("comprable");
                int comp = dia.getInt("comprado");
                /* Lógica de habilitación y deshabilitación de los checkboxs. */
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
                                            if (comp == 1) {
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
                                            if (comp == 1) {
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
                                            if (comp == 1) {
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
                                            if (comp == 1) {
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
                                            if (comp == 1) {
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
                                            if (comp == 1) {
                                                c5.setEnabled(false);
                                                c5.setChecked(true);
                                                c5.setTextColor(getResources().getColor(R.color.OliveDrab));
                                            }
                                            break;
                                    }
                            }
                            break;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Función que envía las compras al Backend. */
    private void enviarCompra(final String pate, final String uid, final ArrayList fechas ){

        String tag_string_req = "req_compras";

        pDialog.setMessage("Realizando compras ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.e("ERROR compras", response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String uSaldo = jObj.getString("saldo");
                    if (!error) {
                        Intent intent = new Intent(CompraActivity.this,
                                GetCompras.class);
                        intent.putExtra("saldo", uSaldo);
                        intent.putExtra("uid", uid);
                        intent.putExtra("posicion", pateSelect);
                        startActivity(intent);
                        finish();
                    } else if (error){
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /* Si ocurre un error, me encuentro en esta situación. */
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                /* Mapeo los datos que voy a enviar en el request. */
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "compra");
                params.put("pers_id", uid);
                params.put("pers_pate", pate);
                JSONArray jsonArray = new JSONArray(fechas);
                params.put("cod_fecha", jsonArray.toString());
                return params;
            }

        };
        /* Agrego la request a la cola de requests. */
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    /* Función que actualiza CompraActivity a pedido del usuario. */
    private void refreshsaldo(final String uid){
        String tag_string_req = "req_refresh_compra";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("ERROR refreshCompras", response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String saldoact = jObj.getString("saldo");
                        VarGlobales.cSaldo = saldoact;
                        /* Setéo la actividad a cargar 1 = CompraActivity. */
                        VarGlobales.actacargar = 1;
                        Intent intent = new Intent(CompraActivity.this,
                                GetCompras.class);
                        intent.putExtra("saldo", VarGlobales.cSaldo);
                        intent.putExtra("uid", VarGlobales.cUid);
                        startActivity(intent);
                        finish();

                    } else if (error){
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /* Si ocurre un error, me encuentro en esta situación. */
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                /* Mapeo los datos que voy a enviar en el request. */
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "refreshsaldo");
                params.put("pers_id", uid);
                return params;
            }

        };
        /* Agrego la request a la cola de requests. */
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

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
        Intent intent = new Intent(CompraActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    /* Función que infla el navigation menú de la izquierda. */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_navu_1:
                Intent intent = new Intent(CompraActivity.this,
                        CompraActivity.class);

                intent.putExtra("saldo", VarGlobales.cSaldo);
                intent.putExtra("uid", VarGlobales.cUid);
                intent.putExtra("selectSpi", VarGlobales.cPosSpi);
                intent.putExtra("semcomp", VarGlobales.cCompSem);
                intent.putExtra("codigo", VarGlobales.cCod);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_2:
                intent = new Intent(CompraActivity.this,
                        DeshacerActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_3:
                intent = new Intent(CompraActivity.this,
                        HistorialesActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_5:
                intent = new Intent(CompraActivity.this,
                        GestContraseniaActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_6:
                intent = new Intent(CompraActivity.this,
                        GestEmailActivity.class);
                startActivity(intent);
                finish();
                break;



        }

        return false;
    }




}
