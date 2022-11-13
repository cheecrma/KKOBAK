package com.example.kkobak.ui.challenge;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kkobak.R;
import com.example.kkobak.data.retrofit.api.ChallengeChkApi;
import com.example.kkobak.data.room.dao.AccessTokenDao;
import com.example.kkobak.data.room.database.AccessTokenDatabase;
import com.example.kkobak.data.room.entity.AccessToken;
import com.example.kkobak.ui.test.TestActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PillActivity extends AppCompatActivity {
    AccessTokenDatabase db;
    String accessToken;

    String chlId;
    int cnt, goal;

    TextView tv_cnt;
    TextView tv_goal;
    ImageView iv_pill;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill);

        Intent intent = getIntent();
        if (intent.getStringExtra("chlId") != null)
            chlId = intent.getStringExtra("chlId");
        else
            chlId = "-1";
        cnt = goal = -1;
        if (intent.getIntExtra("cnt", 0) != 0)   cnt = intent.getIntExtra("cnt", 0);
        if (intent.getIntExtra("goal", 0) != 0)  goal = intent.getIntExtra("goal", 0);
        if (cnt < 0)    cnt = 0;
        if (goal < 0)   goal = 0;
        if (cnt > goal) cnt = goal;

        tv_cnt = findViewById(R.id.pillCnt);
        tv_goal = findViewById(R.id.pillGoal);
        iv_pill = findViewById(R.id.pillImg);

        tv_cnt.setText("현재 횟수: " + cnt);
        tv_goal.setText("목표 횟수: " + goal);

        changeImgBaseStatus();

        db = AccessTokenDatabase.getAppDatabase(this);
        try {
            accessToken = new TestActivity.getAccessTokenAsyncTask(db.accessTokenDao()).execute().get().getAccessToken();
        } catch (Exception e) {
            Toast.makeText(this, "에러 발생", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "cnt: " + cnt , Toast.LENGTH_SHORT).show();

        Call<Boolean> call = ChallengeChkApi.getService().setCnt(accessToken, Integer.parseInt(chlId), cnt);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() != 200)
                    Toast.makeText(PillActivity.this, "이상함", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PillActivity.this, "성공", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }


    public void changeImgBaseStatus() {
        if (cnt == -1 || goal == -1 || goal == 0)    return;
        int quot = (int)((double)cnt / goal * 100);

//        Toast.makeText(this, "" + quot, Toast.LENGTH_SHORT).show();
        if (quot >= 100)
            Glide.with(this).load(R.drawable.pillfinish).into(iv_pill);
        else if (quot >= 75)
            Glide.with(this).load(R.drawable.pill75).into(iv_pill);
        else if (quot >= 50)
            Glide.with(this).load(R.drawable.pill50).into(iv_pill);
        else if (quot >= 25)
            Glide.with(this).load(R.drawable.pill25).into(iv_pill);
        else
            Glide.with(this).load(R.drawable.pill0).into(iv_pill);
    }

    public void pillPlus(View v){
        if (cnt >= goal)    return;
        ++cnt;
        if (cnt == goal) {
            Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
        }

        tv_cnt.setText("현재 횟수: " + cnt);
        changeImgBaseStatus();
    }

    public void pillMinus(View v) {
        if (cnt <= 0)   return;
        --cnt;
        tv_cnt.setText("현재 횟수: " + cnt);
        changeImgBaseStatus();
    }

    public static class getAccessTokenAsyncTask extends AsyncTask<Void, Void, AccessToken> {
        private final AccessTokenDao accessTokenDao;

        public getAccessTokenAsyncTask(AccessTokenDao accessTokenDao) {
            this.accessTokenDao = accessTokenDao;
        }

        @Override
        protected AccessToken doInBackground(Void... voids) {
            List<AccessToken> tokens = accessTokenDao.getAll();
            if (tokens == null || tokens.size() == 0)
                return (null);
            else
                return (tokens.get(0));
        }
    }
}
