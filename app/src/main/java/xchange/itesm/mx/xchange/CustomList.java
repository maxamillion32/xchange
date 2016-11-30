package xchange.itesm.mx.xchange;

/**
 * Created by Ciria on 28/11/2016.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class CustomList extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] web;
    private final long[] cost;
    private int[] offerWishList;
    private String[] status;

    public CustomList(Activity context, String[] web, long[] cost, int[] offerWish, String[] status) {
        super(context, R.layout.list_adapter, web);
        this.context = context;
        this.web = web;
        this.cost = cost;
        this.offerWishList=offerWish;
        this.status=status;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_adapter, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        txtTitle.setText("$"+cost[position]+"\n Offered: $"+offerWishList[position]);
            txtTitle.setText("$"+cost[position]+"\n Offered: $"+offerWishList[position]+"\n"+status[position]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        Picasso.with(context).load(web[position]).into(imageView);
        return rowView;

    }
}
