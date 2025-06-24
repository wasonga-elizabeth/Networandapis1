package com.example.networkandapis;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnFetch;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Post> postList;
    private static final String URL = "https://jsonplaceholder.typicode.com/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataFromApi();
            }
        });
    }

    private void initViews() {
        btnFetch = findViewById(R.id.btnFetch);
        recyclerView = findViewById(R.id.recyclerView);
        postList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(postList);
        recyclerView.setAdapter(adapter);
    }

    private void fetchDataFromApi() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API Response", response);
                        parseJsonAndUpdateList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                });

        queue.add(request);
    }

    private void parseJsonAndUpdateList(String jsonResponse) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            postList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Post post = new Post(
                        obj.getInt("userId"),
                        obj.getInt("id"),
                        obj.getString("title"),
                        obj.getString("body")
                );
                postList.add(post);
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
