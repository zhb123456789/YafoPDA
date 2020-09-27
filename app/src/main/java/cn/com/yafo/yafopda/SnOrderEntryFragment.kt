package cn.com.yafo.yafopda

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.com.yafo.yafopda.Adapter.SnOrderAdapter
import cn.com.yafo.yafopda.Adapter.SnOrderEntryAdapter
import cn.com.yafo.yafopda.databinding.SnOrderEntryFragmentBinding
import cn.com.yafo.yafopda.databinding.SnOrderFragmentBinding
import cn.com.yafo.yafopda.vm.SnMainFragmentVM
import cn.com.yafo.yafopda.vm.SnOrderEntryVM
import cn.com.yafo.yafopda.vm.SnOrderVM
import kotlinx.android.synthetic.main.one_input_dialog.view.*
import kotlinx.android.synthetic.main.sn_order_entry_fragment.*
import org.jetbrains.anko.support.v4.runOnUiThread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val positionOrder = "positionOrder"
private const val positionEntry = "positionEntry"
/**
 * A simple [Fragment] subclass.
 * Use the [sn_order_info_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SnOrderEntryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var poOrder: Int? = null
    private var poOrderEntry: Int? = null
    lateinit var adapter: SnOrderEntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            poOrder = it.getInt(positionOrder)
            poOrderEntry = it.getInt(positionEntry)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var vm= ViewModelProvider(this.requireActivity()).get(SnMainFragmentVM::class.java) // 关键代码
        var mBinding: SnOrderEntryFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sn_order_entry_fragment,container,false)
        var orderEntry =vm.orderList[this!!.poOrder!!].orderEntrys[this!!.poOrderEntry!!]
        var orderCode =vm.orderList[this!!.poOrder!!].code
        mBinding.orderEntry=orderEntry
        mBinding.orderCode=orderCode.value

                //使用 MutableLiveData<String>() 单个属性
        orderEntry.checkedNum.observe(viewLifecycleOwner, Observer {
            // Update the UI when the data has changed
            runOnUiThread {
                remainNum.text=orderEntry.remainNum.value.toString()
                if(orderEntry.remainNum.value==0)
                    addSn.visibility= View.GONE
                else
                    addSn.visibility= View.VISIBLE
            }
        })

        mBinding.addSn.setOnClickListener(OnClickListener(orderEntry.invname.value.toString()))

        //定制Adapter 绑定List
        //orderEntry.snList.add( "123")
        adapter = SnOrderEntryAdapter( orderEntry, requireContext())
        mBinding.adapter=adapter
        return mBinding.root;
    }
    //接收扫码数据
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //获取结果值：
            var scanStatus= intent?.getStringExtra("SCAN_STATE");
            if("ok" == scanStatus){
                intent?.let {
                    adapter.addSnItem(intent.getStringExtra("SCAN_BARCODE1"))
                    adapter.notifyDataSetChanged()
                }
            }else{
                val t =
                    Toast.makeText(context, "获取失败:$scanStatus", Toast.LENGTH_LONG)
                t.setGravity(Gravity.TOP, 0, 0)
                t.show()
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

    inner class OnClickListener( var invname :String) : View.OnClickListener {
        override fun onClick(view: View) {
            try {

                var builder = AlertDialog.Builder(context)
                // 设置“确定”按钮,使用DialogInterface.OnClickListener接口参数
                var dialogView = LayoutInflater.from(context)
                    .inflate(R.layout.one_input_dialog, null);
                builder.setPositiveButton(
                    "确定"
                ) { _, _ ->
                    adapter.addSnItem(dialogView.edit_text.text.toString())
                }
                val dialog = builder.create()

                dialog.setTitle("请输入SN");
                dialog.setView(dialogView);
                dialog.setMessage(invname)

                dialogView.edit_text.setOnKeyListener { _, keyCode, event ->
                    if (KeyEvent.KEYCODE_ENTER == keyCode && event.action == KeyEvent.ACTION_DOWN) {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
                        true
                    }
                    false
                }
                dialog.show()
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}
