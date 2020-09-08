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

import cn.com.yafo.yafopda.R;
import cn.com.yafo.yafopda.databinding.SnOrderItemBinding;
import cn.com.yafo.yafopda.vm.SnOrderEntryVM;

public class SnOrderAdapter extends BaseAdapter {

    private List<SnOrderEntryVM> data;

    private Context context;

    public SnOrderAdapter(List<SnOrderEntryVM> data, Context context) {
        this.data = data;
        this.context = context;
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
        SnOrderItemBinding binding;
        //ItemListBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.sn_order_item,//子项布局ID
                    parent, false);
            if (binding==null){
                //L.e("空的binding");
            }
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        //binding.setVariable(BR.orderitem, data.get(position)); //BR 不要写错了
        binding.setOrderitem(data.get(position)); //BR. 此方法也可以
        //binding.button4.setOnClickListener(new OnBtnClickListener( position));
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
                Navigation.findNavController(view).navigate(R.id.action_snOrderFragment_to_snMainFragment, bundle);

               // Toast.makeText(context,position, Toast.LENGTH_LONG).show();
                notifyDataSetChanged();//刷新数据
            }
            catch(Exception e)
            {Toast.makeText(context,e.toString(), Toast.LENGTH_LONG).show();}
        }
    }

}