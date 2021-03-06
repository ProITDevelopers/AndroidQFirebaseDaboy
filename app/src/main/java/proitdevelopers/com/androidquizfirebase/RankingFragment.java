package proitdevelopers.com.androidquizfirebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import proitdevelopers.com.androidquizfirebase.Common.Common;
import proitdevelopers.com.androidquizfirebase.Interface.ItemClickListener;
import proitdevelopers.com.androidquizfirebase.Interface.RankingCallBack;
import proitdevelopers.com.androidquizfirebase.Model.Category;
import proitdevelopers.com.androidquizfirebase.Model.QuestionStore;
import proitdevelopers.com.androidquizfirebase.Model.Ranking;
import proitdevelopers.com.androidquizfirebase.ViewHolder.CategoryViewHolder;
import proitdevelopers.com.androidquizfirebase.ViewHolder.RankingViewHolder;

public class RankingFragment extends Fragment {

    View myFragment;
    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter ;
    FirebaseDatabase database;
    DatabaseReference questionScore,rankingTbl;

    int sum=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking,container,false);

        rankingList =  myFragment.findViewById(R.id.rankingList);
        rankingList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        //rankingList.setLayoutManager(layoutManager);
        //Como o firebase vai organizar a lista do ascendente, então srá necessario reverter a
        //ordem do recyclerview
        //a partir do layoutManager
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
                //  showRanking(); //Depois de atalizar, mostrar o resultado do ranking ordenado
            }
        });

        FirebaseRecyclerOptions<Ranking> options =
                new FirebaseRecyclerOptions.Builder<Ranking>()
                        .setQuery(rankingTbl.orderByChild("score"), Ranking.class)
                        .build();

        //configurando o adapter
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RankingViewHolder holder, int position, @NonNull final Ranking model) {

                holder.txt_name.setText(model.getUserName());
                holder.txt_score.setText(String.valueOf(model.getScore()));

                Log.i("valores",model.getUserName() + "------" + model.getScore());

                //resolvendo o erro quando o usario clica no item
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(),ScoreDetail.class);
                        scoreDetail.putExtra("viewUser",model.getUserName());
                        startActivity(scoreDetail);
                    }
                });

            }

            @NonNull
            @Override
            public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_ranking, viewGroup, false);
                return new RankingViewHolder(view);
            }
        };
        rankingList.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        adapter.startListening();
        
        return myFragment;
    }


    //Aqui vai se criar uma interface callback para processar o valor
    private void updateScore(final String userName, final RankingCallBack<Ranking> callback) {
        Log.i("valordoResultado","antes fo for" + "sum ->" + sum);
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            Log.i("valordoResultado",data.getValue().getClass().getName() + "sum ->" + sum);
                            QuestionStore ques = data.getValue(QuestionStore.class);
                            sum+= Integer.valueOf(ques.getScore());
                        }
                        /*Depois de somar toda pontuacao, precisa-se processar o somatorio com variaveis aqui
                        porque o firebase é assincono e se processar fora a variavel 'sum' será redifinida para 0
                        */
                        Ranking ranking = new Ranking(userName,sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /* private void showRanking() {
        rankingTbl.orderByChild("score")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            Ranking local = data.getValue(Ranking.class);
                            Log.d("DEBUG",local.getUserName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }*/

}
