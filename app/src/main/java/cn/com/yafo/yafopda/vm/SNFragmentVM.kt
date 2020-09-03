package cn.com.yafo.yafopda.vm

import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.com.yafo.yafopda.Adapter.OrdersAdapter
import cn.com.yafo.yafopda.helper.CrashHandler.TAG
import cn.com.yafo.yafopda.helper.Loading
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class SNFragmentVM ( ) : ViewModel(){
    private val client = OkHttpClient()
    var orderList : MutableList <SnOrderVM> =mutableListOf()
    //var orderList : MutableLiveData<MutableList<OrderVM>> = MutableLiveData()
    fun addOrder(orCode: String,ad : OrdersAdapter) {
        val order =SnOrderVM()
        ad.LoadingShow()
        try {
            val client = OkHttpClient()
            val request = Request.Builder().get()
                .url("http://193.111.99.63/api/PDA/GetBill?billcode=CR2007160034")
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
                        order.custname.postValue(o.getString("provider"))
                        orderList.add(order )

                        //通过代理 调用notifyDataSetChanged事件 刷新UI
                        handler.sendEmptyMessage(0) //.sendMessage(Message())
                    }
                    else
                    {
                        //  Toast.makeText("","位置提交失败",Toast.LENGTH_SHORT).show()

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