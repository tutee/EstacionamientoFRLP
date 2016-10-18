package com.tute.estacionamientofrlp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tute on 26/9/2016.
 */

public class HistorialesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private Session session;
    private Spinner spi;
    private ListView li;
    ArrayList compras = new ArrayList<String>();
    List<String> comppatecod = new ArrayList<String>();
    List<String> compfechas = new ArrayList<String>();
    List<String> diadecompra = new ArrayList<String>();
    List<String> costocomp = new ArrayList<String>();
    private ProgressDialog Dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historiales);
        spi = (Spinner)findViewById(R.id.spinner);
        li = (ListView) findViewById(R.id.list);
        Dialog = new ProgressDialog(this);
        Dialog.setCancelable(false);
        Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        session = new Session(HistorialesActivity.this);

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi.setAdapter(adapter);

        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (String.valueOf(spi.getItemAtPosition(position)).equals("Mis compras")){
                    cargarlista(VarGlobales.cUid);
                }
                else if (String.valueOf(spi.getItemAtPosition(position)).equals("Mis cargas")){
                    cargarlistasaldo(VarGlobales.cUid);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void cargarlistasaldo (final String uId) {
        // Tag used to cancel the request
        String tag_string_req = "req_cargarlista_cargas";

        showDialog(); // CAMBIAR

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("ERROR Saldo", response);
                Log.e("id", uId);


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONArray arrayC = jObj.getJSONArray("compras");
                        for (int h = 0; h < arrayC.length(); h++ ){


                            JSONObject jObjP = arrayC.getJSONObject(h);
                            compras.add(jObjP);
                            costocomp.add(jObjP.getString("carg_monto"));
                            diadecompra.add(jObjP.getString("carg_fc"));

                        }

                        ListAdapterCarg adapterlistcargas = new ListAdapterCarg(HistorialesActivity.this,diadecompra,costocomp);
                        li.setAdapter(adapterlistcargas);
                        hideDialog();

                    } else {

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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "listcargasusu");
                params.put("pers_id", uId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void cargarlista (final String uId) {
        // Tag used to cancel the request
        String tag_string_req = "req_cargarlista";

        showDialog(); // CAMBIAR

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("ERROR Saldo", response);
                Log.e("id", uId);


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONArray arrayC = jObj.getJSONArray("compras");
                        for (int h = 0; h < arrayC.length(); h++ ){


                            JSONObject jObjP = arrayC.getJSONObject(h);
                            compras.add(jObjP);
                            comppatecod.add(jObjP.getString("comp_pate_cod"));
                            compfechas.add(jObjP.getString("comp_fecha"));
                            diadecompra.add(jObjP.getString("comp_fc"));
                            costocomp.add(jObjP.getString("comp_monto"));

                        }

                        Log.e("COMPRAS", String.valueOf(compras));
                        Log.e("PATES", String.valueOf(comppatecod));
                        Log.e("FECHAS", String.valueOf(compfechas));

                        ListAdapter adapterlist = new ListAdapter(HistorialesActivity.this, comppatecod, compfechas,diadecompra,costocomp);
                        li.setAdapter(adapterlist);
                        hideDialog();

                    } else {

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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "listcomprasusu");
                params.put("pers_id", uId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    class ListAdapterCarg extends BaseAdapter {

        Context context;
        List<String> cuando = diadecompra;
        List<String> costo = costocomp;
        LayoutInflater inflater;

        public ListAdapterCarg(Context context, List<String> cuando, List<String> costo){
            this.context = context;
            this.cuando = cuando;
            this.costo = costo;
        }


        @Override
        public int getCount() {
            return costo.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if(itemView==null){
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = inflater.inflate(R.layout.single_row_cargas,parent,false);
            }

            TextView fc = (TextView) itemView.findViewById(R.id.fc);
            fc.setText("Fecha de carga: " + cuando.get(position));
            TextView cost = (TextView) itemView.findViewById(R.id.monto);
            cost.setText("Carga: $" + costo.get(position));

            return itemView;
        }
    }

    class ListAdapter extends BaseAdapter {

        Context context;
        List<String> pates = comppatecod;
        List<String> cf = compfechas;
        List<String> cuando = diadecompra;
        List<String> costo = costocomp;
        LayoutInflater inflater;

        public ListAdapter(Context context, List<String> pates, List<String> cf, List<String> cuando, List<String> costo){
            this.context = context;
            this.pates = pates;
            this.cf = cf;
            this.cuando = cuando;
            this.costo = costo;
        }


        @Override
        public int getCount() {
            return pates.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if(itemView==null){
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = inflater.inflate(R.layout.single_row_miscomp,parent,false);
            }

            TextView pate = (TextView) itemView.findViewById(R.id.pate);
            pate.setText("Patente: " + pates.get(position));
            TextView dia = (TextView) itemView.findViewById(R.id.dia);
            dia.setText("Dia comprado: "+cf.get(position));
            TextView fc = (TextView) itemView.findViewById(R.id.fc);
            fc.setText("Fecha de compra: " + cuando.get(position));
            TextView cost = (TextView) itemView.findViewById(R.id.costo);
            cost.setText("Costo: " + costo.get(position));

            return itemView;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_navu_1:
                Intent intent = new Intent(HistorialesActivity.this,
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
                intent = new Intent(HistorialesActivity.this,
                        DeshacerActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_3:
                intent = new Intent(HistorialesActivity.this,
                        HistorialesActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_5:
                intent = new Intent(HistorialesActivity.this,
                        GestContraseniaActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navu_6:
                intent = new Intent(HistorialesActivity.this,
                        GestEmailActivity.class);
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
        Intent intent = new Intent(HistorialesActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void hideDialog() {
        if (Dialog.isShowing())
            Dialog.dismiss();
    }

    private void showDialog() {
        if (!Dialog.isShowing())
            Dialog.show();
    }

}
