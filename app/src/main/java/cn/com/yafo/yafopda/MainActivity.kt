package cn.com.yafo.yafopda

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.com.yafo.yafopda.helper.CrashHandler
import cn.com.yafo.yafopda.helper.GlobalVar
import cn.com.yafo.yafopda.vm.MainViewModel
import com.xiaoluo.updatelib.UpdateManager
import kotlinx.android.synthetic.main.main_activity.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    var fragmentmain: MainFragment ? = null
    /**
     * 上次点击返回键的时间
     */
    private var lastBackPressTime = -1L

    //private lateinit var viewModel: MainViewModel
    val userparam = MutableLiveData<String>()
    val handler : Handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                0 -> try {
                    msg.data.getString("appName")?.let { update(it,msg.data.getInt("appVer"),
                        msg.data.getString("appVerName")!!
                    ) }
                } catch (ex : Throwable){
                    ex.printStackTrace()
                }
            }
        }
    }
    //两次返回退出 https://www.jianshu.com/p/4b70f0ba2296
    override fun finishAfterTransition() {
        if(popSupportBackStack()){
            return
        }
        val currentTIme = System.currentTimeMillis();
        if(lastBackPressTime == -1L || currentTIme - lastBackPressTime >= 2000){
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
            lastBackPressTime = currentTIme
        }else{
            //退出应用
            finish()
        }
    }
    /**
     * @return true:没有Fragment弹出 false:有Fragment弹出
     */
    private fun popSupportBackStack():Boolean{
        // 当Fragment状态保存了
        return supportFragmentManager.isStateSaved
                ||supportFragmentManager.popBackStackImmediate()
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 注册crashHandler
        CrashHandler.getInstance()
            .initHandler { t, e -> Toast.makeText(applicationContext, "未处理异常：" + t.name + e.toString(), Toast.LENGTH_LONG).show() }


        //自动更新
        checkUpdate()

        setContentView(R.layout.main_activity)

        appVer.text = "V:"+getVersionCode(this)

        GlobalVar.userVM = ViewModelProvider(this)[MainViewModel::class.java] // 关键代码
       // viewModel.username.value=("22222111")

        GlobalVar.userVM.username.observe(this, Observer {
           // Log.e(TAG, "updated: $it")
            mainUser.text = it
        })


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

//    fun btnSNonClick(v: View) {
//
//        // 参数设置
//        val bundle = Bundle()
//        bundle.putString("name", "TeaOf")
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.container, SnMainFragment.newInstance("1","2"))
//            .addToBackStack("").commit()
//    }

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
    fun update(appName:String ,verCode:Int,verName:String)
    {
        //https://www.jianshu.com/p/e8449ea77280
        UpdateManager.getInstance().init(this) // 获取实例并初始化,必要
            .compare(UpdateManager.COMPARE_VERSION_NAME) // 通过版本号或版本名比较,默认版本号
            .downloadUrl(GlobalVar.GetUrl("/fordownload/yafopad/$appName")) // 下载地址,必要
            .downloadTitle("正在下载洋帆手持机程序") // 下载标题
            .lastestVerName(verName) // 最新版本名
            .lastestVerCode(verCode) // 最新版本号
            // .minVerName("1.0") // 最低版本名
            // .minVerCode(1) // 最低版本号
            // .isForce(true) // 是否强制更新,true无视版本直接更新
            .update() // 开始更新
        // 设置版本对比回调
        // .setListener { result -> Toast.makeText(this, result, Toast.LENGTH_SHORT).show() }
    }
    //检查更新
    fun checkUpdate() {
        val client = OkHttpClient()
        val request = Request.Builder().get()
            .url(GlobalVar.GetUrl("/fordownload/yafopad/output.json"))
            .build()
        val call = client.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("UPDATE", "onFailure: $e")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                var jsonArray =JSONArray(response.body().string())
                val jsonObject = jsonArray.getJSONObject(0)

                var appName = jsonObject["path"].toString()
                val data= JSONObject(jsonObject.getString("apkData"))
                var appVer = data.getInt("versionCode")
                var appVerName = data.getString("versionName")

                val message = Message.obtain()
                message.what = 0

                //传递复杂类型的消息
                val bundleData = Bundle()
                bundleData.putString("appName", appName)
                bundleData.putString("appVerName", appVerName)
                bundleData.putInt("appVer", appVer)
                message.data = bundleData
                handler.sendMessage(message)
            }
        })
    }
}

