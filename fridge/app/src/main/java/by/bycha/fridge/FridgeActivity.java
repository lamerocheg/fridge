package by.bycha.fridge;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FridgeActivity extends AppCompatActivity {
    private ArrayList<String> data;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FridgeListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //прячем клавиатуру
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.fridge_layout);
        initRecyclerViewAndCards();
        initAppBar();
    }
    private void initRecyclerViewAndCards(){
        data = new ArrayList<>(1000);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("ingridients.txt")));
            String tmp ;
            while ((tmp = reader.readLine()) != null) data.add(0 , tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RecyclerView rv = (RecyclerView)findViewById(R.id.scrollView);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new FridgeListAdapter(data);
        rv.setAdapter(adapter);
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rv,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {


                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    data.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                                saveDATA();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    data.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                saveDATA();
                                adapter.notifyDataSetChanged();
                            }
                        });

        rv.addOnItemTouchListener(swipeTouchListener);
    }


    private void initAppBar(){
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
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
        ArrayAdapter<String> autoadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, Constants.INGRIDIENTS_NAME);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(autoadapter);
    }




    public void onAddButtonClick(View v){
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        String text = textView.getText().toString();
        if (text.length() < 2) {
            Toast.makeText(this, "Ингридиент не может иметь такое короткое название =)", Toast.LENGTH_LONG).show();
            return;
        }
        Boolean flag = true;
        for (int i = 0; i < Constants.INGRIDIENTS_NAME.length; i++) {
            if (text.equals(Constants.INGRIDIENTS_NAME[i])) {
                flag = false;
                break;
            }
        }
        if (flag) {
            Toast.makeText(this, "Ингридиент не встречается в наших рецептах, попробуйте вести другой ингридиент", Toast.LENGTH_LONG).show();
            return;
        }
        data.add(0, text);
        adapter.notifyDataSetChanged();
        textView.setText("");
        saveDATA();
    }
    private void saveDATA(){
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("ingridients.txt", MODE_PRIVATE)));
            for (int i = 0 ; i < data.size() ; i++)
                bw.write(data.get(i)+"\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
