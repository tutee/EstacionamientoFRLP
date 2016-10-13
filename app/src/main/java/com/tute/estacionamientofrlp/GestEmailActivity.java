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
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;

/**
 * Created by Tute on 26/9/2016.
 */

public class GestEmailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private Session session;
    private Button cambiarButton;
    private EditText emailn1, emailn2;
    private TextView tv1;

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestemail);

        session = new Session(GestEmailActivity.this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (!session.getLoggedIn()) {
            logoutUser();
        }
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText("Email actual: "+Constantes.email);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);

        cambiarButton = (Button) findViewById(R.id.confirmar_button);
        emailn1 = (EditText) findViewById(R.id.mailnuevoedit1);
        emailn2 = (EditText) findViewById(R.id.mailnuevoedit2);

        cambiarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = emailn1.getText().toString();
                String email2 = emailn2.getText().toString();

                if (email1.trim().length() > 0 && email2.trim().length() > 0) {
                    if (email1.equals(email2)) {
                        enviarEmail(email1);
                    } else {
                        Snackbar.make(v, "Los emails no coinciden", Snackbar.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Snackbar.make(v, "Complete los campos", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void enviarEmail(final String email1){

        Log.e("ERROR1", email1);
        Log.e("ERROR2", Constantes.cUid);

        String tag_string_req = "req_email";

        pDialog.setMessage("Modificando email...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("ERROR", String.valueOf(response));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Constantes.email = email1;
                        Intent intent = new Intent(GestEmailActivity.this,
                                GestEmailActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (error){
                        Toast.makeText(getApplicationContext(),
                                "El email ya se encuentra en uso", Toast.LENGTH_LONG).show();
                        Log.e("ERROR","No se pudo modificar el email");
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
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "cambiaremailusu");
                params.put("pers_id", Constantes.cUid);
                params.put("pers_email", email1);
                Log.e("ERROR", String.valueOf(params));

                return params;
            }

        };

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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_nav_1:
                Intent intent = new Intent(GestEmailActivity.this,
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
                intent = new Intent(GestEmailActivity.this,
                        DeshacerActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nav_3:
                intent = new Intent(GestEmailActivity.this,
                        HistorialesActivity.class);
                startActivity(intent);
                finish();
                break;

            /*case R.id.menu_nav_5:
                intent = new Intent(GestCuentaActivity.this,
                        GestPatesActivity.class);
                startActivity(intent);
                finish();
                break;
            */
            case R.id.menu_nav_6:
                intent = new Intent(GestEmailActivity.this,
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
        Intent intent = new Intent(GestEmailActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
