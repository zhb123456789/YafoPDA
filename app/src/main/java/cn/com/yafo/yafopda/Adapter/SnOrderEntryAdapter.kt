package cn.com.yafo.yafopda.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import cn.com.yafo.yafopda.BR
import cn.com.yafo.yafopda.R
import cn.com.yafo.yafopda.databinding.SnOrderEntryItemBinding
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
        binding.snOrderEntryItemLayout .setOnClickListener(OnItemClickListener(position))
        //https://segmentfault.com/a/1190000008246487  binding.setVariable(variableId, data.get(position));
        return binding.root
    }

    init {
        mLoading = object : Loading(context) {
            override fun cancle() {}
        }
    }

    inner class OnItemClickListener(private val position: Int) : View.OnClickListener {
        override fun onClick(view: View) {
            try {
                val bundle = Bundle()
                bundle.putInt("position", position)
                Navigation.findNavController(view)
                    .navigate(R.id.action_sn_order_fragment_to_sn_order_entry_fragment, bundle)

                // Toast.makeText(context,position, Toast.LENGTH_LONG).show();
                notifyDataSetChanged() //刷新数据

            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }

}