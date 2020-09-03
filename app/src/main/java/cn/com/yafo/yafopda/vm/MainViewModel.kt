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
    val username= ObservableField<String>()
    val password= MutableLiveData<String>()
    val user= MutableLiveData<User>()

    private val u2 = User2()

    fun getu2(): User2? {
        return u2
    }
    fun btnBoxOnClick()
    {
        username.set("1211134")
        password.value=username.get()+password.value
        user.value = User("111","222")
    }


}
