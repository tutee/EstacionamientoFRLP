package com.tute.estacionamientofrlp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
 * Created by Tute on 1/9/2016.
 */
public class RegistrationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private TextView tvLogin;
    private EditText email, apellido, nombre, dni, password, rol;
    private Button registerButton;
    private Session session;
    private ProgressDialog pDialog;
    private Spinner spi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new Session(RegistrationActivity.this);

        if (!session.getLoggedIn()) {
            logoutUser();
        }

        registerButton = (Button) findViewById(R.id.register_button);
        email = (EditText) findViewById(R.id.email_register);
        dni = (EditText) findViewById(R.id.dni_register);
        apellido = (EditText) findViewById(R.id.apellido_register);
        nombre = (EditText) findViewById(R.id.nombre_register);
        password = (EditText) findViewById(R.id.password_register);
        spi = (Spinner) findViewById(R.id.spinner);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String ape = apellido.getText().toString();
                String nom = nombre.getText().toString();
                String doc = dni.getText().toString();
                String role = spi.getSelectedItem().toString();
                Log.e("ERROR",role);

                if (!mail.isEmpty() && !ape.isEmpty() && !nom.isEmpty() && !doc.isEmpty() && !role.isEmpty()) {
                    registerUser(mail, ape, nom, doc, role);
                    Log.e("ERROR","ERROR0");
                } else {
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi.setAdapter(adapter);
    }



    private void registerUser(final String email, final String apellido,
                              final String nombre, final String documento, final String rol) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("ERROR","ERROR1");
        pDialog.setMessage("Registering ...");
        showDialog();
        Log.e("ERROR","ERROR6");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.e("ERROR","ERROR7");
                try {
                    Log.e("ERROR","ERROR2");
                    JSONObject jObj = new JSONObject(response);
                    JSONObject jObjU = jObj.getJSONObject("user");
                    String pass = jObjU.getString("pass");
                    Log.e("PASS:", pass);
                    Log.e("ERROR","ERROR2.1");
                    boolean error = jObj.getBoolean("error");
                    Log.e("ERROR","ERROR2.2");
                    if (!error) {
                        Log.e("ERROR","ERROR11");
                        Intent intent = new Intent(
                                RegistrationActivity.this,
                                RegistrationActivity.class);
                        startActivity(intent);
                        finish();
                        Log.e("ERROR","ERROR3");
                    } else {
                        Log.e("ERROR","ERROR10");
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Log.e("ERROR","ERROR8");
                    }
                } catch (JSONException e) {
                    Log.e("ERROR","ERROR11");
                    e.printStackTrace();
                }
                Log.e("ERROR","ERROR9");
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                Log.e("ERROR","ERROR4");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("pers_email", email);
                params.put("pers_apellido", apellido);
                params.put("pers_nombre", nombre);
                params.put("pers_doc", documento);
                params.put("pers_rol", rol);
                Log.e("ERROR","ERROR5");
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
                Intent intent = new Intent(RegistrationActivity.this,
                        AddSaldoActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navi_2:
                intent = new Intent(RegistrationActivity.this,
                        RegistrationActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navi_3:
                intent = new Intent(RegistrationActivity.this,
                        AddPateActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navi_6:
                intent = new Intent(RegistrationActivity.this,
                        GestContraseniaActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_navi_7:
                intent = new Intent(RegistrationActivity.this,
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
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}