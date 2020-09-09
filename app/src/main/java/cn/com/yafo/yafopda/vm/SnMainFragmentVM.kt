package cn.com.yafo.yafopda.vm


import androidx.lifecycle.ViewModel



class SnMainFragmentVM ( ) : ViewModel(){
    var orderList : MutableList <SnOrderVM> =mutableListOf()
    //var orderList : MutableLiveData<MutableList<OrderVM>> = MutableLiveData()
    //var orderList : MutableLiveData<List<OrderVM>>= MutableLiveData()
}