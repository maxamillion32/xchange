package xchange.itesm.mx.xchange;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by Ciria on 26/11/2016.
 */
public class SellerDetails extends AppCompatActivity {
    Context context;
    AllData data=new AllData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_details);
        ImageView imageView=(ImageView)findViewById(R.id.imgUsrDetails);
        RatingBar ratingBar=(RatingBar)findViewById(R.id.ratingBarDetails);
        final EditText  editText=(EditText)findViewById(R.id.msjToSeller) ;
        Button sendButton= (Button)findViewById(R.id.sendButtonDetails);

        Intent intent=getIntent();
        final int position=intent.getIntExtra("pos",0);
        int seller=data.whosSeller(position);
        Picasso.with(context).load(data.getUsers()[seller]).into(imageView);
        ratingBar.setRating(data.getStars()[seller]);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msj= editText.getText().toString();
                data.setMsj(msj);
                Toast.makeText(SellerDetails.this, data.getMsj(), Toast.LENGTH_SHORT).show();
                Intent intent= new Intent (SellerDetails.this,WishList.class);
                startActivity(intent);
            }
        });
    }
}
