package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.com.yafo.yafopda.Adapter.SnOrderAdapter

class SnOrderVM: ViewModel() {
    var code = MutableLiveData<String>()
    var ordercode = MutableLiveData<String>()
    var storname = MutableLiveData<String>()
    val custname = MutableLiveData<String>()
    val dpt = MutableLiveData<String>()
    val biz = MutableLiveData<String>()
    val provider = MutableLiveData<String>()
    val billType = MutableLiveData<String>()
    val storeCode = MutableLiveData<String>()
    val nchpk = MutableLiveData<String>()
    val chkOutTime = MutableLiveData<String>()
    val note = MutableLiveData<String>()

    var details: MutableList<SnOrderEntryVM> = mutableListOf()

    fun addDetail(detail: SnOrderEntryVM, ad: SnOrderAdapter) {
        details.add(detail)
        ad.notifyDataSetChanged()
    }
}