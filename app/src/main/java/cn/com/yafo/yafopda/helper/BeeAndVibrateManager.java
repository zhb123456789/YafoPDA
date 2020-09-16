package cn.com.yafo.yafopda.helper;

import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import java.io.IOException;

import cn.com.yafo.yafopda.R;

/**
 * @author Is-Poson
 * @time 2017/9/13  11:05
 * @desc 提示音 + 手机震动管理类
 */

public class BeeAndVibrateManager {

    private static boolean shouldPlayBeep = true;

    /**
     * @param context      Context实例
     * @param milliseconds 震动时长 , 单位毫秒
     */
    public static void vibrate(Context context, long milliseconds) {

        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    /**
     * @param context  Context实例
     * @param pattern  自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]单位是毫秒
     * @param isRepeat true-> 反复震动，false-> 只震动一次
     */
    public static void vibrate(Context context, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        if(isRepeat)
        {vib.vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE));}
        else
        {vib.vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE));}

    }


    public static void playBee(final Context context,int rawId, final PlayerCompleteListener listener) {
        AudioManager audioService = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            shouldPlayBeep = false;//检查当前是否是静音模式
        }

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                player.seekTo(0);
            }
        });

        AssetFileDescriptor file = context.getResources().openRawResourceFd(rawId);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(),
                    file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(0, 1);
            mediaPlayer.prepare();
        } catch (IOException ioe) {
            mediaPlayer = null;
        }

        if (shouldPlayBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if(listener!=null) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    listener.onCompletion(mp);
                }
            });
        }
    }

    public static void playBeeAndVibrate(final Context context,int rawId, long milliseconds, PlayerCompleteListener listener) {
        //震动
        vibrate(context, milliseconds);
        //提示音
        playBee(context,rawId, listener);
    }

    //MediaPlayer播放完毕监听
    public interface PlayerCompleteListener {
        void onCompletion(MediaPlayer mp);
    }
}