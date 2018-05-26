package cu.IntegratedLanguages;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import cu.DataBase.ConnectionDB;

public class FragmentHome extends Fragment {

    private ConnectionDB db;
    private Calendar calendar;
    private int week;
    private String year;
    private PieChart pieChart;
    private LineChart lineChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view;
        view=inflater.inflate(R.layout.fragment_home,container,false);

        db=new ConnectionDB(view.getContext());
        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        lineChart = (LineChart) view.findViewById(R.id.lineChart);

        ImageButton left=(ImageButton)view.findViewById(R.id.btn_prev);
        Button now=(Button)view.findViewById(R.id.btn_now);
        ImageButton right=(ImageButton)view.findViewById(R.id.btn_next);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy",new Locale("es"));
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        year=formatter.format(calendar.getTime());
        week=calendar.get(Calendar.WEEK_OF_YEAR);


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week--;
                staticsApp(lineChart,week,year);
            }
        });
        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week=calendar.get(Calendar.WEEK_OF_YEAR);
                staticsApp(lineChart,week,year);

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week++;
                staticsApp(lineChart,week,year);

            }
        });

        staticsApp(lineChart,week,year);
        staticsEvaluation(pieChart);


        return view;
    }

    public void staticsEvaluation(PieChart pieChart){

        /*definimos algunos atributos*/
        pieChart.setHoleRadius(40f);
        pieChart.setDrawYValues(true);
        pieChart.setDrawXValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.animateXY(1500, 1500);

        Cursor c=db.getUserLog();
        assert c != null;
        if(c.moveToFirst()) {
            int id = c.getInt(c.getColumnIndex("id"));
            int def = db.getEvaluationSize(id, 0);
            int apr = db.getEvaluationSize(id, 1);
            int dap = db.getEvaluationSize(id, 2);


            int tot=def+apr+dap;

            /*creamos una lista para los valores Y*/
            ArrayList<Entry> valsY = new ArrayList<>();
            valsY.add(new Entry((def*100.0f)/tot, 0));
            valsY.add(new Entry((apr*100.0f)/tot, 1));
            valsY.add(new Entry((dap*100.0f)/tot, 2));


            /*creamos una lista para los valores X*/
            String[] valsX = getResources().getStringArray(R.array.evaluation);

            /*creamos una lista de colores*/
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.MAGENTA);
            colors.add(getResources().getColor(R.color.colorPrimary));
            colors.add(Color.RED);

            /*seteamos los valores de Y y los colores*/
            PieDataSet set1 = new PieDataSet(valsY, "");
            set1.setSliceSpace(4f);
            set1.setColors(colors);

            /*seteamos los valores de X*/
            PieData data = new PieData(valsX, set1);
            pieChart.setData(data);
            pieChart.highlightValues(null);
            pieChart.invalidate();

            /*Ocutar descripcion*/
            pieChart.setDescription(getResources().getString(R.string.description_evaluation));
            /*Ocultar leyenda*/
            pieChart.setDrawLegend(true);
        }
    }

  private ArrayList<Entry> getHistory(int user, int week, String year){
      ArrayList<Entry> entries = new ArrayList<>();

      entries.add(new Entry(db.getHistoryByDay(
              user,
              week,
              1,
              Integer.parseInt(year)),
              0));

      entries.add(new Entry(db.getHistoryByDay(
              user,
              week,
              2,
              Integer.parseInt(year)),
              1));

      entries.add(new Entry(db.getHistoryByDay(
              user,
              week,
              3,
              Integer.parseInt(year)),
              2));

      entries.add(new Entry(db.getHistoryByDay(
              user,
              week,
              4,
              Integer.parseInt(year)),
              3));

      entries.add(new Entry(db.getHistoryByDay(
              user,
              week,
              5,
              Integer.parseInt(year)),
              4));

      entries.add(new Entry(db.getHistoryByDay(
              user,
              week,
              6,
              Integer.parseInt(year)),
              5));

      entries.add(new Entry(db.getHistoryByDay(
              user,
              week,
              1,
              Integer.parseInt(year)),
              6));

return entries;
  }

    public void staticsApp(LineChart lineChart, int week, String year){

        lineChart.animateXY(1500, 1500);
        Cursor c= db.getUserLog();
        assert c != null;
        if(c.moveToFirst()) {
            int user = c.getInt(c.getColumnIndex("id"));
            ArrayList<Entry> entries=getHistory(user,week,year);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(getResources().getColor(R.color.colorPrimary));

            LineDataSet dataset = new LineDataSet(entries,
                    getResources().getString(R.string.entries_statics_app));
            dataset.setDrawCubic(true);
            dataset.setColors(colors);

            dataset.setCubicIntensity(0.2f);
            dataset.setDrawCircles(false);
            dataset.setLineWidth(1.8f);
            dataset.setHighLightColor(Color.rgb(244, 117, 117));

            String[] labels=getResources().getStringArray(R.array.days);

            LineData data = new LineData(labels, dataset);
            lineChart.setData(data);
            lineChart.setDescription(getResources().getString(R.string.week)+" "+week);

            lineChart.setDrawGridBackground(true);
            lineChart.setDrawLegend(true);
            lineChart.setTouchEnabled(false);

        }

    }

    @Override
    public void onStop() {
        db.close();
        super.onStop();
    }
}
