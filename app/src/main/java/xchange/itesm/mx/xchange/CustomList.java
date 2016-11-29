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
    private final int[] cost;
    private int[] offerWishList;
    private boolean[] sellerAccepted;
    private String[] confirmDone;

    public CustomList(Activity context, String[] web, int[] cost, int[] offerWish, boolean[] sellerAccepted,String[]confirmDone) {
        super(context, R.layout.list_adapter, web);
        this.context = context;
        this.web = web;
        this.cost = cost;
        this.offerWishList=offerWish;
        this.sellerAccepted=sellerAccepted;
        this.confirmDone=confirmDone;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_adapter, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        txtTitle.setText("$"+cost[position]+"\n Offered: $"+offerWishList[position]);
        if (sellerAccepted[position]==true){
            txtTitle.setText("$"+cost[position]+"\n Offered: $"+offerWishList[position]+"\n"+confirmDone[position]);
        }

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        Picasso.with(context).load(web[position]).into(imageView);
        return rowView;

    }
}
