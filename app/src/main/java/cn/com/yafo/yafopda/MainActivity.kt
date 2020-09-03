package cn.com.yafo.yafopda

import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Message
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import cn.com.yafo.yafopda.helper.CrashHandler
import cn.com.yafo.yafopda.helper.Loading
import cn.com.yafo.yafopda.vm.MainViewModel
import java.io.File


class MainActivity : AppCompatActivity() {
    var fragmentmain: MainFragment ? = null

    private lateinit var viewModel: MainViewModel
    val userparam = MutableLiveData<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crashHandler = CrashHandler.getInstance()
        // 注册crashHandler
        crashHandler.init(applicationContext)
        // 发送以前没发送的报告(可选)
        //crashHandler.sendPreviousReportsToServer()

        // 添加Loading cancle()是按返回键，Loading框关闭的回调，可以做取消加载请求的操作。
        val mLoading: Loading = object : Loading(this) {
            override fun cancle() {}
        }
        // 显示Loading
        mLoading.show();
        // 关闭Loading
        mLoading.dismiss();




        setContentView(R.layout.main_activity)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java] // 关键代码
        viewModel.username.set("22222")
        //按 HOME 键禁止回到桌面
        val intent = Intent("nlscan.action.HOMEKEY_SWITCH_STATE")
        intent.putExtra("ENABLE", false)
        this.sendBroadcast(intent)


//        //注册广播：
//        val  mFilter=  IntentFilter("nlscan.action.SCANNER_RESULT");
//        var mReceiver= BarCodeReceiver()
//
//        this.registerReceiver(mReceiver, mFilter);

        //设置扫描为“广播输出模式”，同时输出“自动换行”
        val intent2 =  Intent ("ACTION_BAR_SCANCFG");
        intent2.putExtra("EXTRA_SCAN_MODE", 3);
        intent.putExtra("EXTRA_SCAN_AUTOENT", 1);
        this.sendBroadcast(intent2);
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        if( fragmentmain != null ){
            supportFragmentManager.putFragment(outState,"fragmentmain", fragmentmain!!);
        }
        super.onSaveInstanceState(outState);
    }

    fun btnSNonClick(v: View) {

            // 参数设置
            val bundle = Bundle()
            bundle.putString("name", "TeaOf")
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, sn_fragment.newInstance("1","2"))
            .addToBackStack("").commit()
        }

    //获取当前版本号
    fun getVersionCode(context: Context): String? {
        try {
            val packageManager: PackageManager = context.packageManager
            val packageInfo: PackageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
    //检查更新 https://www.jianshu.com/p/f49036f5cd3b?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes
    fun checkUpdate()
    {
//        if (getVersionCode(activity)!!.toInt() < `object`.getString("verCode").toInt()) {
//            val msg: Message = Message.obtain()
//            msg.obj = downloadAdd + `object`.getString("apkname") //apk下载地址
//            handler.sendMessage(msg)
//        } else {
//            val apkfile = File(apk)
//            if (apkfile.isFile()) {
//                apkfile.delete()
//            }
//            if (tip) {
//                activity.runOnUiThread(Runnable { show(activity, "已经是最新版本", 1) })
//            }
//        }
    }

}

