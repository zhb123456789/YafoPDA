package cn.com.yafo.yafopda.Adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.Selection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import cn.com.yafo.yafopda.R
import cn.com.yafo.yafopda.databinding.SnOrderItemBinding
import cn.com.yafo.yafopda.helper.BeeAndVibrateManager
import cn.com.yafo.yafopda.vm.SnOrderEntryVM
import kotlinx.android.synthetic.main.one_input_dialog.view.*

class SnOrderAdapter( private val data: MutableList<SnOrderEntryVM>, val po: Int,
                      var context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return data!!.size
    }

    override fun getItem(position: Int): Any? {
        return data!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        val binding: SnOrderItemBinding?
        //ItemListBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.sn_order_item,  //子项布局ID
                parent, false
            )
            if (binding == null) {
                //L.e("空的binding");
            }
        } else {
            binding = DataBindingUtil.getBinding(convertView)
        }
        //binding.setVariable(BR.orderitem, data.get(position)); //BR 不要写错了
        binding!!.orderitem = data!![position] //BR. 此方法也可以
        //binding.button4.setOnClickListener(new OnBtnClickListener( position));
        binding.snOrderItemLayout.setOnClickListener(OnItemClickListener(position))
        return binding.root
    }

    inner class OnItemClickListener(private val position: Int) : View.OnClickListener {
        override fun onClick(view: View) {
            try {
                val bundle = Bundle()
                bundle.putInt("positionOrder", po)
                bundle.putInt("positionEntry", position)
                if (data!![position].invclass.value=="打印机") {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_sn_order_fragment_to_sn_order_entry_fragment, bundle)
                }
                else
                {
                    Toast.makeText(context, "只有打印机才可以录入SN", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun checkInvNumByBarcode(barcode: String) {

        var invFind=false

        for (i in data!!.indices) {
            val vm = data!![i]
            if (vm.barCode.value.toString() == barcode ) {
                invFind=true
                if(vm.remainNum.value!! <=0)
                {
                    Toast.makeText(context, "该存货已经检查完，剩余数量0", Toast.LENGTH_LONG).show()
                    BeeAndVibrateManager.playBeeAndVibrate(context,R.raw.warning ,200,null)
                    break;
                }
                val dialog =  AlertDialog.Builder(context)
                var  dialogView = LayoutInflater.from(context)
                    .inflate(R.layout.one_input_dialog,null);
                dialogView.edit_text.inputType = InputType.TYPE_CLASS_NUMBER;
                dialog.setTitle("请输入数量");
                dialog.setView(dialogView);
                // dialog.setIcon(R.drawable.dictation2_64);
                dialog.setMessage(vm.invname.value)
                dialogView.edit_text.setText("1")
                Selection.selectAll( dialogView.edit_text.text);


                // 设置“确定”按钮,使用DialogInterface.OnClickListener接口参数
                dialog.setPositiveButton(
                    "确定"
                ) { _, _ ->
                    Log.d("Dialog", "点击了“确认”按钮")
                    var num = dialogView.edit_text.text.toString().toInt()
                    var checknum =vm.checkedNum.value
                    if (checknum == null) {
                        checknum =0
                    }
                    checknum += num
                    if(checknum <= vm.ncChkNum.value!!) {
                        vm.checkedNum.value = checknum
                    }
                    else
                    {
                        Toast.makeText(context, "应捡数量超出！", Toast.LENGTH_LONG).show()
                        BeeAndVibrateManager.playBeeAndVibrate(context,R.raw.warning ,1000,null)
                    }
                    notifyDataSetChanged() //刷新数据
                }


                dialog.show()
                break
            }
        }
        if (!invFind)
        {
            Toast.makeText(context, "无此条码", Toast.LENGTH_LONG).show()
            BeeAndVibrateManager.playBeeAndVibrate(context,R.raw.notexists ,500,null)
        }
    }
}