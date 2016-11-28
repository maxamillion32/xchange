package xchange.itesm.mx.xchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


/**
 * Created by Ciria on 26/11/2016.
 */
public class WishList extends AppCompatActivity{
    ListView list;
    AllData dataL=new AllData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_list);

        CustomList adapter = new CustomList(WishList.this, dataL.getArticle(), dataL.getCost(), dataL.getOfferWishList(), dataL.getSellerAccepted(),dataL.getConfirmDone());
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(WishList.this,ContactSeller.class);
                intent.putExtra("pos",position);
                startActivity(intent);
            }
        });
    }
}
