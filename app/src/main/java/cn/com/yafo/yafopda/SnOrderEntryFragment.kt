package cn.com.yafo.yafopda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.com.yafo.yafopda.Adapter.SnOrderAdapter
import cn.com.yafo.yafopda.Adapter.SnOrderEntryAdapter
import cn.com.yafo.yafopda.databinding.SnOrderEntryFragmentBinding
import cn.com.yafo.yafopda.databinding.SnOrderFragmentBinding
import cn.com.yafo.yafopda.vm.SnMainFragmentVM
import cn.com.yafo.yafopda.vm.SnOrderEntryVM
import cn.com.yafo.yafopda.vm.SnOrderVM

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
        mBinding.orderEntry=orderEntry
        //定制Adapter 绑定List
        //orderEntry.snList.add( "123")
        var adapter: SnOrderEntryAdapter = SnOrderEntryAdapter( orderEntry,  requireContext())
        mBinding.adapter=adapter
        return mBinding.root;
    }

}
