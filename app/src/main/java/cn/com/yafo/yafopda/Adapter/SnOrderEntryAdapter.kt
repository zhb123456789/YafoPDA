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
import cn.com.yafo.yafopda.vm.SnOrderEntryVM
import cn.com.yafo.yafopda.vm.SnOrderVM
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class SnOrderEntryAdapter(
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

        //https://segmentfault.com/a/1190000008246487  binding.setVariable(variableId, data.get(position));
        return binding.root
    }

    init {
        mLoading = object : Loading(context) {
            override fun cancle() {}
        }
    }

}