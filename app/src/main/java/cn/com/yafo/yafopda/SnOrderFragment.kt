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
class SnOrderFragment : Fragment() {
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

//
//        var item = SnOrderEntryVM()
//        item.invcode.value="111"
//        item.invname.value="vvvv"
//        order.addDetail(item,adapter)

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
}
