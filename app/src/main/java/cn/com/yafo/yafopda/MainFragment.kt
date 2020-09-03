package cn.com.yafo.yafopda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cn.com.yafo.yafopda.databinding.MainFragmentBinding
import cn.com.yafo.yafopda.helper.Loading
import cn.com.yafo.yafopda.vm.LoginVm
import cn.com.yafo.yafopda.vm.MainViewModel


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }



    private lateinit var mBinding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?    ): View? {
        super.onCreate(savedInstanceState)
        mBinding =  DataBindingUtil.inflate(inflater, R.layout.main_fragment,container,false);
        //mBinding = DataBindingUtil.setContentView(getActivity(),R.layout.station_fragment_home);

        var viewModel = ViewModelProvider(this.requireActivity()).get(MainViewModel::class.java) // 关键代码
        var loginVm = ViewModelProvider(this.requireActivity()).get(LoginVm::class.java) // 关键代码

        //直接使用绑定 的属性 ObservableField
        mBinding.user=viewModel
        mBinding.login=loginVm

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
            val controller = Navigation.findNavController(v)

            controller.navigate(R.id.action_main_to_SN)
        })


        return mBinding.root;

    }

}


