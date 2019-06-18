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

        data.product.setText(list.get(i).getProductname());
        data.color.setText(list.get(i).getColor());
        data.size.setText(list.get(i).getSize());
        data.carton.setText(list.get(i).getCarton());
        data.pcspercarton.setText(list.get(i).getPicspercartton());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Data extends RecyclerView.ViewHolder {

TextView product,color,size,carton,pcspercarton;

        public Data(View itemView) {
            super(itemView);

            product=itemView.findViewById(R.id.product);
            color=itemView.findViewById(R.id.color);
            size=itemView.findViewById(R.id.size);
            carton=itemView.findViewById(R.id.carton);
            pcspercarton=itemView.findViewById(R.id.pcspercarton);


        }
    }
}
