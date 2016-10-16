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
 * Actividad para modificar la contraseña del usuario.
 */

public class GestContraseniaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private Session session;
    private Button confirmarButton;
    private TextView pv, pn1, pn2;

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*  Levanta el layout correspondiente según el rol.
            Cada layout maneja un navigation menu distinto. */
        switch (VarGlobales.cRole){
            case "Usuario":
                setContentView(R.layout.activity_gestcontrasenia_usu);
                break;
            case "Encargado":
                setContentView(R.layout.activity_gestcontrasenia_enc);
                break;
            case "Guardia":
                setContentView(R.layout.activity_gestcontrasenia_gua);
                break;
        }


        session = new Session(GestContraseniaActivity.this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
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

        confirmarButton = (Button) findViewById(R.id.confirmar_button);
        pv = (EditText) findViewById(R.id.contraviejaedit);
        pn1 = (EditText) findViewById(R.id.contranuevaedit);
        pn2 = (EditText) findViewById(R.id.confircontraedit);

        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contra0 = pv.getText().toString();
                String contra1 = pn1.getText().toString();
                String contra2 = pn2.getText().toString();
                /* Checkea que los campos no son vacíos. */
                if (contra0.trim().length() > 0 && contra1.trim().length() > 0 && contra2.trim().length() > 0) {
                    /*  Pisa el resultado de VarGlobales.cerror.
                        Si la password ingresada machea con la de la base de datos, continua. */
                    checkearPass(contra0);
                    if (VarGlobales.cerror) {
                        /* Checkea que ninguna de las passwords nuevas coincida con la anterior. */
                        if (contra0.equals(contra1) || contra0.equals(contra2)) {
                            Toast.makeText(getApplicationContext(),
                                    "La contraseña nueva debe ser distinta", Toast.LENGTH_LONG).show();
                        } else {
                            /* Si es correcto, actúa la función enviarPass. */
                            if (contra1.equals(contra2)) {
                                enviarPass(contra0, contra1);
                            } else {
                                Snackbar.make(v, "Las contraseñas no coinciden", Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }
                } else {
                    Snackbar.make(v, "Complete todos los campos", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    /* Función que comprueba que la contraseña anterior coincida con la contraseña válida del usuario. */
    private void checkearPass(final String pv){

        String tag_string_req = "chk_pass";

        pDialog.setMessage("Comprobando datos...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("ERROR Check_Pass", response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        VarGlobales.cerror = true;
                    } else if (error){
                        VarGlobales.cerror = false;
                        Toast.makeText(getApplicationContext(),
                                "Contraseña actual erronea", Toast.LENGTH_LONG).show();
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                /* Mapeo los datos que voy a enviar en el request. */
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "checkpass");
                params.put("pers_id", VarGlobales.cUid);
                params.put("pers_email", VarGlobales.email);
                params.put("pass_vieja", pv);
                return params;
            }

        };
        /* Agrego la request a la cola de requests. */
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void enviarPass(final String pv, final String pn1){

        String tag_string_req = "req_pass";

        pDialog.setMessage("Modificando contraseña...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.e("ERROR envPass", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "Contraseña modificada correctamente", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(GestContraseniaActivity.this,
                                GestContraseniaActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (error){
                        Toast.makeText(getApplicationContext(),
                                "No se pudo modificar la contraseña", Toast.LENGTH_LONG).show();
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                /* Mapeo los datos que voy a enviar en el request. */
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "cambiarpassusu");
                params.put("pers_id", VarGlobales.cUid);
                params.put("pers_email", VarGlobales.email);
                params.put("pass_vieja", pv);
                params.put("pass_nueva", pn1);
                Log.e("ERROR_Params", String.valueOf(params));
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

    /*  Función que infla el navigation menú de la izquierda.
        Dependiendo del rol, infla un menú distinto. */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (VarGlobales.cRole){
            case "Usuario":
                switch (item.getItemId()) {
                    case R.id.menu_navu_1:
                        Intent intent = new Intent(GestContraseniaActivity.this,
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
                        intent = new Intent(GestContraseniaActivity.this,
                                DeshacerActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_navu_3:
                        intent = new Intent(GestContraseniaActivity.this,
                                HistorialesActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_navu_5:
                        intent = new Intent(GestContraseniaActivity.this,
                                GestContraseniaActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_navu_6:
                        intent = new Intent(GestContraseniaActivity.this,
                                GestEmailActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                }
                break;

            case "Encargado":
                switch (item.getItemId()) {
                    case R.id.menu_nave_1:
                        Intent intent = new Intent(GestContraseniaActivity.this,
                                AddSaldoActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_nave_2:
                        intent = new Intent(GestContraseniaActivity.this,
                                RegistrationActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_nave_3:
                        intent = new Intent(GestContraseniaActivity.this,
                                AddPateActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_nave_4:
                        intent = new Intent(GestContraseniaActivity.this,
                                GestPatesActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_nave_5:
                        intent = new Intent(GestContraseniaActivity.this,
                                RegenContraseniaActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_nave_7:
                        intent = new Intent(GestContraseniaActivity.this,
                                GestContraseniaActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_nave_8:
                        intent = new Intent(GestContraseniaActivity.this,
                                GestEmailActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                break;

            case "Guardia":
                switch (item.getItemId()) {
                    case R.id.menu_navg_1:
                        Intent intent = new Intent(GestContraseniaActivity.this,
                                VerComprasActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_navg_3:
                        intent = new Intent(GestContraseniaActivity.this,
                                GestContraseniaActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menu_navg_4:
                        intent = new Intent(GestContraseniaActivity.this,
                                GestEmailActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                }
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
        Intent intent = new Intent(GestContraseniaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
