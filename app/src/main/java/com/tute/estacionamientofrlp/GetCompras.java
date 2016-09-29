package com.tute.estacionamientofrlp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

                ArrayList pate = new ArrayList<String>();;

                try {

                    Log.e("ERROR", "1");
                    JSONObject obj = new JSONObject(response);
                    JSONArray arrayP = obj.getJSONArray("patente");
                    Log.e("ERROR", String.valueOf(arrayP));

                    for (int h = 0; h < arrayP.length(); h++ ){

                        JSONObject jObjP = arrayP.getJSONObject(h);
                        if(jObjP.getString("codigo").equals("null")) {
                            Log.e("IF", jObjP.getString("codigo"));
                        }
                        else {
                            pate.add(jObjP.getString("codigo"));
                            Log.e("ELSE", jObjP.getString("codigo"));
                        }
                    }
                    Log.e("ERROR", String.valueOf(pate));
                    Log.e("ERROR", String.valueOf(arrayP));
                    Log.e("OBJETOcheck", String.valueOf(pate));
                    Intent intent = new Intent(GetCompras.this,
                            CompraActivity.class);

                    Constantes.cSaldo = saldo;
                    Constantes.cUid = uid;
                    Constantes.cPosSpi = pateSelect;
                    Constantes.cCompSem = arrayP.toString();
                    Constantes.cCod = pate;

                    intent.putExtra("saldo", saldo);
                    intent.putExtra("uid", uid);
                    intent.putExtra("selectSpi", pateSelect);
                    intent.putExtra("semcomp", arrayP.toString());
                    intent.putExtra("codigo", pate);
                    startActivity(intent);
                    finish();


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
