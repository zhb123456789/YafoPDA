package cn.com.yafo.yafopda.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;

import java.util.List;

import cn.com.yafo.yafopda.BR;
import cn.com.yafo.yafopda.R;
import cn.com.yafo.yafopda.databinding.SnItemBinding;
import cn.com.yafo.yafopda.vm.SnOrderVM;

public class ListAdapter<T>  extends BaseAdapter {
    private Context context;
    private List<T> data;
    private int layoutId;//子项布局ID
    private int brId;

    public ListAdapter(List<T> data, Context context, int layoutId, int brId) {
        this.data = data;
        this.context = context;
        this.layoutId = layoutId;
        this.brId = brId;
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
        ViewDataBinding binding = null;
        //ItemListBinding
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    layoutId,//子项布局ID
                    parent, false);
            if (binding==null){
                //L.e("空的binding");
            }
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }

        binding.setVariable(brId, data.get(position)); //BR.Orders 不要写错了
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
                Navigation.findNavController(view).navigate(R.id.action_SNFragment_to_sn_order_info_fragment, bundle);

               // Toast.makeText(context,position, Toast.LENGTH_LONG).show();
                notifyDataSetChanged();//刷新数据
            }
            catch(Exception e)
            {Toast.makeText(context,e.toString(), Toast.LENGTH_LONG).show();}
        }
    }

}