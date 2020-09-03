package cn.com.yafo.yafopda.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.com.yafo.yafopda.R;

public class Update {

    // 应用程序Context
    private Context mContext;
    // 下载安装包的网络路径
    private String apkUrl;
    private static final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/yafopda.apk";// 保存apk的文件夹
    // 进度条与通知UI刷新的handler和msg常量
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;// 当前进度
    private boolean interceptFlag = false;// 用户取消下载
    // 通知处理刷新界面的handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public Update(Context context, String apkUrl) {
        this.mContext = context;
        this.apkUrl = apkUrl;
    }
    // 显示更新程序对话框，供主程序调用
    public void checkUpdateInfo() {
//      showNoticeDialog();
        showDownloadDialog();
    }

    /**
     * 询问是否更新
     */
    private void showNoticeDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                mContext);// Builder，可以通过此builder设置改变AleartDialog的默认的主题样式及属性相关信息
        builder.setTitle("软件版本更新");
        String updateMsg = "有最新的软件包，请下载！";
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 更新进度对话框
     */
    private void showDownloadDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setTitle("软件版本更新");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams")
        View v = inflater.inflate(R.layout.progress, null);
       // mProgress = v.findViewById(R.id.progress);
        builder.setCancelable(false);
        builder.setView(v);// 设置对话框的内容为一个View
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
                System.exit(0);//强制更新，取消更新便退出程序
            }
        });
        Dialog downloadDialog = builder.create();
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.show();
        downloadApk();
    }

    /**
     * 启动下载apk线程
     */
    private void downloadApk() {
        Thread downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 打开apk启动安装，并在安装完成后自动打开
     */
    private void installApk() {
        File apkfile = new File(savePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装完成后自动打开
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //AndroidManifest provider authorities
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.overc_i3.kydz", apkfile);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//对目标应用临时授权该Uri所代表的文件
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
//            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
//                    "application/vnd.android.package-archive");// File.toString()会返回路径信息
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            URL url;
            try {
                url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream ins = conn.getInputStream();
                FileOutputStream outStream = new FileOutputStream(savePath);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = ins.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 下载进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    outStream.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消停止下载
                outStream.close();
                ins.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}