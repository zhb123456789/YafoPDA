package cn.com.yafo.yafopda.vm

import android.app.AlertDialog
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.lifecycle.ViewModel
import cn.com.yafo.yafopda.Adapter.SnMainAdapter
import cn.com.yafo.yafopda.helper.CrashHandler.TAG
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class SnMainFragmentVM ( ) : ViewModel(){
    private val client = OkHttpClient()
    var orderList : MutableList <SnOrderVM> =mutableListOf()
    //var orderList : MutableLiveData<MutableList<OrderVM>> = MutableLiveData()
    //var orderList : MutableLiveData<List<OrderVM>>= MutableLiveData()
    fun addOrder(orCode: String,ad : SnMainAdapter) {
        val order =SnOrderVM()
        ad.LoadingShow()
        try {
            val client = OkHttpClient()
            val request = Request.Builder().get()
                .url("http://193.111.99.63/api/PDA/GetBill?billcode=$orCode")
                .build()

            //代理 notifyDataSetChanged事件，okhttp 不能在子线程直接调用
            val handler : Handler = object : Handler(){
                override fun handleMessage(msg: Message?) {
                    super.handleMessage(msg)
                    ad.notifyDataSetChanged()
                }
            }

            var call = client.newCall(request)

            //异步请求
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("UPDATE", "onFailure: $e")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if(response.code()==200) {
                        val o = JSONObject(response.body().string())
                        order.code.postValue(o.getString("billCode"))
                        order.custname.postValue(o.getString("customer"))
                        order.provider.postValue(o.getString("provider"))
                        order.dpt.postValue(o.getString("dpt"))
                        order.biz.postValue(o.getString("biz"))
                        order.storeCode.postValue(o.getString("storeCode"))
                        orderList.add(order )

                        //通过代理 调用notifyDataSetChanged事件 刷新UI
                        handler.sendEmptyMessage(0) //.sendMessage(Message())
                    }
                    else
                    {

                        // 1、创建简单的AlertDialog https://www.cnblogs.com/jiayongji/p/5371996.html
                        val dialog = AlertDialog.Builder(ad.context)

                        // (2)设置各种属性 // 注：不设置哪项属性，这个属性就默认不会显示出来
                        dialog.setTitle("错误")
                        dialog.setMessage("代码："+ response.code() + " 内容："+ response.body().string())

                        // (3)设置dialog可否被取消
                        dialog.setCancelable(true)

                        // (4)显示出来
                        dialog.show()

                        //处理错误
                        Log.d(TAG, "OnResponse: " + response.body()?.string())
                    }
                    ad.LoadingDismiss()

                }
            })
        }catch (e:Exception) {
            Log.e("UPDATE ERROR:", "", e)
        }
    }
}