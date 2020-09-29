package cn.com.yafo.yafopda.helper

import org.json.JSONArray
import org.json.JSONObject

class  JSONHelper {
    companion object instance {
        //移除不用 MutableLiveData 自动生成的属性 节点
        fun travelLiJSONObject(originalJSONObject: JSONObject) {

            originalJSONObject.remove("mBagOfTags")
            originalJSONObject.remove("mCleared")
            for (key in originalJSONObject.keys()) {    // 最外层的key

                val value = originalJSONObject[key]

                if (value is JSONObject) {

                    //修改JSONObject 的值为  mData
                    var v = value["mData"]
                    if (v.toString() == "{}")
                        v = ""
                    originalJSONObject.put(key, v.toString())
                    continue
                }
                if (value is JSONArray) {
                    //(json数组)")

                    if (value.length() == 0) {
                        continue
                    } else {
                        for (i in 0 until value.length()) {
                            if (value[i] is JSONObject) {
                                val o1 = value.getJSONObject(i)
                                travelLiJSONObject(o1)
                            }
                        }
                    }
                }
            }
        }
    }
}