package cn.com.yafo.yafopda

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import cn.com.yafo.yafopda.helper.CrashHandler
import cn.com.yafo.yafopda.helper.Loading
import cn.com.yafo.yafopda.helper.StaticStr
import cn.com.yafo.yafopda.vm.MainViewModel
import com.xiaoluo.updatelib.UpdateManager
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    var fragmentmain: MainFragment ? = null

    private lateinit var viewModel: MainViewModel
    val userparam = MutableLiveData<String>()
    val handler : Handler = object : Handler(){
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crashHandler = CrashHandler.getInstance()
        // 注册crashHandler
        crashHandler.init(applicationContext)
        // 发送以前没发送的报告(可选)
        //crashHandler.sendPreviousReportsToServer()

        //自动更新
        checkUpdate()



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
            .replace(R.id.container, SnMainFragment.newInstance("1","2"))
            .addToBackStack("").commit()
    }


    fun update(appName:String ,verCode:Int,verName:String)
    {
        //https://www.jianshu.com/p/e8449ea77280
        UpdateManager.getInstance().init(this) // 获取实例并初始化,必要
            .compare(UpdateManager.COMPARE_VERSION_NAME) // 通过版本号或版本名比较,默认版本号
            .downloadUrl(StaticStr.GetUrl("/fordownload/yafopad/$appName")) // 下载地址,必要
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
            .url(StaticStr.GetUrl("/fordownload/yafopad/output.json"))
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

