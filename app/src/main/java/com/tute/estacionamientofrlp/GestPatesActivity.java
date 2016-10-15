package com.tute.estacionamientofrlp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Created by Tute on 26/9/2016.
 */

public class GestPatesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    private ArrayList<String> ite;
    private Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestpates);

        ite = new ArrayList<String>();

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
