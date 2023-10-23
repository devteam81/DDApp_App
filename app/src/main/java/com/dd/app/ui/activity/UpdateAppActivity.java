package com.dd.app.ui.activity;

import static com.dd.app.network.APIConstants.Params.APPVERSION;
import static com.dd.app.network.APIConstants.Params.VERSION_CODE;
import static com.dd.app.util.download.DownloadUtils.getFileSize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.app.BuildConfig;
import com.dd.app.R;
import com.dd.app.network.APIClient;
import com.dd.app.network.APIInterface;
import com.dd.app.util.UiUtils;
import com.dd.app.util.download.DownloadCancelReceiver;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class UpdateAppActivity extends AppCompatActivity {

    private final String TAG = UpdateAppActivity.class.getSimpleName();
    private final int STORAGE_PERMISSION = 124;

    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.textViewAlreadyUpdated)
    TextView textViewAlreadyUpdated;
    @BindView(R.id.ll_app_update)
    LinearLayoutCompat ll_app_update;

    @BindView(R.id.btnInstall)
    Button btnInstall;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.textViewProgressText)
    TextView textViewProgressText;
    @BindView(R.id.textViewProgress)
    TextView textViewProgress;


    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    APIInterface apiInterface;
    int currentVersionCode;
    String currentVersionName;

    String url = "https://www.bebu.app/apk/Bebu.apk";
    String dirPath;// = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
    String fileName = "Bebu.apk";

    NotificationManager manager;
    HashMap<Integer, Long> map = new HashMap<>();
    int notificationId = 500;
    int downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);
        ButterKnife.bind(this);
        img_back.setOnClickListener(v -> onBackPressed());
        //apiInterface = APIClient.getClient().create(APIInterface.class);
        currentVersionCode = getIntent().getIntExtra(VERSION_CODE, -1);
        currentVersionName = getIntent().getStringExtra(APPVERSION);
        PRDownloader.cancelAll();
        //dirPath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        dirPath = getRootDirPath(this) + "/";
        UiUtils.log(TAG,"Path: "+ dirPath);
        if (currentVersionCode <= BuildConfig.VERSION_CODE)
        {
            ll_app_update.setVisibility(View.GONE);
            textViewAlreadyUpdated.setVisibility(View.VISIBLE);
            UiUtils.log(TAG,"Latest version");
        }

        btnUpdate.setOnClickListener(v-> updateAppPR());
        btnInstall.setOnClickListener(v-> installAPK());
    }

    @Override
    protected void onResume() {
       super.onResume();
    }

    private void updateAppPR()
    {

        try {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (Build.VERSION.SDK_INT >= 33)
                perms =new String[] {Manifest.permission.READ_MEDIA_IMAGES/*, Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.READ_MEDIA_VIDEO*/};
            if (EasyPermissions.hasPermissions(UpdateAppActivity.this, perms)) {

                if (Status.RUNNING == PRDownloader.getStatus(downloadId)) {
                    PRDownloader.pause(downloadId);
                    return;
                }

                //progress_bar.setIndeterminate(true);
                /*progress_bar.getIndeterminateDrawable().setColorFilter(
                        Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);*/

                if (Status.PAUSED == PRDownloader.getStatus(downloadId)) {
                    PRDownloader.resume(downloadId);
                    return;
                }

                manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (!map.containsKey(notificationId)) {
                    map.put(notificationId, 0L);
                    map.put(100, 0L);
                }
                int id = (int) System.currentTimeMillis();
                NotificationCompat.Builder builder = showDownloadingNotification(UpdateAppActivity.this, fileName, "");

                downloadId = PRDownloader.download(url, dirPath, fileName)
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {
                                UiUtils.log(TAG, "onStartOrResume");
                                progress_bar.setIndeterminate(false);
                                //btnUpdate.setEnabled(true);
                                btnUpdate.setText(R.string.pause);
                                textViewProgressText.setText("Downloading...");
                                //btnCancel.setEnabled(true);
                                btnCancel.setVisibility(View.VISIBLE);
                                btnInstall.setVisibility(View.GONE);
                            }
                        })
                        .setOnPauseListener(new OnPauseListener() {
                            @Override
                            public void onPause() {
                                UiUtils.log(TAG, "setOnPauseListener");
                                btnUpdate.setText(R.string.resume);
                                textViewProgressText.setText("Paused");
                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel() {
                                UiUtils.log(TAG, "setOnCancelListener");
                                btnUpdate.setText(R.string.update);
                                //btnCancel.setEnabled(false);
                                btnCancel.setVisibility(View.GONE);
                                progress_bar.setProgress(0);
                                textViewProgressText.setText("");
                                textViewProgress.setText("");
                                downloadId = 0;
                                progress_bar.setIndeterminate(false);
                                manager.cancel(notificationId);
                            }
                        })
                        .setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onProgress(Progress progress) {
                                long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                                progress_bar.setProgress((int) progressPercent);
                                //UiUtils.log("Progress", "Percentage: " + (int) progressPercent + "%");
                                textViewProgress.setText(getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                                progress_bar.setIndeterminate(false);
                                if (progress.currentBytes - (map.get(notificationId)) > 300000) {
                                    map.put(notificationId, progress.currentBytes);
                                    builder.setContentText(getFileSize(progress.currentBytes) + " / " + getFileSize(progress.totalBytes));
                                    builder.setProgress(100, (int) ((progress.currentBytes * 100) / progress.totalBytes), false);
                                    manager.notify(notificationId, builder.build());
                                    //progress_bar.setMax(100);
                                    //progress_bar.setProgress((int) ((progress.currentBytes * 100) / progress.totalBytes));
                                }
                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                UiUtils.log(TAG, "onDownloadComplete");
                                btnCancel.setVisibility(View.GONE);
                                btnUpdate.setText("Download");
                                textViewProgressText.setText("Ready To Install");
                                btnInstall.setVisibility(View.VISIBLE);
                                manager.cancel(notificationId);
                                installAPK();
                            }

                            @Override
                            public void onError(Error error) {
                                UiUtils.log(TAG, "onError");
                                UiUtils.log(TAG, "onError->" + error.getServerErrorMessage());
                                //UiUtils.log(TAG, "onError->" + error.getConnectionException().getMessage());
                                btnUpdate.setText(R.string.update);
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                textViewProgressText.setText("");
                                textViewProgress.setText("");
                                progress_bar.setProgress(0);
                                PRDownloader.cancelAll();
                                manager.cancel(notificationId);
                                downloadId = 0;
                                //btnCancel.setEnabled(false);
                                btnCancel.setVisibility(View.GONE);
                                progress_bar.setIndeterminate(false);
                                //btnUpdate.setEnabled(true);
                            }

                        });


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PRDownloader.cancel(downloadId);
                    }
                });

                UiUtils.log(TAG, "downloadId: " + downloadId);
                NotificationCompat.Action cancelAction = getCancelActionForVideoId(id, downloadId);
                builder.addAction(cancelAction);
                manager.notify(notificationId, builder.build());
            }else {
                getPermission();
            }
        }catch (Exception e)
        {
            UiUtils.log(TAG,Log.getStackTraceString(e));
        }

    }

    public void getPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            EasyPermissions.requestPermissions(this, getString(R.string.needPermissionToAccessYourStoarge),
                    STORAGE_PERMISSION, Manifest.permission.READ_MEDIA_IMAGES/*,
                    Manifest.permission.READ_MEDIA_AUDIO,Manifest.permission.READ_MEDIA_VIDEO*/);
        }else
        {
            EasyPermissions.requestPermissions(this, getString(R.string.needPermissionToAccessYourStoarge),
                    STORAGE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private NotificationCompat.Builder showDownloadingNotification(Context context, String title, String message) {

        String channelId = "updateApp";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(channelId, context.getString(R.string.channel_name), importance);
            mChannel.enableVibration(false);
            manager.createNotificationChannel(mChannel);
        }

        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


        return new NotificationCompat.Builder(context, channelId).setContentTitle(title)
                .setSubText("Download Version "+ currentVersionName)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("")
                .setOngoing(true)
                .setVibrate(new long[]{0l})
                .setContentIntent(contentIntent)
                .setProgress(100, 0, false);
    }

    private void hideDownloadingNotification(final int id) {
        manager.cancel(id);
    }

    private NotificationCompat.Action getCancelActionForVideoId(int uniqueId, int downloadId) {
        UiUtils.log(TAG,"downloadId: "+downloadId );
        Intent cancel = new Intent(BuildConfig.APPLICATION_ID + ".CANCEL_DOWNLOAD");
        cancel.putExtra(DownloadCancelReceiver.EXTRA_ADMIN_ID, downloadId);
        cancel.putExtra(DownloadCancelReceiver.EXTRA_NOTIFICATION_ID,500);
        cancel.putExtra(DownloadCancelReceiver.EXTRA_URL_ID, url);
        cancel.putExtra(DownloadCancelReceiver.EXTRA_SAVE_PATH_ID, dirPath);
        cancel.putExtra(DownloadCancelReceiver.EXTRA_FILE_NAME_ID, fileName);
        cancel.setClass(UpdateAppActivity.this, DownloadCancelReceiver.class);
        PendingIntent cancelIntent = PendingIntent.getBroadcast(getApplicationContext(), uniqueId, cancel, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_close_clear_cancel, "Cancel download", cancelIntent)
                .build();
    }


    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes){
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }

    private void installAPK()
    {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(UpdateAppActivity.this, getApplicationContext().getPackageName() + ".myprovider", new File(dirPath + fileName));
                            /*Uri contentUri = FileProvider.getUriForFile(
                                    activity,
                                    activity.getApplicationContext().getPackageName()  + ".myprovider",
                                    new File(finalDestination)
                            );*/
            UiUtils.log(TAG, contentUri.getPath());
            UiUtils.log(TAG, "Install Start");

            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            //install.setData(contentUri);
            install.setDataAndType(contentUri, "application/vnd.android.package-archive");
            startActivity(install);
            UiUtils.log(TAG, "Install Complete");
        } else{
            Uri uri = Uri.parse("file://" + dirPath + fileName);
            UiUtils.log(TAG, uri.getPath());
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            //install.setDataAndType(uri, "application/vnd.android.package-archive"/*manager.getMimeTypeForDownloadedFile(downloadId)*/);
            startActivity(install);
            UiUtils.log(TAG, "Download Install");
        }
        //install.setDataAndType(uri, "application/vnd.android.package-archive");

    }

}