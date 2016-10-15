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
 * Created by Tute on 19/9/2016.
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

    public void checkFecha(final String uid, final String saldo){

        Log.e("ERROR", "2");


        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppURLs.URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                ArrayList pate = new ArrayList<String>();

                try {

                    Log.e("ERROR", "1");
                    JSONObject obj = new JSONObject(response);
                    JSONArray arrayP = obj.getJSONArray("patente");

                    for (int h = 0; h < arrayP.length(); h++ ){

                        JSONObject jObjP = arrayP.getJSONObject(h);

                        if(jObjP.getString("codigo").equals("null")) {
                            Log.e("IF", jObjP.getString("codigo"));
                        }
                        else {
                            pate.add(jObjP.getString("codigo"));
                            Log.e("ELSE", jObjP.getString("codigo"));
                        }

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

                    VarGlobales.pridiasem = Character.toString(VarGlobales.sem1.charAt(8))+Character.toString(VarGlobales.sem1.charAt(9))+"/"+Character.toString(VarGlobales.sem1.charAt(5))+Character.toString(VarGlobales.sem1.charAt(6));
                    VarGlobales.ultdiasem = Character.toString(VarGlobales.sem2.charAt(8))+Character.toString(VarGlobales.sem2.charAt(9))+"/"+Character.toString(VarGlobales.sem2.charAt(5))+Character.toString(VarGlobales.sem2.charAt(6));
                    VarGlobales.semana = VarGlobales.pridiasem +" - "+ VarGlobales.ultdiasem;

                    Log.e("PRIMER DIA", String.valueOf(VarGlobales.pridiasem));
                    Log.e("ULTI DIA", String.valueOf(VarGlobales.ultdiasem));
                    Log.e("ULTI DIA", String.valueOf(VarGlobales.semana));
                    Log.e("ERROR", String.valueOf(pate));
                    Log.e("ERROR", String.valueOf(arrayP));
                    Log.e("OBJETOcheck", String.valueOf(pate));

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
                //Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getdias");
                params.put("pers_id", uid);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }



}
