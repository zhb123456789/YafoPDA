package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SnOrderDetailVM  {
    var invcode= MutableLiveData<String>()
    var invname= MutableLiveData<String>()
    var invclass= MutableLiveData<String>()
    var should_in_num= MutableLiveData<Int>()
    var should_out_num= MutableLiveData<Int>()
    var checked_num= MutableLiveData<Int>()
    var checked_sn : MutableList<String> = mutableListOf()
}