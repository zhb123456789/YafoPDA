package cn.com.yafo.yafopda.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.TreeSet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 *
 *
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。
 *                           如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框
 *                           实现该接口并注册为程序中的默认未捕获异常处理
 *                           这样当未捕获异常发生时，就可以做些异常处理操作
 *                           例如：收集异常信息，发送错误报告 等。
 *
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{

    /**自定义的异常处理回调*/
    private UncaughtExceptionCallBack uncaughtExceptionCallBack;

    public interface UncaughtExceptionCallBack{
        void caughtUncaughtException(Thread t,Throwable e);
    }

    private static CrashHandler crashHandler;

    public static synchronized CrashHandler getInstance(){
        if (crashHandler == null){
            synchronized (CrashHandler.class){
                if (crashHandler == null) {
                    crashHandler = new CrashHandler();
                }
            }
        }
        return crashHandler;
    }

    /**
     * 设置自定义的运行时异常处理
     * @param uncaughtExceptionCallBack
     */
    public void initHandler(final UncaughtExceptionCallBack uncaughtExceptionCallBack){
        this.uncaughtExceptionCallBack = uncaughtExceptionCallBack;
        Thread.setDefaultUncaughtExceptionHandler(this);
        /**
         * 为什么要通过new Handler.post方式而不是直接在主线程中任意位置执行
         * while (true) {
         *      try {
         *          Looper.loop();
         *      } catch (Throwable e) {
         *      }
         *  }
         *
         * 这是因为该方法是个死循环，若在主线程中，比如在Activity的onCreate中执行时
         * 会导致while后面的代码得不到执行，activity的生命周期也就不能完整执行，
         * 通过Handler.post方式可以保证不影响该条消息中后面的逻辑。
         *
         * 摘抄自：https://github.com/android-notes/Cockroach/blob/master/%E5%8E%9F%E7%90%86%E5%88%86%E6%9E%90.md
         */
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        Looper.loop();
                    } catch (Throwable e) {
                        if (uncaughtExceptionCallBack != null) {
                            uncaughtExceptionCallBack.caughtUncaughtException(Looper.getMainLooper().getThread(), e);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void uncaughtException(Thread t, final Throwable e) {
        if (uncaughtExceptionCallBack != null) {
            uncaughtExceptionCallBack.caughtUncaughtException(Looper.getMainLooper().getThread(), e);
        }
    }
}