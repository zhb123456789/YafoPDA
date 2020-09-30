package cn.com.yafo.yafopda.vm

import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.com.yafo.yafopda.helper.GlobalVar
import cn.com.yafo.yafopda.helper.JSONHelper
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
    var compelCommit =false
    var isCheckOver=false
        get() {
            //检查是否验货完成
            for (item in orderEntrys)
            {
                if (item.remainNum.value!! >0)
                {
                    return  false
                }
            }
            return true
        }

    var orderEntrys: MutableList<SnOrderEntryVM> = mutableListOf()

    fun addorderEntry(orderEntry: SnOrderEntryVM) {
        orderEntrys.add(orderEntry)
    }

}
