package com.handm.dhruval.personsinfo;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handm.dhruval.personsinfo.helper.PersonInfoDialogFragment;
import com.handm.dhruval.personsinfo.model.PersonInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyAdapter adapter;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String json = getJsonFileData();
        adapter = new MyAdapter(getPersonInfoFromJson(json));
        recyclerView.setAdapter(adapter);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(floatingActionButtonListener);
    }

    private String getJsonFileData() {
        InputStream is = getResources().openRawResource(R.raw.person_info);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error reading json data",  e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e("MainActivity", "Error reading json data", e);
            }
        }

        return writer.toString();
    }

    private List<PersonInfo> getPersonInfoFromJson(String jsonData) {
        List<PersonInfo> personInfoList = new ArrayList<>();
        try {

            JSONArray jArray = new JSONArray(jsonData);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);

                PersonInfo personInfo = new PersonInfo(jsonObject.getString(FIRST_NAME),
                        jsonObject.getString(LAST_NAME));

                personInfoList.add(personInfo);
            }
        } catch (JSONException e) {
            Log.e("MainActivity", "Error reading json data", e);
        }

        return personInfoList;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        List<PersonInfo> infoList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView fullName;

            public ViewHolder(View itemView) {
                super(itemView);
                fullName = (TextView)itemView.findViewById(R.id.full_name);
            }
        }

        public MyAdapter(List<PersonInfo> infoList) {
            this.infoList = infoList;
        }

        public void update(PersonInfo personInfo) {
            infoList.add(personInfo);
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            PersonInfo personInfo = infoList.get(position);
            holder.fullName.setText(personInfo.lastName + ", " + personInfo.firstName);
        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }
    }

    private View.OnClickListener floatingActionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PersonInfoDialogFragment dialogFragment = new PersonInfoDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "dialog");
        }
    };

    public void showEnteredInfo(PersonInfo personInfo) {
        adapter.update(personInfo);
        adapter.notifyDataSetChanged();
    }

}
