package cn.com.yafo.yafopda.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Toast


class BarCodeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        var scanResult_1=intent.getStringExtra("SCAN_BARCODE1");
        var scanResult_2=intent.getStringExtra("SCAN_BARCODE2");
        var barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1); // -1:unknown
        var msg=""
        //获取结果值：
        var scanStatus=intent.getStringExtra("SCAN_STATE");
        if("ok".equals(scanStatus)){
            msg="静态广播：" + intent.getStringExtra("SCAN_BARCODE1") +" "+ intent.getStringExtra("SCAN_BARCODE2")
        }else{
            msg= "获取失败:$scanStatus"
        }

        val t =
            Toast.makeText(context, msg, Toast.LENGTH_LONG)
        t.setGravity(Gravity.TOP, 0, 0)
        t.show()
    }
}

interface  IBarCodeChangedListener{
    fun onBarCodeChanged(newText:String)
}