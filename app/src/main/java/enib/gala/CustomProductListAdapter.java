package enib.gala;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomProductListAdapter  extends BaseAdapter {

    private List<Product> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomProductListAdapter(Context aContext,  List<Product> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout, null);
            holder = new ViewHolder();
            holder.ConsumptionNameView =  convertView.findViewById(R.id.textView_consumption);
            holder.PriceView = convertView.findViewById(R.id.textView_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product Product = this.listData.get(position);
        holder.ConsumptionNameView.setText(Product.getName());
        String price =Product.getPrice().toString()+"€";
        holder.PriceView.setText(price);
        holder.PriceView.setTextColor(Product.getPrice()>=0 ? Color.GREEN : Color.RED);


        return convertView;
    }


    static class ViewHolder {

        TextView ConsumptionNameView;
        TextView PriceView;
    }

}