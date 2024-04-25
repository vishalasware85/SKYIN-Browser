package com.skyinbrowser.MoreSites.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;
import com.squareup.picasso.Picasso;

public class Games extends Fragment {

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
        view = inflater.inflate(R.layout.online_games_fragment, container, false);
        mContext = view.getContext();

        ImageView atmegames=view.findViewById(R.id.atmegames);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1knQ8wacVoL4wnG5lipxhRy0v1BoppJQI&export=download")
                .into(atmegames);
        atmegames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.atmegame.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView addictinggames=view.findViewById(R.id.addictinggames);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1zSANBf6TBYZ3fXv3gzXt8cVBOpa9UP0P&export=download")
                .into(addictinggames);
        addictinggames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.addictinggames.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView myrealgames=view.findViewById(R.id.myrealgames);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1ZXbl7QLcGhllpK_ybr0vXeagfKwj6_ki&export=download")
                .into(myrealgames);
        myrealgames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.myrealgames.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView goboplay=view.findViewById(R.id.goboplay);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1EMHa37YaubY59MW8vXuLwIvMKZsDAOPx&export=download")
                .into(goboplay);
        goboplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.goboplay.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView minecraft=view.findViewById(R.id.minecraft);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1mArQ4AbAVa8G46NkBVVFFAqrOJPdB-d1&export=download")
                .into(minecraft);
        minecraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.crazygames.com/t/minecraft");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView y8games=view.findViewById(R.id.y8games);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1_mXlijU7wDfvgP-oAYAw9bi2XrbWKpli&export=download")
                .into(y8games);
        y8games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://hi.y8.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView silvergames=view.findViewById(R.id.silvergames);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1eJ4AZT3amV9qAHcNSdRdMRwl09jtb4ZX&export=download")
                .into(silvergames);
        silvergames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.silvergames.com");
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
