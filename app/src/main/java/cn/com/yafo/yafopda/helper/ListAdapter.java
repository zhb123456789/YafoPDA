package cn.com.yafo.yafopda.helper;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.com.yafo.yafopda.databinding.SnMainItemBinding;


public class ListAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> list;
    private int layoutId;//单布局
    private int variableId;

    public ListAdapter(Context context, List<T> list, int layoutId, int variableId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
        this.variableId = variableId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding = null;
        if (convertView == null){
            binding =DataBindingUtil.inflate(LayoutInflater.from(context),layoutId,parent,false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        assert binding != null;
        binding.setVariable(variableId,list.get(position));
        return binding.getRoot();
    }
}