package com.tr.nata.projectandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.nata.projectandroid.Adapter.kategoriAdapter;
import com.tr.nata.projectandroid.api.ApiClient;
import com.tr.nata.projectandroid.api.ApiService;
import com.tr.nata.projectandroid.model.DataKategoriItem;
import com.tr.nata.projectandroid.model.ResponseKategori;
import com.tr.nata.projectandroid.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    TextView tv_namaUser;

    private RecyclerView recyclerView;
    private kategoriAdapter adapter;

    private List<ResponseKategori> responseKategoris = new ArrayList<>();

    private List<DataKategoriItem> dataKategoriItems = new ArrayList<>();

    ApiService service;
    ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv_namaUser = findViewById(R.id.tv_nama);

        service=ApiClient.getApiService();

//        SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
//
//        String email = sharedPref.getString("email","");
//
//        Call<user> call = service.dataUser(email)
//                call.enqueue();

//        Bundle bundle = getIntent().getExtras();
//        tv_namaUser.setText(bundle.getString("namaUser"));
//        tv_namaUser.setText(getIntent().getStringExtra("namaUser"));
        SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
        String nama_user_login = sharedPref.getString("nama_user_login","");
        tv_namaUser.setText(nama_user_login);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_kategori);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Button btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean login = false;
                SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("status_login",login);
                editor.putString("status_login_string", String.valueOf(login));
                editor.apply();

                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        service.getKategori()
                .enqueue(new Callback<ResponseKategori>() {
                    @Override
                    public void onResponse(Call<ResponseKategori> call, Response<ResponseKategori> response) {
                        if (response.isSuccessful()){


                            dataKategoriItems=response.body().getDataKategori();
                            setAdapter();
//                            recyclerView.notifyAll();

//                            responseKategoris.addAll((Collection<? extends ResponseKategori>) response.body());

//                            adapter=new kategoriAdapter(responseKategoris,this);
//                            recyclerView.setAdapter(adapter);
//                            dataKategoriItems = response.body().getDataKategori();
//                            adapter = new kategoriAdapter(dataKategoriItems,this);
//                            recyclerView.setAdapter(adapter);
                        }else {
                            Toast.makeText(getApplicationContext(),"login gagal",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseKategori> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"login gagal koneksi",Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void setAdapter(){
        adapter = new kategoriAdapter(this,dataKategoriItems);
        recyclerView.setAdapter(adapter);
    }
}
