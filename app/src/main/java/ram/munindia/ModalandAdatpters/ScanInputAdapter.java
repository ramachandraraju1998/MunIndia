package ram.munindia.ModalandAdatpters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ram.munindia.R;
import ram.munindia.ScanInput;

public class ScanInputAdapter extends RecyclerView.Adapter<ScanInputAdapter.Data> {
    ArrayList<ScanInputModel> list;
    int Rowlayout;
    Context context;

    public ScanInputAdapter(ArrayList<ScanInputModel> list, int dispatchsingle, Context applicationContext) {
        this.context = applicationContext;
        this.list=list;
        this.Rowlayout=dispatchsingle;
    }


    public ScanInputAdapter.Data onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(Rowlayout, viewGroup, false);
        return new Data(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Data data, int i) {
final int position=i;
        // hospital.hcfc_master_id_i.setText(invoices.get(i).getHcf_master_id());


        data.barcodeshow.setText(list.get(i).getBarcode_no());
        data.productshow.setText(list.get(i).getProduct_varient_name());
        data.productcodeshow.setText(list.get(i).getProduct_code());
        data.productcategoryshow.setText(list.get(i).getProduct_category());
        data.productsubcategoryshow.setText(list.get(i).getSub_category());
        data.colorshow.setText(list.get(i).getColor());
        data.sizeshow.setText(list.get(i).getSizes());
        data.grammageshow.setText(list.get(i).getGrammage());
        data.quantityshow.setText(list.get(i).getQty());
data.remove.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());

        ScanInput.total.setText(String.valueOf("TOTAL="+getItemCount()));

    }

});


    }


    @Override
    public int getItemCount() {

        return list.size();
    }

    public class Data extends RecyclerView.ViewHolder {

        TextView barcodeshow,productshow,productcodeshow,productcategoryshow,productsubcategoryshow,colorshow,sizeshow,grammageshow,quantityshow;
        Button remove;

        public Data(View itemView) {
            super(itemView);

            barcodeshow=itemView.findViewById(R.id.barcodeshow);
            productshow=itemView.findViewById(R.id.productshow);
            productcodeshow=itemView.findViewById(R.id.productcodeshow);
            productcategoryshow=itemView.findViewById(R.id.productcategoryshow);
            productsubcategoryshow=itemView.findViewById(R.id.productsubcategoryshow);
            colorshow=itemView.findViewById(R.id.colorshow);
            sizeshow=itemView.findViewById(R.id.sizeshow);
            grammageshow=itemView.findViewById(R.id.grammageshow);
            quantityshow=itemView.findViewById(R.id.quantityshow);
            remove=itemView.findViewById(R.id.remove);



        }
    }
}
