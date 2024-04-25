package com.skyinbrowser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.floating_bubble.overlay.OverlayPermission;
import com.skyinbrowser.Addons.AddonsActivity;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.IOnBackPressed;
import com.skyinbrowser.DownloadManager.DownloadActivity;
import com.suke.widget.SwitchButton;
import com.skyinbrowser.Floating.SingleSectionNotificationHoverMenuService;
import com.skyinbrowser.split.SpltVertical;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

public class BottomSheetLayout extends Fragment implements IOnBackPressed {

    String cityname;
    public static Activity bottomSheet;
    CardView shareLayout;
    Button copyUrl,directShare;
    TextView tvappUrl,txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt11,txt12,txt13,txt14,txt15,txt16,txt17;

    ImageView openNingImage;
    MaterialRippleLayout morebtnt,addons,feedback,about,vpnBtn;
    CardView morebtLayout,bgChangeInco,incoSwitchLayout,btCard,btSheetArrow;
    SwitchButton mainWebSwitcher,floatWebSwitcher,splitWebSwitcher,floatSwitch;
    private LinearLayout bottomsheet;

    private static final int REQUEST_CODE_HOVER_PERMISSION=300;
    boolean mPermissionsRequested = false;
    private String titletext="";

    private FragmentToActivity mCallback;
    private Context mContext;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_bottom__sheet_layout, container, false);
        mContext = view.getContext();

        MaterialRippleLayout history = view.findViewById(R.id.history);
        MaterialRippleLayout download = view.findViewById(R.id.download);
        ImageView share = view.findViewById(R.id.share);
        MaterialRippleLayout incognito = view.findViewById(R.id.incognito);
        MaterialRippleLayout settings = view.findViewById(R.id.settings);
        MaterialRippleLayout bookmarks = view.findViewById(R.id.bookmarks);
        MaterialRippleLayout splitMode = view.findViewById(R.id.splitmode);
        MaterialRippleLayout desktopMode = view.findViewById(R.id.desktopMode);
        ImageButton close = view.findViewById(R.id.close);
        morebtnt=view.findViewById(R.id.morebtsheet);
        addons=view.findViewById(R.id.addons);
        bgChangeInco=view.findViewById(R.id.btsheetBackgroungInco);
        morebtLayout=view.findViewById(R.id.moreBtLayout);
        shareLayout=view.findViewById(R.id.shareLayoutBTSHEET);
        copyUrl=view.findViewById(R.id.copyurlBtn);
        directShare=view.findViewById(R.id.directShareBtn);
        tvappUrl=view.findViewById(R.id.appUrl);
        incoSwitchLayout=view.findViewById(R.id.incoSwitchLayout);
        mainWebSwitcher=view.findViewById(R.id.mainwebInco);
        floatWebSwitcher=view.findViewById(R.id.floatwebInco);
        splitWebSwitcher=view.findViewById(R.id.splitwebInco);
        floatSwitch=view.findViewById(R.id.floatSwitchBT);
        openNingImage=view.findViewById(R.id.openNingImageFromBtSheet);
        feedback=view.findViewById(R.id.feedback);
        about=view.findViewById(R.id.about);
        btCard=view.findViewById(R.id.btmainCard);
        txt1=view.findViewById(R.id.btSheettxt1);
        txt2=view.findViewById(R.id.btSheettxt2);
        txt3=view.findViewById(R.id.btSheettxt3);
        txt4=view.findViewById(R.id.btSheettxt4);
        txt5=view.findViewById(R.id.btSheettxt5);
        txt6=view.findViewById(R.id.btSheettxt6);
        txt7=view.findViewById(R.id.btSheettxt7);
        txt8=view.findViewById(R.id.btSheettxt8);
        txt9=view.findViewById(R.id.btSheettxt9);
        txt10=view.findViewById(R.id.btSheettxt10);
        txt11=view.findViewById(R.id.btSheettxt11);
        txt12=view.findViewById(R.id.btSheettxt12);
        txt13=view.findViewById(R.id.btSheettxt13);
        txt14=view.findViewById(R.id.btSheettxt14);
        txt15=view.findViewById(R.id.btSheettxt15);
        txt16=view.findViewById(R.id.btSheettxt16);
        txt17=view.findViewById(R.id.btSheettxt17);
        btSheetArrow=view.findViewById(R.id.btSheetArrow);
        bottomsheet=view.findViewById(R.id.bottomsheet);
        vpnBtn=view.findViewById(R.id.vpnBTSheet);

            Animation slide_up= AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_up);
            bottomsheet.startAnimation(slide_up);
            bottomsheet.setVisibility(View.VISIBLE);

        SharedPreferences preferences=mContext.getSharedPreferences("Incognito_Mode",MODE_PRIVATE);
        if (preferences.contains("mainIncoOn")){
            bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.btsheetInco));
            mainWebSwitcher.setChecked(true);
        }else {
            bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.transparent));
            mainWebSwitcher.setChecked(false);
        }

        if (preferences.contains("floatIncoOn")){
            bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.btsheetInco));
            floatWebSwitcher.setChecked(true);
        }else {
            bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.transparent));
            floatWebSwitcher.setChecked(false);
        }

        if (preferences.contains("splitIncoOn")){
            bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.btsheetInco));
            splitWebSwitcher.setChecked(true);
        }else {
            bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.transparent));
            splitWebSwitcher.setChecked(false);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, DownloadActivity.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("historyActivity");
            }
        });

        bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("bookmarkActivity");
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("SettingsActivity");
            }
        });

        floatSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    if (!mPermissionsRequested && !OverlayPermission.hasRuntimePermissionToDrawOverlay(mContext)) {
                        @SuppressWarnings("NewApi")
                        Intent myIntent = OverlayPermission.createIntentToRequestOverlayPermission(mContext);
                        startActivityForResult(myIntent, REQUEST_CODE_HOVER_PERMISSION);
                    }else {
                        Intent startHoverIntent = new Intent(mContext, SingleSectionNotificationHoverMenuService.class);
                        mContext.startService(startHoverIntent);
                    }
                }else {
                    Intent stopHoverService=new Intent(mContext,SingleSectionNotificationHoverMenuService.class);
                    mContext.stopService(stopHoverService);
                }
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("FeedbackActivity");
            }
        });

        splitMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,SpltVertical.class);
                startActivity(intent);
            }
        });

        SharedPreferences preferences1=mContext.getSharedPreferences("desktopMode",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences1.edit();
        if (preferences1.contains("modeOn")){

        }else {

        }

        desktopMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences1.contains("modeOn")){
                    editor.remove("modeOn");
                    editor.apply();
                    bottomsheet.setVisibility(View.GONE);
                    btSheetArrow.setVisibility(View.INVISIBLE);
                }else {
                    editor.putString("modeOn","modeOn");
                    editor.apply();
                    Intent intent1=new Intent();
                    bottomsheet.setVisibility(View.GONE);
                    btSheetArrow.setVisibility(View.INVISIBLE);
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (morebtLayout.getVisibility()==View.VISIBLE){
                //    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),
                //            R.anim.morrebtsheet_fadeout);
                //    morebtLayout.startAnimation(animation);
                //    animation.setAnimationListener(new Animation.AnimationListener() {
                //        @Override
                //        public void onAnimationStart(Animation animation) { }
                //        @Override
                //        public void onAnimationEnd(Animation animation) {
                //            morebtLayout.setVisibility(View.INVISIBLE);
                //        }
                //        @Override
                //        public void onAnimationRepeat(Animation animation) { }
                //    });
                //}
                //if (shareLayout.getVisibility()==View.GONE){
                //    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),
                //            R.anim.slide_down_fade_in);
                //    shareLayout.startAnimation(animation);
                //    shareLayout.setVisibility(View.VISIBLE);
                //}else {
                //    Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),
                //            R.anim.slide_up_fade_out);
                //    shareLayout.startAnimation(animation);
                //    shareLayout.setVisibility(View.GONE);
                //}
                Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        morebtnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareLayout.getVisibility()==View.VISIBLE){
                    Animation animation=AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_up_fade_out);
                    shareLayout.startAnimation(animation);
                    shareLayout.setVisibility(View.GONE);
                }

                if (incoSwitchLayout.getVisibility()==View.VISIBLE){
                    incoSwitchLayout.setVisibility(View.GONE);
                }

                if (morebtLayout.getVisibility()==View.INVISIBLE){
                    Animation animation=AnimationUtils.loadAnimation(mContext,
                            R.anim.morebt_sheet_fadwein);
                    morebtLayout.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            morebtLayout.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }else {
                    Animation animation=AnimationUtils.loadAnimation(mContext,
                            R.anim.morrebtsheet_fadeout);
                    morebtLayout.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            morebtLayout.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }
            }
        });

        addons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,AddonsActivity.class);
                startActivity(intent);
            }
        });

        mainWebSwitcher.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    SharedPreferences preferences = mContext.getSharedPreferences("Incognito_Mode", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("mainIncoOn", "mainIncoOn");
                    editor.apply();

                    Intent intent=new Intent("Incognito_ModeOnn");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }else {
                    SharedPreferences preferences = mContext.getSharedPreferences("Incognito_Mode", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("mainIncoOn");
                    editor.apply();

                    Intent intent=new Intent("Incognito_ModeOff");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        });

        floatWebSwitcher.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    SharedPreferences preferences = mContext.getSharedPreferences("Incognito_Mode", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("floatIncoOn", "floatIncoOn");
                    editor.apply();

                    Intent intent=new Intent("float_Incognito_ModeOnn").putExtra("turnedOnn","turnedOnn");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.btsheetInco));
                }else {
                    SharedPreferences preferences = mContext.getSharedPreferences("Incognito_Mode", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("floatIncoOn");
                    editor.apply();

                    Intent intent=new Intent("float_Incognito_ModeOff").putExtra("turnedOff","turnedOff");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        splitWebSwitcher.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    SharedPreferences preferences = mContext.getSharedPreferences("Incognito_Mode", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("splitIncoOn", "splitIncoOn");
                    editor.apply();

                    bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.btsheetInco));
                }else {
                    SharedPreferences preferences = mContext.getSharedPreferences("Incognito_Mode", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("splitIncoOn");
                    editor.apply();

                    bgChangeInco.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        incognito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morebtLayout.getVisibility()==View.VISIBLE){
                    Animation animation=AnimationUtils.loadAnimation(mContext,
                            R.anim.morrebtsheet_fadeout);
                    morebtLayout.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            morebtLayout.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }

                if (shareLayout.getVisibility()==View.VISIBLE){
                    Animation animation=AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_up_fade_out);
                    shareLayout.startAnimation(animation);
                    shareLayout.setVisibility(View.GONE);
                }

                if (incoSwitchLayout.getVisibility()==View.GONE){
                    incoSwitchLayout.setVisibility(View.VISIBLE);
                }else {
                    incoSwitchLayout.setVisibility(View.GONE);
                }
            }
        });

        copyUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appurl=tvappUrl.getText().toString();
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(appurl);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("appurl", appurl);
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

        directShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo applicationInfo=mContext.getApplicationInfo();
                String appPath=applicationInfo.sourceDir;

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("application/vnd.android.package-archive");
                intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(appPath)));
                startActivity(Intent.createChooser(intent,"SHARE SK!yn USING"));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("AboutActivity");
            }
        });

        vpnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(mContext, VpnMainActivity.class);
//                startActivity(intent);
            }
        });

        LocalBroadcastManager.getInstance(mContext).registerReceiver(incoSwitchONN, new IntentFilter("Incognito_ModeOnn"));
        LocalBroadcastManager.getInstance(mContext).registerReceiver(incoSwitchOFF, new IntentFilter("Incognito_ModeOff"));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_HOVER_PERMISSION==requestCode){
            mPermissionsRequested=true;
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private BroadcastReceiver incoSwitchONN = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mainWebSwitcher.setChecked(true);
        }
    };

    private BroadcastReceiver incoSwitchOFF = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mainWebSwitcher.setChecked(false);
        }
    };

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    @Override
    public boolean onBackPressed() {
        LinearLayout bottomsheet=view.findViewById(R.id.bottomsheet);
        if (shareLayout.getVisibility()==View.VISIBLE){
            Animation animation=AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_up_fade_out);
            shareLayout.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    shareLayout.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }else { }

        if (morebtLayout.getVisibility()==View.VISIBLE){
            Animation animation1=AnimationUtils.loadAnimation(mContext,
                    R.anim.morrebtsheet_fadeout);
            morebtLayout.startAnimation(animation1);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    morebtLayout.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }

        if (incoSwitchLayout.getVisibility()==View.VISIBLE){
            Animation animation=AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_out);
            incoSwitchLayout.setAnimation(animation);
            animation.setDuration(100);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    incoSwitchLayout.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }


        Animation slide_down= AnimationUtils.loadAnimation(mContext,
                R.anim.slide_down);
        bottomsheet.startAnimation(slide_down);
        slide_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                bottomsheet.setVisibility(View.GONE);
                btSheetArrow.setVisibility(View.INVISIBLE);
                sendData("bottomSheetClosed");
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        return true;
    }
}
