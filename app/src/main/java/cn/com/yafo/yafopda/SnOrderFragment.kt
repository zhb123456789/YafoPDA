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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cn.com.yafo.yafopda.Adapter.SnOrderAdapter
import cn.com.yafo.yafopda.databinding.SnOrderFragmentBinding
import cn.com.yafo.yafopda.helper.GlobalVar
import cn.com.yafo.yafopda.helper.JSONHelper
import cn.com.yafo.yafopda.helper.Loading
import cn.com.yafo.yafopda.vm.SnMainFragmentVM
import cn.com.yafo.yafopda.vm.SnOrderVM
import com.google.gson.Gson
import okhttp3.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val position = "position"
/**
 * A simple [Fragment] subclass.
 * Use the [sn_order_info_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SnOrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var po: Int? = null
    lateinit var adapter:SnOrderAdapter
    lateinit var vm:SnMainFragmentVM
    lateinit var mLoading: Loading
    lateinit var order:SnOrderVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            po = it.getInt(position)
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
                        po?.let { vm.orderList.removeAt(it) }
                        Toast.makeText(context, "提交成功", Toast.LENGTH_LONG).show()
                        mLoading.dismiss()
                        view?.let {
                            Navigation.findNavController(it)
                                .navigate(R.id.action_snOrderFragment_to_snMainFragment, null)
                        }
                    } catch (ex : Throwable){
                        Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()
                        ex.printStackTrace()
                    }
                }
                400->
                {
                    //提示确认
                    val dialog =
                        AlertDialog.Builder(context)
                    dialog.setTitle("覆盖确认")
                    // dialog.setIcon(R.drawable.dictation2_64);
                    dialog.setMessage("该订单已经存在，是否覆盖？")

                    // 设置“确定”按钮,使用DialogInterface.OnClickListener接口参数
                    dialog.setPositiveButton(
                        "确定"
                    ) { _, _ ->
                        mLoading.show()
                        order.compelCommit=true
                        submitOrder(order)
                        Log.d("Dialog", "点击了“确认”按钮")
                    }

                    // 设置“取消”按钮,使用DialogInterface.OnClickListener接口参数
                    dialog.setNegativeButton(
                        "取消"
                    ) { _, _ -> mLoading.dismiss()
                        Log.d("Dialog", "点击了“取消”按钮") }
                    dialog.show()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        vm= ViewModelProvider(this.requireActivity()).get(SnMainFragmentVM::class.java) // 关键代码
        var mBinding: SnOrderFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sn_order_fragment,container,false)
        order =vm.orderList[this!!.po!!]
        mBinding.order=order
        //定制Adapter 绑定List
        adapter = SnOrderAdapter( order.billEntrys,this!!.po!!, requireContext())
        mBinding.adapter=adapter

        mLoading = object : Loading(context) {
            override fun cancle() {}
        }
        mBinding.buttonSubmit.onClick {

            if (order.isCheckOver) {
                mLoading.show()
                submitOrder(order)
            }
            else
            {
                //提示确认
                val dialog =
                    AlertDialog.Builder(context)
                dialog.setTitle("确认")
                // dialog.setIcon(R.drawable.dictation2_64);
                dialog.setMessage("该订单没有完成验货，是否提交？")

                // 设置“确定”按钮,使用DialogInterface.OnClickListener接口参数
                dialog.setPositiveButton(
                    "确定"
                ) { _, _ ->
                    mLoading.show()
                    submitOrder(order)
                    Log.d("Dialog", "点击了“确认”按钮")
                }

                // 设置“取消”按钮,使用DialogInterface.OnClickListener接口参数
                dialog.setNegativeButton(
                    "取消"
                ) { _, _ -> Log.d("Dialog", "点击了“取消”按钮") }
                dialog.show()
            }
        }

        return mBinding.root;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment sn_order_info_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
            SnOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(position, param1)
                }
            }
    }
    //检查单据是否已经提交过

    //接收扫码数据
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //获取结果值：
            var scanStatus= intent?.getStringExtra("SCAN_STATE");
            if("ok" == scanStatus){
                intent?.let {
                    adapter.checkInvNumByBarcode(intent.getStringExtra("SCAN_BARCODE1"))
                }
            }else{
                val t =
                    Toast.makeText(context, "获取失败:$scanStatus", Toast.LENGTH_LONG)
                t.setGravity(Gravity.TOP, 0, 0)
                t.show()
            }

        }
    }

    fun submitOrder(order:SnOrderVM)
    {
        val client = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")


        var json: String = Gson().toJson(order)
        val jsonObject = JSONObject(json)

        JSONHelper.travelLiJSONObject(jsonObject)

        json =jsonObject.toString()


        val requestBody = RequestBody.create(JSON, json)


        var builder = Request.Builder()
        builder.url(GlobalVar.GetUrl("/api/PDA/AddCheckBill?forceCommit=${order.compelCommit}"))
        builder.addHeader("Content-Type","application/json")
            .post(requestBody)

        client.newCall(builder.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("submitOrder", "onFailure: $e")
                val message = Message.obtain()
                message.obj = e
                message.what = 501
                handler.sendMessage(message)
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.code()==200) {
                    handler.sendEmptyMessage(200)
                } else
                {
                    val message = Message.obtain()
                    message.obj = response.body().string()
                    message.what = response.code()
                    handler.sendMessage(message)
                    //传递复杂类型的消息
//                        val bundleData = Bundle()
//                        bundleData.putString("Name", "Lucy")
//                        message.data = bundleData
//                        handler.sendEmptyMessage(1)
                    //处理错误
                    Log.d("TAG", "OnResponse: " + response.body()?.string())
                }

            }
        })

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
}
