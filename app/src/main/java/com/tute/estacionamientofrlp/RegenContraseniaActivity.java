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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tute on 8/10/2016.
 */

public class RegenContraseniaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private Session session;
    EditText e1;
    Button b1;
    private TextView tv1;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regenerarpass);
        tv1 = (TextView) findViewById(R.id.tv1);
        e1 = (EditText) findViewById(R.id.mailacargaredit);
        b1 = (Button) findViewById(R.id.regenerarpass_button);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                Log.e("ERROR", email);
                Log.e("ERROR", VarGlobales.cUid);
                if (email.trim().length() > 0){
                    regenerarPass(email);
                } else {
                    Snackbar.make(v, "Complete todos los campos", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

        session = new Session(RegenContraseniaActivity.this);

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


    private void regenerarPass (final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_regen_pass";

        pDialog.setMessage("Regenerando contraseña ...");
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
                        String pn = jObj.getString("pass");
                        Toast.makeText(getApplicationContext(),
                                "Contraseña regenerada con exito", Toast.LENGTH_LONG).show();
                        tv1.setText(pn);
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        /*Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();*/
                        Toast.makeText(getApplicationContext(),
                                "Error al regenerar la contraseña", Toast.LENGTH_LONG).show();
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
                params.put("tag", "regenerarpass");
                params.put("pers_email", email);
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

            case R.id.menu_nave_1:
                Intent intent = new Intent(RegenContraseniaActivity.this,
                        AddSaldoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_nave_2:
                intent = new Intent(RegenContraseniaActivity.this,
                        RegistrationActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_nave_3:
                intent = new Intent(RegenContraseniaActivity.this,
                        AddPateActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nave_4:
                intent = new Intent(RegenContraseniaActivity.this,
                        GestPatesActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nave_5:
                intent = new Intent(RegenContraseniaActivity.this,
                        RegenContraseniaActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nave_7:
                intent = new Intent(RegenContraseniaActivity.this,
                        GestContraseniaActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_nave_8:
                intent = new Intent(RegenContraseniaActivity.this,
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
        Intent intent = new Intent(RegenContraseniaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
