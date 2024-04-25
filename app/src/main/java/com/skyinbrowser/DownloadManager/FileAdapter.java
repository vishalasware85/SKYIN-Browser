package com.skyinbrowser.DownloadManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skyinbrowser.BuildConfig;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Status;
import com.skyinbrowser.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    @NonNull
    private final List<DownloadData> downloads = new ArrayList<>();
    @NonNull
    private final ActionListener actionListener;

    FileAdapter(@NonNull final ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.actionButton.setOnClickListener(null);
        holder.actionButton.setEnabled(true);

        final DownloadData downloadData = downloads.get(position);
        String url = "";
        if (downloadData.download != null) {
            url = downloadData.download.getUrl();
        }
        final Uri uri = Uri.parse(url);
        final Status status = downloadData.download.getStatus();
        final Context context = holder.itemView.getContext();

        holder.titleTextView.setText(uri.getLastPathSegment());
        holder.statusTextView.setText(getStatusString(status));

        int progress = downloadData.download.getProgress();
        if (progress == -1) { // Download progress is undermined at the moment.
            progress = 0;
        }
        holder.progressBar.setProgress(progress);
        holder.progressTextView.setText(context.getString(R.string.percent_progress, progress));

        if (downloadData.eta == -1) {
            holder.timeRemainingTextView.setText("");
        } else {
            holder.timeRemainingTextView.setText(Utils.getETAString(context, downloadData.eta));
        }

        if (downloadData.downloadedBytesPerSecond == 0) {
            holder.downloadedBytesPerSecondTextView.setText("");
        } else {
            holder.downloadedBytesPerSecondTextView.setText(Utils.getDownloadSpeedString(context, downloadData.downloadedBytesPerSecond));
        }

        switch (status) {
            case COMPLETED: {
                holder.actionButton.setText(R.string.view);
                holder.actionButton.setOnClickListener(view -> {
                    try {
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        File file = new File(downloadData.download.getFile());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        MimeTypeMap mime=MimeTypeMap.getSingleton();
                        String ext=file.getName().substring(file.getName().lastIndexOf(".") + 1);
                        String type=mime.getMimeTypeFromExtension(ext);
                        Uri fileUri= FileProvider.getUriForFile(context.getApplicationContext(),context.getPackageName()+".provider",file);
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                            if (ext.equals("apk")){
                                try {
                                    Intent intent1 = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                                    intent1.setData( fileUri );
                                    intent1.setFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK );
                                    intent1.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                    intent1.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                                    intent1.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, context.getApplicationInfo().packageName);
                                    if ( context.getPackageManager().queryIntentActivities(intent, 0 ) != null ) {// checked on start Activity
                                        context.startActivity(intent1);
                                    } else {
                                        throw new Exception("don`t start Activity.");
                                    }
                                } catch ( Exception e ) {
                                    Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                                            .show();
                                }
                            }else {
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(fileUri,type);
                                context.startActivity(intent);
                            }
                        }else {
                            if (ext.equals("apk")){
                                try {
                                    Intent intent1 = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                                    intent1.setData( fileUri );
                                    intent1.setFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK );
                                    intent1.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                    intent1.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                                    intent1.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, context.getApplicationInfo().packageName);
                                    if ( context.getPackageManager().queryIntentActivities(intent, 0 ) != null ) {// checked on start Activity
                                        context.startActivity(intent1);
                                    } else {
                                        throw new Exception("don`t start Activity.");
                                    }
                                } catch ( Exception e ) {
                                    Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                                            .show();
                                }
                            }else {
                                intent.setDataAndType(Uri.fromFile(file),type);
                                context.startActivity(intent);
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case FAILED: {
                holder.actionButton.setText(R.string.retry);
                holder.actionButton.setOnClickListener(view -> {
                    holder.actionButton.setEnabled(false);
                    actionListener.onRetryDownload(downloadData.download.getId());
                });
                break;
            }
            case PAUSED: {
                holder.actionButton.setText(R.string.resume);
                holder.actionButton.setOnClickListener(view -> {
                    holder.actionButton.setEnabled(false);
                    actionListener.onResumeDownload(downloadData.download.getId());
                });
                break;
            }
            case DOWNLOADING:
            case QUEUED: {
                holder.actionButton.setText(R.string.pause);
                holder.actionButton.setOnClickListener(view -> {
                    holder.actionButton.setEnabled(false);
                    actionListener.onPauseDownload(downloadData.download.getId());
                });
                break;
            }
            case ADDED: {
                holder.actionButton.setText(R.string.download);
                holder.actionButton.setOnClickListener(view -> {
                    holder.actionButton.setEnabled(false);
                    actionListener.onResumeDownload(downloadData.download.getId());
                });
                break;
            }
            default: {
                break;
            }
        }

        //Set delete action
        holder.itemView.setOnLongClickListener(v -> {
            final Uri uri12 = Uri.parse(downloadData.download.getUrl());
            //new AlertDialog.Builder(context)
            //        .setMessage(context.getString(R.string.delete_title, uri12.getLastPathSegment()))
            //        .setPositiveButton(R.string.delete, (dialog, which) -> actionListener.onRemoveDownload(downloadData.download.getId()))
            //        .setNegativeButton(R.string.cancel, null)
            //        .show();

            Bundle modify_intent=new Bundle();
            modify_intent.putString("downloadURL",downloadData.download.getUrl());
            modify_intent.putInt("downloadId", downloadData.download.getId());
            Intent intent=new Intent("downloadFile").putExtra("file",downloadData.download.getFile());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            FragmentTransaction fragmentTransaction=((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);

            DialogFragment deleteActivity=new DownloadLongClick();
            deleteActivity.setArguments(modify_intent);
            deleteActivity.show(fragmentTransaction,"dialog");

            //android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(context);
            //builder.setCancelable(true);
            //builder.setTitle("Warning!");
            //builder.setMessage(context.getString(R.string.delete_title, uri12.getLastPathSegment()));
            //builder.setIcon(android.R.drawable.ic_dialog_alert);
            //builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
            //    @Override
            //    public void onClick(DialogInterface dialog, int which) {
            //        actionListener.onRemoveDownload(downloadData.download.getId());
            //        dialog.cancel();
            //    }
            //});
//
            //builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            //    @Override
            //    public void onClick(DialogInterface dialog, int which) {
            //        dialog.cancel();
            //    }
            //});
//
            //builder.show();

            return true;
        });

        LocalBroadcastManager.getInstance(context).registerReceiver(delete,new IntentFilter("deleteDownload"));
    }

    private BroadcastReceiver delete=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id= intent.getIntExtra("downloadId",0);
            actionListener.onRemoveDownload(id);
        }
    };

    public void addDownload(@NonNull final Download download) {
        boolean found = false;
        DownloadData data = null;
        int dataPosition = -1;
        for (int i = 0; i < downloads.size(); i++) {
            final DownloadData downloadData = downloads.get(i);
            if (downloadData.id == download.getId()) {
                data = downloadData;
                dataPosition = i;
                found = true;
                break;
            }
        }
        if (!found) {
            final DownloadData downloadData = new DownloadData();
            downloadData.id = download.getId();
            downloadData.download = download;
            downloads.add(downloadData);
            notifyItemInserted(downloads.size() - 1);
        } else {
            data.download = download;
            notifyItemChanged(dataPosition);
        }
    }

    @Override
    public int getItemCount() {
        return downloads.size();
    }

    public void update(@NonNull final Download download, long eta, long downloadedBytesPerSecond) {
        for (int position = 0; position < downloads.size(); position++) {
            final DownloadData downloadData = downloads.get(position);
            if (downloadData.id == download.getId()) {
                switch (download.getStatus()) {
                    case REMOVED:
                    case DELETED: {
                        downloads.remove(position);
                        notifyItemRemoved(position);
                        break;
                    }
                    default: {
                        downloadData.download = download;
                        downloadData.eta = eta;
                        downloadData.downloadedBytesPerSecond = downloadedBytesPerSecond;
                        notifyItemChanged(position);
                    }
                }
                return;
            }
        }
    }

    private String getStatusString(Status status) {
        switch (status) {
            case COMPLETED:
                return "Done";
            case DOWNLOADING:
                return "Downloading";
            case FAILED:
                return "Error";
            case PAUSED:
                return "Paused";
            case QUEUED:
                return "Waiting in Queue";
            case REMOVED:
                return "Removed";
            case NONE:
                return "Not Queued";
            default:
                return "Unknown";
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView titleTextView;
        final TextView statusTextView;
        public final ProgressBar progressBar;
        public final TextView progressTextView;
        public final Button actionButton;
        final TextView timeRemainingTextView;
        final TextView downloadedBytesPerSecondTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            statusTextView = itemView.findViewById(R.id.status_TextView);
            progressBar = itemView.findViewById(R.id.progressBar);
            actionButton = itemView.findViewById(R.id.actionButton);
            progressTextView = itemView.findViewById(R.id.progress_TextView);
            timeRemainingTextView = itemView.findViewById(R.id.remaining_TextView);
            downloadedBytesPerSecondTextView = itemView.findViewById(R.id.downloadSpeedTextView);
        }

    }

    public static class DownloadData {
        public int id;
        @Nullable
        public Download download;
        long eta = -1;
        long downloadedBytesPerSecond = 0;

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            if (download == null) {
                return "";
            }
            return download.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof DownloadData && ((DownloadData) obj).id == id;
        }
    }
}
