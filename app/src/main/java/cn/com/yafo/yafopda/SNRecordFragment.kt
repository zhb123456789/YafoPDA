package cn.com.yafo.yafopda

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.com.yafo.yafopda.helper.ClientBuilder
import cn.com.yafo.yafopda.helper.GlobalVar
import cn.com.yafo.yafopda.helper.Loading
import kotlinx.android.synthetic.main.operation_fragment.textViewOrder
import kotlinx.android.synthetic.main.sn_record_fragment.*
import kotlinx.android.synthetic.main.sn_record_fragment.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SNRecordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SNRecordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var provider: String
    lateinit var note: String
    lateinit var mLoading: Loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private val handler : Handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what)
            {
                200 ->{
                    try {
                        textViewOrder.text=textViewOrder.text.toString() +"\n"+ msg.obj.toString()
                        Toast.makeText(context, "提交成功", Toast.LENGTH_LONG).show()
                        mLoading.dismiss()

                    } catch (ex : Throwable){
                        Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()
                        ex.printStackTrace()
                    }
                }

                500 ->
                {
                    val dialog = AlertDialog.Builder(context)
                    dialog.setTitle("错误:${msg?.what}")
                    if (msg != null) {
                        val o = JSONObject(msg.obj.toString())

                        dialog.setMessage(o.getString("exceptionMessage"))
                    }
                    dialog.setCancelable(true)
                    dialog.show()
                    mLoading.dismiss()
                }
                else -> {
                    // 1、创建简单的AlertDialog https://www.cnblogs.com/jiayongji/p/5371996.html
                    val dialog = AlertDialog.Builder(context)

                    // (2)设置各种属性 // 注：不设置哪项属性，这个属性就默认不会显示出来
                    dialog.setTitle("服务器错误:${msg?.what}")
                    if (msg != null) {
                        dialog.setMessage(msg.obj.toString())
                    }

                    // (3)设置dialog可否被取消
                    dialog.setCancelable(true)

                    // (4)显示出来
                    dialog.show()
                    mLoading.dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mLoading = object : Loading(context) {
            override fun cancle() {}
        }

        // view = inflater.inflate(R.layout.sn_record_fragment, container, false)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sn_record_fragment, container, false)
    }

    //接收扫码数据
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //获取结果值：
            var scanStatus= intent?.getStringExtra("SCAN_STATE");
            if("ok" == scanStatus){
                intent?.let {
                    submitSNRecord( intent.getStringExtra("SCAN_BARCODE1"))
                }
            }else{
                val t =
                    Toast.makeText(context, "获取失败:$scanStatus", Toast.LENGTH_LONG)
                t.setGravity(Gravity.TOP, 0, 0)
                t.show()
            }

        }
    }
    fun submitSNRecord(sn:String)
    {

        provider= view?.txt_provider?.text.toString()
        note= view?.txt_note?.text.toString()

        if (provider.isEmpty())
        { val t =
            Toast.makeText(context, "请先填写供应商", Toast.LENGTH_LONG)
            t.setGravity(Gravity.TOP, 0, 0)
            t.show()
        }
        else {
            mLoading.show()
            var opType = "AddSNRecord"

            try {
                val client = ClientBuilder.plainClient
                val request = Request.Builder().get()
                    .url(GlobalVar.GetUrl("/api/PDA/$opType?sn=$sn&user=${GlobalVar.userVM.username.value}&provider=$provider&note=$note"))
                    .build()


                var call = client.newCall(request)
                //异步请求
                call.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("异步请求失败", "onFailure: $e")
                        val message = Message.obtain()
                        message.obj = e
                        message.what = 501
                        handler.sendMessage(message)
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        if (response.code() == 200) {
                            val message = Message.obtain()
                            message.obj = sn
                            message.what = 200
                            handler.sendMessage(message)

                        } else {
                            val message = Message.obtain()
                            message.obj = response.body().string()
                            message.what = response.code()
                            handler.sendMessage(message)
                            Log.d("TAG", "OnResponse: " + response.body()?.string())
                        }

                    }
                })
            } catch (e: Exception) {
                val message = Message.obtain()
                message.obj = e
                message.what = 501
                handler.sendMessage(message)
                Log.e("ERROR:", "", e)

            }
        }
    }
    override fun onPause() {
        requireActivity().unregisterReceiver(receiver)
        super.onPause()
    }
    override fun onResume() {
        super.onResume()

        // 注册广播：
        requireActivity().registerReceiver(receiver,  IntentFilter("nlscan.action.SCANNER_RESULT"));
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SNRecordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SNRecordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
