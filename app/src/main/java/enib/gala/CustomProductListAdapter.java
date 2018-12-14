package enib.gala;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
            convertView = layoutInflater.inflate(R.layout.list_product_layout, null);
            holder = new ViewHolder();
            holder.ProductNameView =  convertView.findViewById(R.id.textViewProductName);
            holder.PriceView = convertView.findViewById(R.id.textViewPrice);
            holder.CountView = convertView.findViewById(R.id.textViewCount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Product Product = this.listData.get(position);
        holder.ProductNameView.setText(Product.getName());
        String price =Product.getPrice().toString()+"€";
        holder.PriceView.setText(price);
        holder.PriceView.setTextColor(Color.GREEN);
        String text="x"+Product.getCount().toString();
        holder.CountView.setText(text);

        return convertView;
    }

    static class ViewHolder {
        TextView ProductNameView;
        TextView PriceView;
        TextView CountView;
    }

}