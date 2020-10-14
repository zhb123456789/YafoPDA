package cn.com.yafo.yafopda.helper

import cn.com.yafo.yafopda.vm.MainViewModel

class GlobalVar {
    companion object {
        lateinit var userVM: MainViewModel
//        var BaseUrl = "http://192.168.4.18:56580/"
        var BaseUrl = "http://193.111.99.63/"
        fun  GetUrl(url:String): String {
            if(url.startsWith("/"))
                url.removeRange(0,1)
            return BaseUrl+url
        }
    }
}