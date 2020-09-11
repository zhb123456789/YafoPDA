//package cn.com.yafo.yafopda.Adapter;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Toast;
//
//import androidx.databinding.DataBindingUtil;
//import androidx.navigation.Navigation;
//
//import java.io.IOException;
//import java.util.List;
//
//import cn.com.yafo.yafopda.BR;
//import cn.com.yafo.yafopda.R;
//import cn.com.yafo.yafopda.databinding.SnMainItemBinding;
//import cn.com.yafo.yafopda.helper.Loading;
//import cn.com.yafo.yafopda.vm.SnOrderVM;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class SnMainAdapter extends BaseAdapter {
//
//    private List<SnOrderVM> data;
//
//    public Context context;
//
//    // 添加Loading cancle()是按返回键，Loading框关闭的回调，可以做取消加载请求的操作。
//    public Loading mLoading ;
//
//    public SnMainAdapter(List<SnOrderVM> data, Context context) {
//        this.data = data;
//        this.context = context;
//        mLoading = new Loading(this.context) {
//            @Override
//            public void cancle() {
//            }
//        };
//    }
//    public void LoadingShow()
//    {
//        // 显示Loading
//        mLoading.show();
//    }
//    public void LoadingDismiss()
//    {
//        // 关闭Loading
//        mLoading.dismiss();
//    }
//    public  void  addOrder(String orCode)
//    {
//        final SnOrderVM order = new SnOrderVM();
//        LoadingShow();
//        try {
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder().get()
//                    .url("http://193.111.99.63/api/PDA/GetBill?billcode=$orCode")
//                    .build();
//
//
//            Call call = client.newCall(request);
//
////            //代理 notifyDataSetChanged事件，okhttp 不能在子线程直接调用
////             Handler handler  =new  Handler(){
////                @Override
////                public void handleMessage(Message msg) {
////                    super.handleMessage(msg);
////                    notifyDataSetChanged();
////                }
////            };
//
//            //异步请求
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure( Call call,   IOException e) {
//                    Log.d("UPDATE", "onFailure: $e");
//                }
//
//               // @Throws(IOException::class)
//               @Override
//               public void  onResponse(Call call , Response response ) throws IOException {
//                    if(response.code()==200) {
//                        JSONObject o = null;
//                        try {
//                            o = new JSONObject(response.body().string());
//                            SnOrderVM order = new SnOrderVM();
////                            order.code.postValue(o.getString("billCode"));
////                            order.custname.postValue(o.getString("customer"));
////                                    order.provider.postValue(o.getString("provider"));
////                                    order.dpt.postValue(o.getString("dpt"));
////                            order.biz.postValue(o.getString("biz"));
////                            order.storeCode.postValue(o.getString("storeCode"));
//                            data.add(order );
//                           // handler.sendEmptyMessage(0);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    else
//                    {
//
//                        // 1、创建简单的AlertDialog https://www.cnblogs.com/jiayongji/p/5371996.html
//                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//
//                        // (2)设置各种属性 // 注：不设置哪项属性，这个属性就默认不会显示出来
//                        dialog.setTitle("错误");
//                        dialog.setMessage("代码："+ response.code() + " 内容："+ response.body().string());
//
//                        // (3)设置dialog可否被取消
//                        dialog.setCancelable(true);
//
//                        // (4)显示出来
//                        dialog.show();
//
//                        //处理错误
//                        //Log.d(CrashHandler.TAG, "OnResponse: " + response.body()?.string())
//                    }
//                    LoadingDismiss();
//
//                }
//            });
//        }catch ( Exception e) {
//        Log.e("UPDATE ERROR:", "", e);
//    }
//    }
//
//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return data.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        SnMainItemBinding binding;
//        //ItemListBinding
//        if (convertView == null) {
//            binding = DataBindingUtil.inflate(LayoutInflater.from(context),
//                    R.layout.sn_main_item,//子项布局ID
//                    parent, false);
//            if (binding==null){
//                //L.e("空的binding");
//            }
//           // convertView = binding.getRoot();
//          //  convertView.setTag(binding);
//        } else {
//           // binding = (SnOrderItemBinding) convertView.getTag();
//            binding = DataBindingUtil.getBinding(convertView);
//        }
//
//        binding.setVariable(BR.order, data.get(position)); //BR.Orders 不要写错了
//        //binding.setOrder(data.get(position)); //BR.Orders 此方法也可以
//        binding.btDel.setOnClickListener(new OnDelClickListener( position));
//        binding.snMainItemLayout.setOnClickListener(new OnItemClickListener( position));
//        //https://segmentfault.com/a/1190000008246487  binding.setVariable(variableId, data.get(position));
//
//
//        return binding.getRoot();
//
//    }
//public  class OnDelClickListener implements View.OnClickListener {
//    private Integer position;
//
//    public OnDelClickListener( Integer position) {
//        this.position = position;
//    }
//    @Override
//    public void onClick(View view) {
//        try {
//            Bundle bundle = new Bundle();
//            bundle.putInt("position", position);
//            // Toast.makeText(context,position, Toast.LENGTH_LONG).show();
//            //提示确认
//            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//            dialog.setTitle("确认");
//           // dialog.setIcon(R.drawable.dictation2_64);
//            dialog.setMessage("订单数据尚未提交，确定要删除此项吗？");
//
//            // 设置“确定”按钮,使用DialogInterface.OnClickListener接口参数
//            dialog.setPositiveButton("确定",
//                    new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Log.d("Dialog", "点击了“确认”按钮");
//                            data.remove(data.get(position));
//                            notifyDataSetChanged();//刷新数据
//                        }
//                    });
//
//            // 设置“取消”按钮,使用DialogInterface.OnClickListener接口参数
//            dialog.setNegativeButton("取消",
//                    new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Log.d("Dialog", "点击了“取消”按钮");
//                        }
//                    });
//
//            dialog.show();
//            notifyDataSetChanged();//刷新数据
//        }
//        catch(Exception e)
//        {Toast.makeText(context,e.toString(), Toast.LENGTH_LONG).show();}
//    }
//}
//    public class OnItemClickListener implements View.OnClickListener{
//
//        private Integer position;
//
//        public OnItemClickListener( Integer position) {
//            this.position = position;
//        }
//
//        @Override
//        public void onClick(View view) {
//            try {
//                Bundle bundle = new Bundle();
//                bundle.putInt("position", position);
//                Navigation.findNavController(view).navigate(R.id.action_snMainFragment_to_snOrderFragment, bundle);
//
//               // Toast.makeText(context,position, Toast.LENGTH_LONG).show();
//                notifyDataSetChanged();//刷新数据
//            }
//            catch(Exception e)
//            {Toast.makeText(context,e.toString(), Toast.LENGTH_LONG).show();}
//        }
//    }
//
//}