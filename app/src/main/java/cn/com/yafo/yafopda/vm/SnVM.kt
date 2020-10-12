package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SnVM: ViewModel() {
    var SN = MutableLiveData<String>()
    var InvCode = MutableLiveData<String>()
    var InvName = MutableLiveData<String>()
    var OrderCode = MutableLiveData<String>()
}