package com.example.kkobak;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.kkobak.repository.request.LoginRequest;
import com.example.kkobak.repository.response.TokenResponse;
import com.example.kkobak.repository.util.RetrofitClient;
import com.example.kkobak.room.dao.AccessTokenDao;
import com.example.kkobak.room.data.AccessToken;
import com.example.kkobak.room.db.AppDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private EditText et_email;
    private EditText et_pw;
    private ImageButton btn_loginchk;
//    private AccessTokenDao tokenDao;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_pw = findViewById(R.id.et_pw);
        btn_loginchk = findViewById(R.id.btn_loginchk);

        //로딩
        dialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_DARK);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("로그인 중...");
//        dialog.show();
//        dialog.dismiss();

        // Room 관련 코드
//        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
//
//        tokenDao = database.tokenDao();

        // btn listener
        btn_loginchk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                System.out.println("로그인 버튼 클릭");
                getToken(et_email.getText().toString(),et_pw.getText().toString());
            }
        });
    }

    public void getToken(String email, String pw){
        System.out.println(email);
        System.out.println(pw);
        //Retrofit 호출
        LoginRequest loginRequest = new LoginRequest(et_email.getText().toString(),et_pw.getText().toString());
        Call<TokenResponse> call = RetrofitClient.getApiService().login(loginRequest);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적 : ", "error code : " + response.code());
                    return;
                }
                else{
                    TokenResponse token = response.body();
                    Log.d("연결이 성공적 : ", response.body().toString());

//                    List<AccessToken> tokenList = tokenDao.getTokenAll();
//                    if(tokenList.size()!=0){
//                        for(int i=tokenList.size();i>0;i--){
//                            AccessToken at = tokenList.get(i-1);
//                            tokenDao.setDeleteToken(at.getId());
//                        }
//                    }
//
//                    AccessToken accessToken = new AccessToken(); // 객체 인스턴스 생성
//                    accessToken.setAccessToken(token.getAccessToken());
//                    tokenDao.setInsertToken(accessToken);

                    // 전역변수로 액세스 토큰 저장
                    ((KkobakApp)getApplication()).setAccessToken(token.getAccessToken());
                    System.out.println("전역 토큰의 내용: "+((KkobakApp)getApplication()).getAccessToken());
                    dialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
    }
}