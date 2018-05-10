package cu.IntegratedLanguages;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

import cu.DataBase.ConnectionDB;

public class FragmentStaticsExercise extends Fragment {

    private Context context;
    private ConnectionDB db;
    private int id_subject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_statics_exercise,container,false);
        context=view.getContext();

        PieChart pieChart=(PieChart)view.findViewById(R.id.pie_chart);
        Button next=(Button) view.findViewById(R.id.btn_next_home);

        id_subject=(getArguments()!=null)?getArguments().getInt("id_subject"):1;

        db=new ConnectionDB(context);
        staticsEvaluation(pieChart);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upIntent = getActivity().getParentActivityIntent();
                getActivity().navigateUpTo(upIntent);

            }
        });


        return view;
    }

    public void staticsEvaluation(PieChart pieChart){

        pieChart.setHoleRadius(40f);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawXValues(true);
        pieChart.setDrawYValues(true);
        pieChart.setDrawXValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.animateXY(1500, 1500);

        Cursor c=db.getUserLog();
        assert c != null;
        if(c.moveToFirst()) {
            int id = c.getInt(c.getColumnIndex("id"));
            int def = db.getEvaluationSizeBySubject(id,id_subject,0);
            int apr = db.getEvaluationSizeBySubject(id,id_subject, 1);
            int dap = db.getEvaluationSizeBySubject(id,id_subject, 2);

            //int tot=def+apr+dap;

            ArrayList<Entry> valuesY = new ArrayList<>();
            valuesY.add(new Entry(def, 0, def));
            valuesY.add(new Entry(apr, 1, apr));
            valuesY.add(new Entry(dap, 2, dap));

            String[] valuesX = getResources().getStringArray(R.array.evaluation);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.DKGRAY);
            colors.add(getResources().getColor(R.color.colorPrimary));
            colors.add(Color.GREEN);

            PieDataSet set1 = new PieDataSet(valuesY, "");
            set1.setSliceSpace(4f);
            set1.setColors(colors);

            PieData data = new PieData(valuesX, set1);
            pieChart.setData(data);
            pieChart.highlightValues(null);
            pieChart.invalidate();

            pieChart.setDescription(
                    getResources().getString(R.string.description_evaluation));

            pieChart.setDrawLegend(true);
        }
    }
}
