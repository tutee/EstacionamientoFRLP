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
 * Created by Tute on 7/10/2016.
 */

public class GestPatesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private Session session;
    private ProgressDialog pDialog;
    EditText e1, e2;
    Button b0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestpates);

        session = new Session(GestPatesActivity.this);

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

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        e1 = (EditText) findViewById(R.id.usuarioedit);
        e2 = (EditText) findViewById(R.id.pateborraredit);
        b0 = (Button)findViewById(R.id.borrarpate_button);

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String pate = e2.getText().toString();
                if (email.trim().length() > 0 && pate.trim().length() > 0){
                    enviarPB(email,pate);
                } else {
                    Snackbar.make(v, "Complete todos los campos", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void enviarPB(final String email, final String pate){

        Log.e("ERROR", pate);
        Log.e("ERROR", email);

        String tag_string_req = "req_pate_del";

        pDialog.setMessage("Borrando una patente");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.e("ERROR", String.valueOf(response));
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.e("ERROR", String.valueOf(error));

                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "Patente eliminada con exito", Toast.LENGTH_LONG).show();
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
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "eliminarpate");
                params.put("pers_email", email);
                params.put("pate_codigo", pate);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_nave_1:
                Intent intent = new Intent(GestPatesActivity.this,
                        AddSaldoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_nave_2:
                intent = new Intent(GestPatesActivity.this,
                        RegistrationActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_nave_3:
                intent = new Intent(GestPatesActivity.this,
                        AddPateActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nave_4:
                intent = new Intent(GestPatesActivity.this,
                        GestPatesActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nave_5:
                intent = new Intent(GestPatesActivity.this,
                        RegenContraseniaActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nave_7:
                intent = new Intent(GestPatesActivity.this,
                        GestContraseniaActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nave_8:
                intent = new Intent(GestPatesActivity.this,
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

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
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
        Intent intent = new Intent(GestPatesActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
