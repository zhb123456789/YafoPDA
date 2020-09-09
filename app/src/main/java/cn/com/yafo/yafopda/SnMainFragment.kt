package cn.com.yafo.yafopda

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cn.com.yafo.yafopda.Adapter.SnMainAdapter
import cn.com.yafo.yafopda.databinding.SnMainFragmentBinding
import cn.com.yafo.yafopda.vm.SnOrderVM
import cn.com.yafo.yafopda.vm.SnMainFragmentVM


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [SNFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SnMainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var list : MutableList <SnOrderVM> =mutableListOf()
    var vm:SnMainFragmentVM = SnMainFragmentVM()
    lateinit var adapter: SnMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {


        // Inflate the layout for this fragment
        //var viewModel = ViewModelProvider(this.requireActivity()).get(MainViewModel::class.java) // 关键代码

        vm=ViewModelProvider(this.requireActivity()).get(SnMainFragmentVM::class.java) // 关键代码

        var mBinding: SnMainFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sn_main_fragment,container,false)
        mBinding.vm=vm
        mBinding.lifecycleOwner=this.requireActivity()



//定制Adapter 绑定List
        adapter = SnMainAdapter( vm.orderList, requireContext())
        mBinding.adapter=adapter

//绑定List 结束

//通用Adapter 绑定List（需要引用其他包）  Android kotlin DataBinding 之 unresolved reference: BR 坑 https://blog.csdn.net/weixin_40929353/article/details/102911137
//        val adapter: ListAdapter<OrderVM> = ListAdapter(context, list, R.layout.order_item, BR.Orders ) // 必须加 import androidx.databinding.library.baseAdapters.BR 或者直接用ID 数值
//        mBinding.adapter=adapter
//        val order =OrderVM()
//        order.code.value="111code"
//        list.add(order )
//绑定List 结束
        mBinding.lifecycleOwner = this.requireActivity()

        //返回按钮
        mBinding.button.setOnClickListener(View.OnClickListener { v ->
            val controller = Navigation.findNavController(v)
            controller.navigate(R.id.action_snMainFragment_to_mainFragment)
        })
        //加号按钮
        mBinding.btnAddOrder.setOnClickListener(View.OnClickListener {
            adapter.addOrder(mBinding.eidtLastOrCode.text.toString())
        })
        //输入框回车事件
        mBinding.eidtLastOrCode.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mBinding.btnAddOrder.callOnClick()
            }
            false
        }

        return mBinding.root;
    }

    //接收扫码数据
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //获取结果值：
            var scanStatus= intent?.getStringExtra("SCAN_STATE");
            if("ok" == scanStatus){
                intent?.let {
                    adapter.addOrder(intent.getStringExtra("SCAN_BARCODE1"))
                   // adapter.notifyDataSetChanged()
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SNFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SnMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}