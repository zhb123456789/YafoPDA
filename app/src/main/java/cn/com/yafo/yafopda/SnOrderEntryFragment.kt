package cn.com.yafo.yafopda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.com.yafo.yafopda.Adapter.SnOrderAdapter
import cn.com.yafo.yafopda.databinding.SnOrderFragmentBinding
import cn.com.yafo.yafopda.vm.SnMainFragmentVM
import cn.com.yafo.yafopda.vm.SnOrderVM

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val position = "position"
/**
 * A simple [Fragment] subclass.
 * Use the [sn_order_info_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SnOrderEntryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var po: Int? = null

    var list : MutableList <SnOrderVM> =mutableListOf()
    var vm:SnMainFragmentVM = SnMainFragmentVM()
    lateinit var adapter: SnOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            po = it.getInt(position)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        vm= ViewModelProvider(this.requireActivity()).get(SnMainFragmentVM::class.java) // 关键代码
        var mBinding: SnOrderFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sn_order_fragment,container,false)
        var order =vm.orderList[this!!.po!!]
        mBinding.order=order
        //定制Adapter 绑定List
        adapter = SnOrderAdapter( order.orderEntrys, context)
        mBinding.adapter=adapter



        return mBinding.root;
    }

}
