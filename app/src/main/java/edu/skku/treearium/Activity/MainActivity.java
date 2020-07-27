package edu.skku.treearium.Activity;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import edu.skku.treearium.Activity.AR.ArActivity;
import edu.skku.treearium.Activity.login.LoginActivity;
import edu.skku.treearium.R;
import edu.skku.treearium.helpers.LocationHelper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bnv;
    NavController  nc;
    FloatingActionButton btn;
    DrawerLayout drawerLayout;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigationViewListener();
        //네비게이션 바
        bnv = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        nc = Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupWithNavController(bnv,nc);

        //AR 버튼
        btn = findViewById(R.id.arbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAr = new Intent(MainActivity.this, ArActivity.class);
                try {
                    startActivity(intentAr);
                } catch (ActivityNotFoundException e) {
                    System.out.println("error");
                }
            }
        });

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.Logout: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this , LoginActivity.class));
                break;
            }
            case R.id.hello: {
                Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class App extends Application {

        private static Context mContext;

        @Override
        public void onCreate() {
            super.onCreate();
            mContext = this;
        }

        public static Context getContext(){
            return mContext;
        }
    }

    public void onResume() {
        super.onResume();
        {
            if (!LocationHelper.hasLocationPermission(this)) {
                LocationHelper.requestLocationPermission(this);
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!LocationHelper.hasLocationPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show();
            if (!LocationHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                LocationHelper.launchPermissionSettings(this);
            }
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            finish();
        }
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationlayout);
        navigationView.setNavigationItemSelectedListener(this);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.top_menu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) { // top menu navigation
//
//        switch (item.getItemId()) {
//            case R.id.Logout:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this , LoginActivity.class));
//                return true;
//
//            case R.id.hello:
//                Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//    }




}

