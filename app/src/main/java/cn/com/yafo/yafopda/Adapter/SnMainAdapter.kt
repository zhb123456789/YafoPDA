package cn.com.yafo.yafopda.Adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import cn.com.yafo.yafopda.BR
import cn.com.yafo.yafopda.R
import cn.com.yafo.yafopda.databinding.SnMainItemBinding
import cn.com.yafo.yafopda.helper.CrashHandler
import cn.com.yafo.yafopda.helper.Loading
import cn.com.yafo.yafopda.vm.SnOrderVM
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SnMainAdapter(
    private val data: MutableList<SnOrderVM>,
    var context: Context
) :
    BaseAdapter() {

    // 添加Loading cancle()是按返回键，Loading框关闭的回调，可以做取消加载请求的操作。
    var mLoading: Loading
    fun LoadingShow() {
        // 显示Loading
       mLoading.show()
    }

    fun LoadingDismiss() {
        // 关闭Loading
       mLoading.dismiss()
    }

    fun addOrder(orCode: String?) {
        val order =SnOrderVM()
        LoadingShow()
        try {
            val client = OkHttpClient()
            val request = Request.Builder().get()
                .url("http://193.111.99.63/api/PDA/GetBill?billcode=$orCode")
                .build()


            var call = client.newCall(request)

            //代理 notifyDataSetChanged事件，okhttp 不能在子线程直接调用
            val handler : Handler = object : Handler(){
                override fun handleMessage(msg: Message?) {
                    super.handleMessage(msg)
                    when(msg?.what){
                        0 ->{
                            try {
                               notifyDataSetChanged()
                            } catch (ex : Throwable){
                                ex.printStackTrace()
                            }
                        }
                        else -> {
                        // 1、创建简单的AlertDialog https://www.cnblogs.com/jiayongji/p/5371996.html
                            val dialog = AlertDialog.Builder(context)

                            // (2)设置各种属性 // 注：不设置哪项属性，这个属性就默认不会显示出来
                            dialog.setTitle("服务器错误:${msg?.arg1}")
                            if (msg != null) {
                                dialog.setMessage(msg.obj.toString())
                            }

                            // (3)设置dialog可否被取消
                            dialog.setCancelable(true)

                            // (4)显示出来
                            dialog.show()
                            LoadingDismiss()
                        }
                    }
                }
            }

            //异步请求
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("UPDATE", "onFailure: $e")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if(response.code()==200) {
                        val o = JSONObject(response.body().string())
                        order.code.postValue(o.getString("billCode"))
                        order.custname.postValue(o.getString("customer"))
                        order.provider.postValue(o.getString("provider"))
                        order.dpt.postValue(o.getString("dpt"))
                        order.biz.postValue(o.getString("biz"))
                        order.storeCode.postValue(o.getString("storeCode"))
                        data.add(order )
                        handler.sendEmptyMessage(0)
                    }
                    else
                    {
                        val message = Message.obtain()
                        message.obj = response.body().string()
                        message.arg1 = response.code()
                        message.what = 1
                        handler.sendMessage(message)
                        //传递复杂类型的消息
//                        val bundleData = Bundle()
//                        bundleData.putString("Name", "Lucy")
//                        message.data = bundleData
//                        handler.sendEmptyMessage(1)
                        //处理错误
                        Log.d(CrashHandler.TAG, "OnResponse: " + response.body()?.string())
                    }
                    LoadingDismiss()
                }
            })
        }catch (e:Exception) {
            Log.e("UPDATE ERROR:", "", e)
        }
    }
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val binding: SnMainItemBinding?
        //ItemListBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.sn_main_item,  //子项布局ID
                parent, false
            )
            if (binding == null) {
                //L.e("空的binding");
            }
            // convertView = binding.getRoot();
            //  convertView.setTag(binding);
        } else {
            // binding = (SnOrderItemBinding) convertView.getTag();
            binding = DataBindingUtil.getBinding(convertView)
        }
        binding!!.setVariable(BR.order, data[position]) //BR.Orders 不要写错了
        //binding.setOrder(data.get(position)); //BR.Orders 此方法也可以
        binding.btDel.setOnClickListener(OnDelClickListener(position))
        binding.snMainItemLayout.setOnClickListener(
            OnItemClickListener(
                position
            )
        )
        //https://segmentfault.com/a/1190000008246487  binding.setVariable(variableId, data.get(position));
        return binding.root
    }

    inner class OnDelClickListener(private val position: Int) : View.OnClickListener {
        override fun onClick(view: View) {
            try {
                val bundle = Bundle()
                bundle.putInt("position", position)
                // Toast.makeText(context,position, Toast.LENGTH_LONG).show();
                //提示确认
                val dialog =
                    AlertDialog.Builder(context)
                dialog.setTitle("确认")
                // dialog.setIcon(R.drawable.dictation2_64);
                dialog.setMessage("订单数据尚未提交，确定要删除此项吗？")

                // 设置“确定”按钮,使用DialogInterface.OnClickListener接口参数
                dialog.setPositiveButton(
                    "确定"
                ) { dialog, which ->
                    Log.d("Dialog", "点击了“确认”按钮")
                    data.remove(data[position])
                    notifyDataSetChanged() //刷新数据
                }

                // 设置“取消”按钮,使用DialogInterface.OnClickListener接口参数
                dialog.setNegativeButton(
                    "取消"
                ) { dialog, which -> Log.d("Dialog", "点击了“取消”按钮") }
                dialog.show()
                notifyDataSetChanged() //刷新数据
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }

    inner class OnItemClickListener(private val position: Int) : View.OnClickListener {
        override fun onClick(view: View) {
            try {
                val bundle = Bundle()
                bundle.putInt("position", position)
                Navigation.findNavController(view)
                    .navigate(R.id.action_snMainFragment_to_snOrderFragment, bundle)

                // Toast.makeText(context,position, Toast.LENGTH_LONG).show();
                notifyDataSetChanged() //刷新数据
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }

    init {
        mLoading = object : Loading(context) {
            override fun cancle() {}
        }
    }
}