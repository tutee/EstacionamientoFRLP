package com.tute.estacionamientofrlp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
 * Created by Tute on 1/9/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText email, password;

    private ProgressDialog progressDialog;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);*/
        session = new Session(LoginActivity.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        //registrationButton = (Button) findViewById(R.id.registration_button);
        loginButton = (Button) findViewById(R.id.signin_button);
        email = (EditText) findViewById(R.id.email_to_login);
        password = (EditText) findViewById(R.id.password_to_login);


        /**
         * utilizado para el boton register
         *
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
        */

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = email.getText().toString();
                String pass = password.getText().toString();

                if (correo.trim().length() > 0 && pass.trim().length() > 0) {
                    checkLogin(correo, pass);
                } else {
                    Snackbar.make(v, "Falta DNI o Contraseña", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void checkLogin(final String correo, final String password) {
        String tag_string_req = "req_login";

        progressDialog.setMessage("Iniciando sesión");
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

                    if (!error){
                    String userEmail = jObj.getString("email");
                    JSONObject jObjU = jObj.getJSONObject("user");
                    Constantes.cUid = jObjU.getString("id");
                    String userRole = jObjU.getString("rol");
                    String userSaldo = jObjU.getString("saldo");
                    Constantes.apellido = jObjU.getString("apellido");
                    Constantes.nombre = jObjU.getString("nombre");
                    Constantes.email = userEmail;
                    Log.e("ERROR", userRole);
                    Log.e("ERROR", Constantes.cUid);
                    Log.e("ERROR", userSaldo);

                        if (userEmail != null) {
                            session.setLogin(true);
                            Log.e("ERROR", "2");
                            //Dependiendo del rol de usuario loggeado, obtenemos un distinto activity

                            switch (userRole) {
                                case "Usuario":
                                    Constantes.actacargar = 1;
                                    Intent intent = new Intent(LoginActivity.this,
                                            GetCompras.class);
                                    intent.putExtra("saldo", userSaldo);
                                    intent.putExtra("uid", Constantes.cUid);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "Encargado":
                                    intent = new Intent(LoginActivity.this,
                                            RegistrationActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "Guardia":
                                    intent = new Intent(LoginActivity.this,
                                            RegistrationActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                            }
                        }

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
                        "Error de conexion", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("pers_email", correo);
                params.put("pers_password", password);

                return params;
            }

        };

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}