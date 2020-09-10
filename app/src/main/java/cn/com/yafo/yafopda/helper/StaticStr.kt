package cn.com.yafo.yafopda.helper

class StaticStr {
    companion object {
        var BaseUrl = "http://193.111.99.63/"
        fun  GetUrl(url:String): String {
            if(url.startsWith("/"))
                url.removeRange(0,1)
            return BaseUrl+url
        }
    }
}