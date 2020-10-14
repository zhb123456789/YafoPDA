package cn.com.yafo.yafopda

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cn.com.yafo.yafopda.databinding.MainFragmentBinding
import cn.com.yafo.yafopda.helper.GlobalVar
import cn.com.yafo.yafopda.vm.MainViewModel


class MainFragment : Fragment() {


    private lateinit var mBinding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?    ): View? {
        super.onCreate(savedInstanceState)
        mBinding =  DataBindingUtil.inflate(inflater, R.layout.main_fragment,container,false);
        //mBinding = DataBindingUtil.setContentView(getActivity(),R.layout.station_fragment_home);

        var viewModel = ViewModelProvider(this.requireActivity()).get(MainViewModel::class.java) // 关键代码

        //直接使用绑定 的属性 ObservableField
        mBinding.user=viewModel

        mBinding.lifecycleOwner = this.requireActivity()

//        //使用 MutableLiveData<String>() 单个属性
//        viewModel.password.observe(viewLifecycleOwner, Observer {
//            // Update the UI when the data has changed
//            runOnUiThread {  btnBox.text=viewModel.password.value }
//        })
//        //使用  MutableLiveData<User>() 引用属性
//        viewModel.user.observe(viewLifecycleOwner, // Update the UI when the data has changed
//            Observer<User> { })


        //设置按钮导航
        mBinding.btnSN.setOnClickListener(View.OnClickListener { v ->
//            if (GlobalVar.userVM.username.value == null)
//            { val t =
//                Toast.makeText(context, "请先填写用户名或编码", Toast.LENGTH_LONG)
//                t.setGravity(Gravity.TOP, 0, 0)
//                t.show()
//            }
//            else {
//                val controller = Navigation.findNavController(v)
//                controller.navigate(R.id.action_main_to_SN)
//            }
            navigation(R.id.action_main_to_SN,v,null)
        })
        mBinding.btnBox.setOnClickListener(View.OnClickListener { v ->
            val bundle = Bundle()
            bundle.putString("Operation", "boxing")
            navigation(R.id.action_mainFragment_to_operation_fragment,v,bundle)
        })
        mBinding.btnPicking.setOnClickListener(View.OnClickListener { v ->
            val bundle = Bundle()
            bundle.putString("Operation", "picking")

            navigation(R.id.action_mainFragment_to_operation_fragment,v,bundle)
        })


        return mBinding.root;

    }
    fun navigation(resId:Int, v:View, bundle: Bundle?)
    {
        if (GlobalVar.userVM.username.value == null)
        { val t =
            Toast.makeText(context, "请先填写用户名或编码", Toast.LENGTH_LONG)
            t.setGravity(Gravity.TOP, 0, 0)
            t.show()
        }
        else {
            val controller = Navigation.findNavController(v)
            if(bundle==null)
                controller.navigate(resId)
            else
                controller.navigate(resId,bundle)
        }
    }

}


