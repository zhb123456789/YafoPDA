package cn.com.yafo.yafopda.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import java.util.List;
import cn.com.yafo.yafopda.BR;
import cn.com.yafo.yafopda.R;
import cn.com.yafo.yafopda.databinding.SnItemBinding;
import cn.com.yafo.yafopda.helper.Loading;
import cn.com.yafo.yafopda.vm.SnOrderVM;

public class OrdersAdapter  extends BaseAdapter {

    private List<SnOrderVM> data;

    private Context context;

    // 添加Loading cancle()是按返回键，Loading框关闭的回调，可以做取消加载请求的操作。
    public Loading mLoading ;

    public OrdersAdapter(List<SnOrderVM> data, Context context) {
        this.data = data;
        this.context = context;
        mLoading = new Loading(this.context) {
            @Override
            public void cancle() {
            }
        };
    }
    public void LoadingShow()
    {
        // 显示Loading
        mLoading.show();
    }
    public void LoadingDismiss()
    {
        // 关闭Loading
        mLoading.dismiss();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SnItemBinding binding;
        //ItemListBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.sn_item,//子项布局ID
                    parent, false);
            if (binding==null){
                //L.e("空的binding");
            }
           // convertView = binding.getRoot();
          //  convertView.setTag(binding);
        } else {
           // binding = (SnOrderItemBinding) convertView.getTag();
            binding = DataBindingUtil.getBinding(convertView);
        }

        binding.setVariable(BR.order, data.get(position)); //BR.Orders 不要写错了
        //binding.setOrder(data.get(position)); //BR.Orders 此方法也可以
        binding.button4.setOnClickListener(new OnBtnClickListener( position));
        //https://segmentfault.com/a/1190000008246487  binding.setVariable(variableId, data.get(position));


        return binding.getRoot();

    }

    public class OnBtnClickListener implements View.OnClickListener{

        private Integer position;

        public OnBtnClickListener( Integer position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            try {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                Navigation.findNavController(view).navigate(R.id.action_SNFragment_to_sn_order_info_fragment, bundle);

               // Toast.makeText(context,position, Toast.LENGTH_LONG).show();
                notifyDataSetChanged();//刷新数据
            }
            catch(Exception e)
            {Toast.makeText(context,e.toString(), Toast.LENGTH_LONG).show();}
        }
    }

}