package ram.munindia.ModalandAdatpters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ram.munindia.R;

public class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.Data> {

    ArrayList<DispatchModel> list;
    int Rowlayout;
    Context context;

    public DispatchAdapter(ArrayList<DispatchModel> list, int dispatchsingle, Context applicationContext) {
        this.context = applicationContext;
        this.list=list;
        this.Rowlayout=dispatchsingle;
    }


    public DispatchAdapter.Data onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(Rowlayout, viewGroup, false);
        return new Data(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Data data, int i) {

       // hospital.hcfc_master_id_i.setText(invoices.get(i).getHcf_master_id());

        data.saleordernumber.setText(list.get(i).getSaleorder());
        data.barcodenumber.setText(list.get(i).getBarcodenumber());
        data.prepareddate.setText(list.get(i).getPrepareddate());
        data.dispatcheddate.setText(list.get(i).getDispatcheddate());
        data.qty.setText(list.get(i).getQty());
        data.dispatchstatus.setText(list.get(i).getDispatchedstatus());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Data extends RecyclerView.ViewHolder {

TextView saleordernumber,barcodenumber,prepareddate,dispatcheddate,qty,dispatchstatus;

        public Data(View itemView) {
            super(itemView);

            saleordernumber=itemView.findViewById(R.id.saleordernumber);
            barcodenumber=itemView.findViewById(R.id.barcodenumber);
            prepareddate=itemView.findViewById(R.id.prepareddate);
            dispatcheddate=itemView.findViewById(R.id.dispatcheddate);
            qty=itemView.findViewById(R.id.qty);
            dispatchstatus=itemView.findViewById(R.id.dispatchstatus);



        }
    }
}
