package cn.com.yafo .yafopda.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class User(var name:String ,pwd :String)  {


}

class User2:LiveData<User2>()
{
    private var tag1 = 0
    private var tag2 = 0

    fun getTag1(): Int {
        return tag1
    }

    fun setTag1(tag1: Int) {
        this.tag1 = tag1
        postValue(this)
    }

    fun getTag2(): Int {
        return tag2
    }

    fun setTag2(tag2: Int) {
        this.tag2 = tag2
        postValue(this)
    }
}

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val username= MutableLiveData<String>()
    val password= MutableLiveData<String>()


    fun btnBoxOnClick()
    {
        username.value=("1211134")
        password.value=username.value+password.value
    }


}
