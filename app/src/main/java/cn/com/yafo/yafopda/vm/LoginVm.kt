package cn.com.yafo.yafopda.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginVm : ViewModel() {

    val nameOrCode= MutableLiveData<String>()

}