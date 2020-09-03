package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.com.yafo.yafopda.Adapter.OrderDetailsAdapter

class SnOrderVM: ViewModel() {
    var code = MutableLiveData<String>()
    var ordercode = MutableLiveData<String>()
    var storcode = MutableLiveData<String>()
    var storname = MutableLiveData<String>()
    val custcode = MutableLiveData<String>()
    val custname = MutableLiveData<String>()

    var details: MutableList<SnOrderDetailVM> = mutableListOf()

    fun addDetail(detail: SnOrderDetailVM, ad: OrderDetailsAdapter) {
        details.add(detail)
        ad.notifyDataSetChanged()
    }
}