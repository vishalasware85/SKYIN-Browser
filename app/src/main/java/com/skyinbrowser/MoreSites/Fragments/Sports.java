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

public class Sports extends Fragment {

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
        view = inflater.inflate(R.layout.sports_fragment, container, false);
        mContext = view.getContext();

        ImageView yahoosports=view.findViewById(R.id.yahoosports);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1Jjl8gdOSYYZPzxL8DE594IqJs12WuB5t&export=download")
                .into(yahoosports, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        yahoosports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://sports.yahoo.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView cbssports=view.findViewById(R.id.cbssports);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1jmXYVmwyyLTug_5q-xIFY54kBMKXp4dq&export=download")
                .into(cbssports, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        cbssports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.cbssports.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView nbcsports=view.findViewById(R.id.nbcsports);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1RSrrm5tkMDqKdH8Rf2kqrc7MEt9Vf4Ua&export=download")
                .into(nbcsports, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        nbcsports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.nbcsports.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView espon=view.findViewById(R.id.espn);
        Picasso.get()
                .load("https://drive.google.com/uc?id=12U5YvTGEauLuM5awjkp2yBvRuMLZouXO&export=download")
                .into(espon, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        espon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.espn.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView skysports=view.findViewById(R.id.skysports);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1NxUnbPiBC77X5MFnT-E-ypvzjpe8KDwM&export=download")
                .into(skysports, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        skysports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.skysports.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView foxsports=view.findViewById(R.id.foxsports);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1QRyRY7QKe6o285qchozEoVOcL7dxmsKC&export=download")
                .into(foxsports, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        foxsports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.foxsports.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView bleacherreport=view.findViewById(R.id.bleacherreport);
        Picasso.get()
                .load("https://drive.google.com/uc?id=18M4eG4-XlVSjqO3Dc5_PxmprfUjIjzS1&export=download")
                .into(bleacherreport, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        bleacherreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.bleacherreport.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView sportsillustrated=view.findViewById(R.id.sportsillustrated);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1a8fvO286-ggaDALfPx1VwfnIK3m8RQVE&export=download")
                .into(sportsillustrated, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        sportsillustrated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.si.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView deadspin=view.findViewById(R.id.deadspin);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1ldZL7h5J-1DprNPDfR41fVajSsp8SK0Z&export=download")
                .into(deadspin, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
        deadspin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://deadspin.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView sportingnews=view.findViewById(R.id.sportingnews);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1Z4R_maQbWlhCb6qZsV6IqCyP5C5xf3V7&export=download")
                .into(sportingnews, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        sportingnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.sportingnews.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        return view;
    }
    private static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }
}
