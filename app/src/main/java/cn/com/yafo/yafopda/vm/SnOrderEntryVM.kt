package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SnOrderEntryVM : ViewModel() {
    var invcode= MutableLiveData<String>()
    var invname= MutableLiveData<String>()
    var invclass= MutableLiveData<String>()
    var ncChkNum= MutableLiveData<Int>()
    var checked_num= MutableLiveData<Int>()
    var snList : MutableList<String> = mutableListOf()
}