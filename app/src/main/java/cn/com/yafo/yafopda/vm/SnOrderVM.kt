package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.com.yafo.yafopda.helper.GlobalVar
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class SnOrderVM: ViewModel() {
    var code = MutableLiveData<String>()
    var orderCode = MutableLiveData<String>()
    var storName = MutableLiveData<String>()
    val custName = MutableLiveData<String>()
    val dpt = MutableLiveData<String>()
    val biz = MutableLiveData<String>()
    val provider = MutableLiveData<String>()
    val billType = MutableLiveData<String>()
    val storeCode = MutableLiveData<String>()
    val nchpk = MutableLiveData<String>()
    val chkOutTime = MutableLiveData<String>()
    val note = MutableLiveData<String>()

    var orderEntrys: MutableList<SnOrderEntryVM> = mutableListOf()

    fun addorderEntry(orderEntry: SnOrderEntryVM) {
        orderEntrys.add(orderEntry)
    }
    fun submitOrder()
    {
        val client = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")


        val json: String = Gson().toJson(Gson().toJson(this))

        val requestBody = RequestBody.create(JSON, json.toString())

        var builder = Request.Builder()
        builder.url(GlobalVar.GetUrl("/api/PDA/GetBill?billcode=$"))
        builder.addHeader("Content-Type","application/json")
            .post(requestBody)

        client.newCall(builder.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("————失败了$e")
            }
            override fun onResponse(call: Call, response: Response) {
                var stA = response.body()!!.string()
                println("————成功 $stA")

            }
        })

    }
}