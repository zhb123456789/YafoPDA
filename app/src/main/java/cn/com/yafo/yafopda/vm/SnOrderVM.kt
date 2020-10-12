package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SnOrderVM: ViewModel() {

    var billCode = MutableLiveData<String>()
    var billDate = MutableLiveData<String>()
    val billType = MutableLiveData<String>()
    var storName = MutableLiveData<String>()
    val custName = MutableLiveData<String>()
    val dpt = MutableLiveData<String>()
    val nchpk = MutableLiveData<String>()
    val biz = MutableLiveData<String>()
    val provider = MutableLiveData<String>()
    val storeCode = MutableLiveData<String>()
    val chkOutTime = MutableLiveData<String>()
    val note = MutableLiveData<String>()
    var compelCommit =false
    var isCheckOver=false
        get() {
            //检查是否验货完成
            for (item in billEntrys)
            {
                if (item.remainNum.value!! >0)
                {
                    return  false
                }
            }
            return true
        }

    var billEntrys: MutableList<SnOrderEntryVM> = mutableListOf()

    fun addBillEntry(orderEntry: SnOrderEntryVM) {
        billEntrys.add(orderEntry)
    }

}
