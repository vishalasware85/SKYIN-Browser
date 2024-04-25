package com.skyinbrowser.MoreSites.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class Music extends Fragment {

    private Context mContext;
    private FragmentToActivity mCallback;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
        mContext = context;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.music_fragment, container, false);
        mContext = view.getContext();

        ImageView gaana=view.findViewById(R.id.gaana);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1W-wXzL-IVo_f_tXNhjOVaOuApZf04hRZ&export=download")
                .into(gaana, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        gaana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://gaana.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView soundcloud=view.findViewById(R.id.soundcloud);
        Picasso.get()
                .load("https://drive.google.com/uc?id=17xfxmji3DvJUlipFf7A-XpdhkzMY_6de&export=download")
                .into(soundcloud, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        soundcloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.soundcloud.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView spotify=view.findViewById(R.id.spotify);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1RRDR3HLE2stsD2xlUnlj7z0D5Lr-oOgU&export=download")
                .into(spotify, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.spotify.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView beatport=view.findViewById(R.id.beatport);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1tOxkWQHmIFG5h4C9JAGIVE_WzhP-0c22&export=download")
                .into(beatport, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        beatport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.beatport.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView mixcloud=view.findViewById(R.id.mixcloud);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1JRhXTykpthaJ4Wcvw21Jku-qd81c2Wpg&export=download")
                .into(mixcloud, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        mixcloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.mixcloud.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView pandora=view.findViewById(R.id.pandora);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1UxY-NC_Bbce7HHtADpaTKKV89PCHoMJE&export=download")
                .into(pandora, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        pandora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.pandora.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView noisetrade=view.findViewById(R.id.noisetrade);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1ekrOd6ZHh2q6mu8IxQ8NgiYJegKjAZ48&export=download")
                .into(noisetrade, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        noisetrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://noisetrade.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        return view;
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }
}
