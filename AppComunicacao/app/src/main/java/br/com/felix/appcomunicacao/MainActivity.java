package br.com.felix.appcomunicacao;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.felix.appcomunicacao.custom.ItemMarca;
import br.com.felix.appcomunicacao.http.WebServiceCliente;
import br.com.felix.appcomunicacao.model.Item;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LineChart lineChartExample;
    private ProgressDialog dialog;
    private List<Item> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.titulo));
        setSupportActionBar(toolbar);

        Button btnGerarRelatorio = (Button) findViewById(R.id.btnGerarRelatorio);
        btnGerarRelatorio.setOnClickListener(this);

        lineChartExample = (LineChart) findViewById(R.id.lineChartExample);
        lineChartExample.setDrawGridBackground(false);
        lineChartExample.getDescription().setEnabled(false);
        lineChartExample.setTouchEnabled(true);
        lineChartExample.setDragEnabled(true);
        lineChartExample.setScaleEnabled(true);
        lineChartExample.setPinchZoom(true);

        ItemMarca mv = new ItemMarca(this, R.layout.custom_marker_view);
        mv.setChartView(lineChartExample);
        lineChartExample.setMarker(mv);

        lineChartExample.getAxisRight().setEnabled(false);
        lineChartExample.animateX(1500);
        lineChartExample.getLegend().setEnabled(false);
        lineChartExample.invalidate();
    }

    @Override
    public void onClick(View v) {
        GetDados getDados = new GetDados();
        getDados.execute();
    }

    private void setContent() {
        if(posts != null && posts.size() > 0) {
            lineChartExample.setVisibility(View.VISIBLE);

            ArrayList<Entry> values = new ArrayList<>();
            final List<String> listNomes = new ArrayList<>();

            for (int i = 0; i < posts.size(); i++) {
                listNomes.add(posts.get(i).getNome());
                values.add(new Entry(i, posts.get(i).getValor()));
            }

            IAxisValueFormatter formatter = new IAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return listNomes.get((int) value);
                }
            };

            XAxis xAxis = lineChartExample.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(formatter);

            LineDataSet set1 = new LineDataSet(values, "Gastos do mês");
            set1.setDrawIcons(false);

            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setFillColor(Color.BLACK);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            lineChartExample.setData(data);
            lineChartExample.notifyDataSetChanged();
            lineChartExample.invalidate();
        }
    }

    public class GetDados extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(R.string.aguarde);
            dialog.setMessage("Carregando...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

                String resultado = new WebServiceCliente().getDados();

                Type listType = new TypeToken<Map<String,List<Item>>>(){}.getType();
                Map<String, List<Item>> itens = gson.fromJson(resultado, listType);

                posts = itens.get("meses");

                return true;
            }catch (Exception e){
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                setContent();
            }
            dialog.dismiss();
        }
    }
}
