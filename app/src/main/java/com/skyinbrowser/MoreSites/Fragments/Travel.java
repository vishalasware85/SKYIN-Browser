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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;
import com.squareup.picasso.Picasso;

public class Travel extends Fragment {

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
        view = inflater.inflate(R.layout.travel_fragment, container, false);
        mContext = view.getContext();

        ImageView makemytrip=view.findViewById(R.id.makemytrip);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1rDGAS5EbrmL3yTDdmsbW-cvRrDggOoy7&export=download")
                .into(makemytrip);
        makemytrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.makemytrip.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView goibibo=view.findViewById(R.id.goibibo);
        Picasso.get()
                .load("https://drive.google.com/uc?id=16M6_sFcE_4FvSIyxs4pE_zvX9EAUleyu&export=download")
                .into(goibibo);
        goibibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.goibibo.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView booking=view.findViewById(R.id.booking);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1pcHE_Q70IxhA4iiDgx7lKsgO4PeLmSOd&export=download")
                .into(booking);
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.booking.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView irctc=view.findViewById(R.id.irctc);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1ooy4d7QlD_35Bty6NXoWDGDTXsXpg01c&export=download")
                .into(irctc);
        irctc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.irctc.co.in");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView yatra=view.findViewById(R.id.yatra);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1zsAyu1scyq94XkIlvP7BnJ4HlOwvJ2dR&export=download")
                .into(yatra);
        yatra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.yatra.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView airbnb=view.findViewById(R.id.airbnb);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1SGognelcGppuirpdnWPHq7tWdSG7ISTI&export=download")
                .into(airbnb);
        airbnb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.airbnb.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView homeaway=view.findViewById(R.id.homeaway);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1AS7gPkd_BRnyLAKl033rgYB6dmubZiqj&export=download")
                .into(homeaway);
        homeaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.homeaway.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView travelocity=view.findViewById(R.id.travelocity);
        Picasso.get()
                .load("https://drive.google.com/uc?id=14YmxrROF-HhSgLCJdspQ6TZSAnldQfFi&export=download")
                .into(travelocity);
        travelocity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.travelocity.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView lonelyplanet=view.findViewById(R.id.lonelyplanet);
        Picasso.get()
                .load("https://drive.google.com/uc?id=118GRDkzM_WMALmKow9r4RI8aL339QM85&export=download")
                .into(lonelyplanet);
        lonelyplanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.lonelyplanet.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView viator=view.findViewById(R.id.viator);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1Fzgi41wUy0PTAz0t2VF79wopnR6PkT2A&export=download")
                .into(viator);
        viator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.viator.com");
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
