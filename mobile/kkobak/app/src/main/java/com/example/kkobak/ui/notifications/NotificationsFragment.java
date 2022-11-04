package com.example.kkobak.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.kkobak.R;
import com.example.kkobak.data.retrofit.api.MyChallengeApi;
import com.example.kkobak.data.retrofit.model.MyChallengeRes;
import com.example.kkobak.data.room.database.AccessTokenDatabase;
import com.example.kkobak.databinding.FragmentNotificationsBinding;
import com.example.kkobak.ui.main.MainActivity;
import com.example.kkobak.ui.test.TestActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    AccessTokenDatabase db;
    String accessToken;
    Call<List<MyChallengeRes>> callChallenge;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


//        View iv = binding.tmpImg;
//        String imgURL = "https://w7.pngwing.com/pngs/441/722/png-transparent-pikachu-thumbnail.png";
//        Glide.with(this).load(imgURL).into((ImageView) iv);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AccessTokenDatabase.getAppDatabase(getActivity());
        try {
            accessToken = new TestActivity.getAccessTokenAsyncTask(db.accessTokenDao()).execute().get().getAccessToken();
        } catch (Exception e) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        GridView gridView = (GridView) getView().findViewById(R.id.myChallengeGV);
        MyChallengeAdapter adapter = new MyChallengeAdapter();

        callChallenge = MyChallengeApi.getMyChallengeService().getMyChallenge(accessToken);
        callChallenge.enqueue(new Callback<List<MyChallengeRes>>() {
            @Override
            public void onResponse(Call<List<MyChallengeRes>> call, Response<List<MyChallengeRes>> response) {
                String str = "";
                List<MyChallengeRes> result = response.body();

                for (int i = 0; i < result.size(); i++){
                    MyChallengeRes item = response.body().get(i);
                    adapter.addItem(new MyChallengeCard(item.getImgUrl(), item.getStartTime(), item.getEndTime(), item.getTitle(), item.getId(), item.isWatch()));
                }

//                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        final MyChallengeCard item = (MyChallengeCard) adapter.getItem(i);
//                        Toast.makeText(getContext(), "ID: " + item.getId(), Toast.LENGTH_SHORT);
//                    }
//                });

                gridView.setAdapter(adapter);


                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<MyChallengeRes>> call, Throwable t) {
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}