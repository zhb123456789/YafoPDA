package cn.com.yafo.yafopda

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}
