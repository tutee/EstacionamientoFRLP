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
import android.widget.ListAdapter;
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
 * Esta actividad permite al Guardia visualizar todas las compras del día.
 */

public class VerComprasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private Session session;
    private Spinner spi;
    private ListView li;
    private ProgressDialog Dialog;
    List<String> patecomp = new ArrayList<String>();
    List<String> accetipo = new ArrayList<String>();
    List<String> compid = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vercompras);
        spi = (Spinner)findViewById(R.id.spinner);
        li = (ListView) findViewById(R.id.list);
        Dialog = new ProgressDialog(this);
        Dialog.setCancelable(false);
        Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        session = new Session(VerComprasActivity.this);

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

        cargarlista(VarGlobales.cUid);

        li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (accetipo.get(position).equals("Comprado")){
                    ingreso (VarGlobales.cUid, compid.get(position));
                } else if (accetipo.get(position).equals("Ingreso")) {
                    egreso(VarGlobales.cUid, compid.get(position));
                } else if (accetipo.get(position).equals("Egreso")) {
                    ingreso (VarGlobales.cUid, compid.get(position));
                }
                //Toast.makeText(getApplicationContext(), "presiono " + compid.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ingreso (final String uId, final String cId) {
        // Tag used to cancel the request
        String tag_string_req = "req_ingreso";

        showDialog(); // CAMBIAR

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("RESPUESTA", response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getApplicationContext(),
                                "Cambio realizado", Toast.LENGTH_LONG).show();

                        cargarlista(VarGlobales.cUid);

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
                params.put("tag", "ingreso");
                params.put("guardia_id", uId);
                params.put("comp_id", cId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void egreso (final String uId, final String cId) {
        // Tag used to cancel the request
        String tag_string_req = "req_egreso";

        showDialog(); // CAMBIAR

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("RESPUESTA", response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getApplicationContext(),
                                "Cambio realizado", Toast.LENGTH_LONG).show();

                        cargarlista(VarGlobales.cUid);

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
                params.put("tag", "egreso");
                params.put("guardia_id", uId);
                params.put("comp_id", cId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void cargarlista (final String uId) {
        // Tag used to cancel the request
        String tag_string_req = "req_cargarlista";

        showDialog(); // CAMBIAR

        patecomp = new ArrayList<String>();
        accetipo = new ArrayList<String>();
        compid = new ArrayList<String>();

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
                            patecomp.add(jObjP.getString("comp_pate_cod"));
                            if(jObjP.getString("acce_tipo").equals("null")){
                                accetipo.add("Comprado");
                            } else {
                                accetipo.add(jObjP.getString("acce_tipo"));
                            }
                            compid.add(jObjP.getString("comp_id"));

                        }

                        Log.e("COMPRAS", String.valueOf(patecomp));


                        ListAdapterCompras listAdapterComp = new ListAdapterCompras(VerComprasActivity.this, compid, patecomp, accetipo);
                        li.setAdapter(listAdapterComp);
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
                params.put("tag", "listcomprasdia");
                params.put("pers_id", uId);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    class ListAdapterCompras extends BaseAdapter {

        Context context;
        List<String> pates = patecomp;
        List<String> actp = accetipo;
        List<String> cpid = compid;
        LayoutInflater inflater;

        public ListAdapterCompras(Context context,List<String> cpid, List<String> pates,  List<String> actp){
            this.context = context;
            this.cpid = cpid;
            this.pates = pates;
            this.actp = actp;
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
                itemView = inflater.inflate(R.layout.single_row_patecomp,parent,false);
            }

            TextView patentes = (TextView) itemView.findViewById(R.id.pate);
            patentes.setText("Patente: " + pates.get(position));

            TextView accesos = (TextView) itemView.findViewById(R.id.estado);
            accesos.setText("Estado: " + actp.get(position));

            return itemView;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_navg_1:
                Intent intent = new Intent(VerComprasActivity.this,
                        VerComprasActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navg_3:
                intent = new Intent(VerComprasActivity.this,
                        GestContraseniaActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navg_4:
                intent = new Intent(VerComprasActivity.this,
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
        Intent intent = new Intent(VerComprasActivity.this, LoginActivity.class);
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
