package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.com.yafo.yafopda.helper.GlobalVar
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class SnOrderVM: ViewModel() {

    var billCode = MutableLiveData<String>()
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


        var json: String = Gson().toJson(this)
        val jsonObject = JSONObject(json)
        //jsonObject.remove("mActiveCount")
        //jsonObject.keySet().removeIf({ k -> !k.equals("a") })

        travelLiJSONObject(jsonObject)

        json =jsonObject.toString()


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
    fun travelLiJSONObject(originalJSONObject: JSONObject) {

        originalJSONObject.remove("mBagOfTags")
        originalJSONObject.remove("mCleared")
        for (key in originalJSONObject.keys()) {    // 最外层的key

            val value = originalJSONObject[key]

            if (value is JSONObject) {
//                value.remove("mActiveCount")
//                value.remove("mDataLock")
//                value.remove("mDispatchInvalidated")
//                value.remove("mDispatchingValue")
//                value.remove("mObservers")
//                value.remove("mPendingData")
//                value.remove("mVersion")
                //修改JSONObject 的值为  mData
                var v= value["mData"]
                if(v.toString()=="{}")
                    v=""
                originalJSONObject.put(key, v.toString())
                continue
            }
            if (value is JSONArray) {
                //(json数组)")

                if (value.length()==0) {
                    continue
                } else {
                    for (i in 0 until value.length()) {
                        if(value[i] is JSONObject) {
                            val o1 = value.getJSONObject(i)
                            travelLiJSONObject(o1)
                        }
                    }
                }
            }
        }
    }


}
class SnOrderVo(){
    var billCode:String=""
    var storName :String=""
    var custName:String=""
    var dpt :String=""
    var biz :String=""
    var provider :String=""
    var billType :String=""
    var storeCode :String=""
    var nchpk :String=""
    var chkOutTime :String=""
    var note :String=""

}