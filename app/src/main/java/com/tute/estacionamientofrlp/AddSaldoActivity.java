package com.tute.estacionamientofrlp;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tute on 8/10/2016.
 */

public class AddSaldoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private Session session;
    EditText e1,e2;
    Button b1;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsaldo);
        e1 = (EditText) findViewById(R.id.mailacargaredit);
        e2 = (EditText) findViewById(R.id.saldoacargaredit);
        b1 = (Button) findViewById(R.id.agregarsaldo_button);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String saldo = e2.getText().toString();
                Log.e("ERROR", email);
                Log.e("ERROR", saldo);
                Log.e("ERROR", Constantes.cUid);
                cargarsaldo(email, saldo);
            }
        });

        session = new Session(AddSaldoActivity.this);

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


    }


    private void cargarsaldo (final String email, final String saldo) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Cargando saldo ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.e("ERROR", String.valueOf(error));

                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "Saldo cargado con exito", Toast.LENGTH_LONG).show();
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "cargarsaldo");
                params.put("pers_id", Constantes.cUid);
                params.put("pers_email", email);
                params.put("pers_monto", saldo);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_navi_1:
                Intent intent = new Intent(AddSaldoActivity.this,
                        AddSaldoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_navi_2:
                intent = new Intent(AddSaldoActivity.this,
                        RegistrationActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_navi_3:
                intent = new Intent(AddSaldoActivity.this,
                        AddPateActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navi_6:
                intent = new Intent(AddSaldoActivity.this,
                        GestContraseniaActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navi_7:
                intent = new Intent(AddSaldoActivity.this,
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
        Intent intent = new Intent(AddSaldoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
