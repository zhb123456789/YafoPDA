package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SnOrderEntryVM : ViewModel() {
    private val _remainNum=MutableLiveData<Int>()
    var invCode= MutableLiveData<String>()
    var invName= MutableLiveData<String>()
    var invClass= MutableLiveData<String>()
    var mainClass= MutableLiveData<String>()
    var barCode= MutableLiveData<String>()
    var ncChkNum= MutableLiveData<Int>()
    var checkedNum= MutableLiveData<Int>()
    //剩余数量
    val  remainNum :MutableLiveData<Int>
        get() {
            _remainNum.value= ncChkNum.value!! - checkedNum.value!!
            return  _remainNum
        }


    var snList : MutableList<String> = mutableListOf()

}