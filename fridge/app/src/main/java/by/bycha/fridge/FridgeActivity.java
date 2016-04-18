package by.bycha.fridge;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.Inflater;

public class FridgeActivity extends AppCompatActivity {
    private LinearLayout.LayoutParams layoutParams;
    private JSONObject jsonIngridients;
    public LinearLayout cardLayout;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_layout);
        initCardsWork();
        initAppBar();
        addCompleteCards();
    }
    private void initCardsWork(){
        cardLayout = (LinearLayout) findViewById(R.id.layout_for_cardview);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(16, 16, 16, 16);


    }

    private void initAppBar(){
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        appBarLayout.setExpanded(false, false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            FloatingActionButton buttonAdd = (FloatingActionButton) findViewById(R.id.fridge_search_button);

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Ваш холодильник");
                    buttonAdd.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    buttonAdd.setVisibility(View.INVISIBLE);
                    isShow = false;
                }
            }
        });
    }
    private void addCompleteCards(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("ingridients.txt")));
            String tmp , result = "" ;
            while ((tmp = reader.readLine()) != null) result += tmp ;
            jsonIngridients = new JSONObject(result);
            for(int i = 0 ; i < jsonIngridients.getJSONArray("ingridients").length() ; i++)
            {
                addCard(jsonIngridients.getJSONArray("ingridients").getString(i));

            }
        } catch (IOException | JSONException e) {
            try {
                jsonIngridients = new JSONObject("{ingridients : []}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
    public void onAddButtonClick(View v){

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        try {
            jsonIngridients.getJSONArray("ingridients").put(textView.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addCard(textView.getText().toString());
        textView.setText("");
        saveJSON();
    }
    private void saveJSON(){
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("ingridients.txt", MODE_PRIVATE)));
            // пишем данные
            bw.write(jsonIngridients.toString());
            // закрываем поток
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addCard(String ingridientName){
        CardView card = new CardView(this);
        TextView text = new TextView(this);
        card.setLayoutParams(layoutParams);
        card.setMinimumHeight(80);
        card.setForegroundGravity(0x10);
        text.setTextSize(20);
        String tmp = "    " + ingridientName;
        text.setText(tmp);
        card.addView(text);
        cardLayout.addView(card, 0);
    }
}
