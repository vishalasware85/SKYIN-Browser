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

public class Shopping extends Fragment {

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
        view = inflater.inflate(R.layout.shopping_fragment, container, false);
        mContext = view.getContext();

        ImageView snapdeal=view.findViewById(R.id.snapdeal);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1MO2UAX76S3O10WJgT3U26PQ2fykm171r&export=download")
                .into(snapdeal, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        snapdeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.snapdeal.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView flipkart=view.findViewById(R.id.flipkart);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1O8k7Xirh8ceXt5Vm_XCDCab7WuS1i3kC&export=download")
                .into(flipkart, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        flipkart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.flipkart.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView shopclues=view.findViewById(R.id.shopclues);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1aESLOoWBRJRpwN-ecUZ7h9Dh4hiMbciX&export=download")
                .into(shopclues, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        shopclues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.shopclues.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView ebay=view.findViewById(R.id.ebay);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1RYqRZXzLWIkP3JacadkYhgTKC1qxsZ7J&export=download")
                .into(ebay, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        ebay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.ebay.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView jabong=view.findViewById(R.id.jabong);
        Picasso.get()
                .load("https://drive.google.com/uc?id=13HqUxK41FmODUmTibtXLUV1MVa3lwoll&export=download")
                .into(jabong, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        jabong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.jabong.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView lenskart=view.findViewById(R.id.lenskart);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1FYuO6T8x9SzHLrLb8Gyvf8JiWvHE7oEu&export=download")
                .into(lenskart, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        lenskart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.lenskart.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView homeshop=view.findViewById(R.id.homeshop);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1uew1-TwwPxFQoEize-bFsFs5CWSuyrkA&export=download")
                .into(homeshop, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        homeshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.homeshop18.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView bigbasket=view.findViewById(R.id.bigbasket);
        Picasso.get()
                .load("https://drive.google.com/uc?id=17pPDHpyz7_VE2hk2tRaxnzkBQbuBtbB8&export=download")
                .into(bigbasket, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        bigbasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.bigbasket.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView paytmmall=view.findViewById(R.id.paytm_mall);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1YTxMD6gLcb89EaG09TruT1qqNR8A7rbW&export=download")
                .into(paytmmall, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        paytmmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://paytmmall.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView nykaa=view.findViewById(R.id.nykaa);
        Picasso.get()
                .load("https://drive.google.com/uc?id=105pJbgYGE0zdGQcJ4dbYvI6zyGNewj49&export=download")
                .into(nykaa, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        nykaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.nykaa.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView pepperfry=view.findViewById(R.id.peperfry);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1Y3o5i1nF8y8Dq_fzZbcDD4bIBVEWtdyy&export=download")
                .into(pepperfry, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        pepperfry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","https://www.pepperfry.com");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sendData("dismiss");
            }
        });

        ImageView yebhi=view.findViewById(R.id.yebhi);
        Picasso.get()
                .load("https://drive.google.com/uc?id=1SLDd0w6N7siqGra9OIhzMYRy4N8t4pYr&export=download")
                .into(yebhi, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        yebhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("sateurl").putExtra("sateurl","http://yebhi.com");
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
