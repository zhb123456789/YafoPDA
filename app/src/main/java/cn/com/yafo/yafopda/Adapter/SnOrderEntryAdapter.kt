package cn.com.yafo.yafopda.Adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.com.yafo.yafopda.BR
import cn.com.yafo.yafopda.R
import cn.com.yafo.yafopda.databinding.SnOrderEntryItemBinding
import cn.com.yafo.yafopda.helper.BeeAndVibrateManager
import cn.com.yafo.yafopda.helper.Loading
import cn.com.yafo.yafopda.vm.SnOrderEntryVM

class SnOrderEntryAdapter(
    private val data: SnOrderEntryVM,
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


    override fun getCount(): Int {
        return data.snList.size
    }

    override fun getItem(position: Int): Any {
        return data.snList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val binding: SnOrderEntryItemBinding?
        //ItemListBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.sn_order_entry_item,  //子项布局ID
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
        binding!!.setVariable(BR.sn, data.snList[position])
        binding.btDel.setOnClickListener(OnDelClickListener(position))
        //https://segmentfault.com/a/1190000008246487  binding.setVariable(variableId, data.get(position));
        return binding.root
    }

    fun addSnItem(snStr:String)
    {
     if(snStr != null) {
         if (!data.snList.contains(snStr)) {
             if (data.ncChkNum.value!! > data.pdaChkNum.value!!)
             {
                 data.snList.add(snStr)
                 data.pdaChkNum.postValue(data.snList.size)

                 notifyDataSetChanged()
             }
             else
             {
                 Toast.makeText(context, "序列号数量超出", Toast.LENGTH_LONG).show()
                 BeeAndVibrateManager.playBeeAndVibrate(context,R.raw.warning ,100,null)

             }

         }
         else
         {
             Toast.makeText(context, "序列号重复", Toast.LENGTH_LONG).show()
             //提示音
             BeeAndVibrateManager.playBeeAndVibrate(context,R.raw.serialnumberduplicate ,500,null)
         }
     }
    }
    init {
        mLoading = object : Loading(context) {
            override fun cancle() {}
        }
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
                dialog.setMessage("删除此序列号？")

                // 设置“确定”按钮,使用DialogInterface.OnClickListener接口参数
                dialog.setPositiveButton(
                    "确定"
                ) { _, _ ->
                    Log.d("Dialog", "点击了“确认”按钮")
                    data.snList.remove(data.snList[position])
                    data.pdaChkNum.postValue(data.snList.size)
                    notifyDataSetChanged() //刷新数据
                }

                // 设置“取消”按钮,使用DialogInterface.OnClickListener接口参数
                dialog.setNegativeButton(
                    "取消"
                ) { _, _ -> Log.d("Dialog", "点击了“取消”按钮") }
                dialog.show()
                notifyDataSetChanged() //刷新数据
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }

}