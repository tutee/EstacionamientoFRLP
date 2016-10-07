package com.tute.estacionamientofrlp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tute on 24/9/2016.
 */
public class DeshacerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private ArrayList<String> ite, pateSelComp, pateComp;
    String pateSelect;
    private Session session;
    private TextView tv1,tv2,tv3;
    private CheckBox c0,c1,c2,c3,c4,c5,c6;
    private Button  b0;
    private Spinner spi;
    double montoacomp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deshacer);
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

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(DeshacerActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
