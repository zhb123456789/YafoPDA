package cn.com.yafo.yafopda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cn.com.yafo.yafopda.Adapter.OrderDetailsAdapter
import cn.com.yafo.yafopda.Adapter.OrdersAdapter
import cn.com.yafo.yafopda.databinding.SnFragmentBinding
import cn.com.yafo.yafopda.databinding.SnOrderInfoFragmentBinding
import cn.com.yafo.yafopda.vm.SNFragmentVM
import cn.com.yafo.yafopda.vm.SnOrderDetailVM
import cn.com.yafo.yafopda.vm.SnOrderVM

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val position = "position"
/**
 * A simple [Fragment] subclass.
 * Use the [sn_order_info_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class sn_order_info_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var po: Int? = null

    var list : MutableList <SnOrderVM> =mutableListOf()
    var vm:SNFragmentVM = SNFragmentVM()
    lateinit var adapter: OrderDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            po = it.getInt(position)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        vm= ViewModelProvider(this.requireActivity()).get(SNFragmentVM::class.java) // 关键代码
        var mBinding: SnOrderInfoFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sn_order_info_fragment,container,false)
        var order =vm.orderList[this!!.po!!]
        mBinding.order=order
        //定制Adapter 绑定List
        adapter = OrderDetailsAdapter( order.details, context)
        mBinding.adapter=adapter


        var item = SnOrderDetailVM()
        item.invcode.value="111"
        item.invname.value="vvvv"
        order.addDetail(item,adapter)



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
                sn_order_info_fragment().apply {
                    arguments = Bundle().apply {
                        putString(position, param1)
                    }
                }
    }
}
