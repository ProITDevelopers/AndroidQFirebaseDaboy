package proitdevelopers.com.androidquizfirebase.Adaptador;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import proitdevelopers.com.androidquizfirebase.Model.PerguntaErrada;
import proitdevelopers.com.androidquizfirebase.R;

public class AdapterResFinal extends RecyclerView.Adapter<AdapterResFinal.MyViewHolder>{

    private List<PerguntaErrada> perguntaErradas;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AdapterResFinal(List<PerguntaErrada> parceiros, Context context) {
        this.perguntaErradas = parceiros;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.resultado_quiz_final,viewGroup,false);
        return new MyViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {

        PerguntaErrada perguntaErrada = perguntaErradas.get(i);
        viewHolder.tv_pergunta_feita.setText(perguntaErrada.getpFeita());
        viewHolder.tv_pergunta_errada.setText(perguntaErrada.getpErrada());
        viewHolder.tv_pergunta_certa.setText(perguntaErrada.getpCerta());
    }

    @Override
    public int getItemCount() {
        return perguntaErradas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_pergunta_feita,tv_pergunta_errada,tv_pergunta_certa,tv_quiz;

        MyViewHolder(View itemView, OnItemClickListener onItemClickListener){
            super(itemView);
            itemView.setOnClickListener(this);
            tv_pergunta_feita = itemView.findViewById(R.id.tv_pergunta_feita);
            tv_pergunta_errada = itemView.findViewById(R.id.tv_pergunta_errada);
            tv_pergunta_certa = itemView.findViewById(R.id.tv_pergunta_certa);
        }

        @Override
        public void onClick(View view) {
            //onItemClickListener.OnItemClick(view,getAdapterPosition());
        }
    }


}
