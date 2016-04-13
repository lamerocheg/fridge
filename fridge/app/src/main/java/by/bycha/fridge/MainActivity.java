package by.bycha.fridge;

import android.media.Image;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private JSONObject jsonSettings = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initNavigationView();
        checkFile();
    }


    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar , R.string.toggle_open , R.string.toggle_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                return false;
            }
        });

    }
    private void checkFile(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("settings.txt")));
            String tmp , result = "" ;
            while ((tmp = reader.readLine()) != null) result += tmp ;
            jsonSettings = new JSONObject(result);
            ImageView image = (ImageView) findViewById(R.id.image_fridge);
            if (jsonSettings.getInt("length_of_recipe_list") > 0 && jsonSettings.getInt("length_of_recipe_list") < 35) {
                image.setImageResource(R.drawable.empty_fridge);
            }
            else if (jsonSettings.getInt("length_of_recipe_list") >= 35){
                image.setImageResource(R.drawable.empty_fridge);
            }
        } catch (IOException | JSONException e) {
                
        }
    }
}
