package com.tute.estacionamientofrlp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
 * Esta actividad se encarga de consultar la base de datos para retornar
 * un Json con los datos requeridos tanto por CompraActivity como por DeshacerActivity.
 */
public class GetCompras extends Activity {

    String pateSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String saldo = (String) getIntent().getExtras().getSerializable("saldo");
        String uid = (String) getIntent().getExtras().getSerializable("uid");
        pateSelect = (String) getIntent().getExtras().getSerializable("posicion");

        checkFecha(uid,saldo);
    }

    /* Función que trae todos los datos del usuario de la base de datos. */
    public void checkFecha(final String uid, final String saldo){

        String tag_string_req = "req_compras";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.e("ERROR Compras", response);
                ArrayList pate = new ArrayList<String>();

                try {

                    JSONObject obj = new JSONObject(response);
                    JSONArray arrayP = obj.getJSONArray("patente");
                    /* Por cada patente del usuario tengo un array de compras y días asociados */
                    for (int h = 0; h < arrayP.length(); h++ ){

                        JSONObject jObjP = arrayP.getJSONObject(h);

                        if(jObjP.getString("codigo").equals("null")) {
                            Log.e("IF", jObjP.getString("codigo"));
                        }
                        else {
                            /* Si el código no es NULL lo agrego al ArrayList*/
                            pate.add(jObjP.getString("codigo"));
                        }
                        /* Tomo del Json los días de la semana */
                        if (h == 0) {
                            JSONArray arrayS = jObjP.getJSONArray("semana");
                            for (int k = 0; k < arrayS.length(); k++ ){
                                JSONObject jObjS = arrayS.getJSONObject(k);
                                if(k == 0){
                                    VarGlobales.sem1 = jObjS.getString("cale_fecha");
                                } else if (k == 5) {
                                    VarGlobales.sem2 = jObjS.getString("cale_fecha");
                                }
                            }

                        }
                    }
                    /* Armo un string con la semana vigente, tomando el primer día y el último día. */
                    VarGlobales.pridiasem = Character.toString(VarGlobales.sem1.charAt(8))+Character.toString(VarGlobales.sem1.charAt(9))+"/"+Character.toString(VarGlobales.sem1.charAt(5))+Character.toString(VarGlobales.sem1.charAt(6));
                    VarGlobales.ultdiasem = Character.toString(VarGlobales.sem2.charAt(8))+Character.toString(VarGlobales.sem2.charAt(9))+"/"+Character.toString(VarGlobales.sem2.charAt(5))+Character.toString(VarGlobales.sem2.charAt(6));
                    VarGlobales.semana = VarGlobales.pridiasem +" - "+ VarGlobales.ultdiasem;

                    /* Decido si tengo que ir a CompraActivity o DeshacerActivity. */
                    switch (VarGlobales.actacargar){
                        case 1: Intent intent = new Intent(GetCompras.this,
                                CompraActivity.class);

                            VarGlobales.cSaldo = saldo;
                            VarGlobales.cUid = uid;
                            VarGlobales.cPosSpi = pateSelect;
                            VarGlobales.cCompSem = arrayP.toString();
                            VarGlobales.cCod = pate;

                            intent.putExtra("saldo", saldo);
                            intent.putExtra("uid", uid);
                            intent.putExtra("selectSpi", pateSelect);
                            intent.putExtra("semcomp", arrayP.toString());
                            intent.putExtra("codigo", pate);
                            startActivity(intent);
                            finish();
                            break;

                        case 2:
                            intent = new Intent(GetCompras.this,
                                    DeshacerActivity.class);

                            VarGlobales.cSaldo = saldo;
                            VarGlobales.cUid = uid;
                            VarGlobales.cPosSpi = pateSelect;
                            VarGlobales.cCompSem = arrayP.toString();
                            VarGlobales.cCod = pate;

                            intent.putExtra("saldo", saldo);
                            intent.putExtra("uid", uid);
                            intent.putExtra("selectSpi", pateSelect);
                            intent.putExtra("semcomp", arrayP.toString());
                            intent.putExtra("codigo", pate);
                            startActivity(intent);
                            finish();
                            break;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /* Si ocurre un error, me encuentro en esta situación. */
                //Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            /* Mapeo los datos que voy a enviar en el request. */
            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getdias");
                params.put("pers_id", uid);

                return params;
            }

        };
        /* Agrego la request a la cola de requests. */
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }



}
