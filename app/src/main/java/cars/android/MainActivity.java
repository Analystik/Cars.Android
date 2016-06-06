package cars.android;


import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cars.android.Models.Car;
import cars.android.Models.Make;
import cars.android.Models.Model;
import cars.android.Models.Profile;
import cars.android.Models.Province;
import cars.android.Models.FinancialEvaluation;
import cars.android.databinding.ResultBinding;

public class MainActivity extends AppCompatActivity {

    private List<Make> Makes;
    public void SetMakes(List<Make> _makes)
    {
        this.Makes = _makes;
        this.populateSpinner(new ArrayList<Object>(this.Makes), (Spinner) this.findViewById(R.id.reponseMake),  makeAdapter);
    }
    public  List<Make> GetMakes()
    {
        return this.Makes;
    }

    private List<Model> Models;
    public void SetModels(List<Model> _models)
    {
        this.Models = _models;
        this.populateSpinner(new ArrayList<Object>(this.Models), (Spinner) this.findViewById(R.id.reponseModel),  modelAdapter);
    }
    public  List<Model> GetModels()
    {
        return this.Models;
    }

    private List<Car> Cars;
    public void SetCars(List<Car> _cars)
    {
        this.Cars = _cars;
        this.populateSpinner(new ArrayList<Object>(this.Cars), (Spinner) this.findViewById(R.id.reponseCar),  carAdapter);
    }
    public  List<Car> GetCars()
    {
        return this.Cars;
    }

    private List<Province> Provinces;
    public void SetProvinces(List<Province> _provinces)
    {
        this.Provinces = _provinces;
        this.populateSpinner(new ArrayList<Object>(this.Provinces), (Spinner) this.findViewById(R.id.reponseProvince),  provinceAdapter);
    }
    public  List<Province> GetProvinces()
    {
        return this.Provinces;
    }

    private FinancialEvaluation FinancialEvaluation;
    public void SetFinancialEvaluation(FinancialEvaluation _financialEvaluation)
    {
        this.FinancialEvaluation = _financialEvaluation;
    }
    public FinancialEvaluation GetFinancialEvaluation()
    {
        this.FinancialEvaluation.GasTotalExpensesIn8Years = Math.round(this.FinancialEvaluation.GasTotalExpensesIn8Years);
        return this.FinancialEvaluation;
    }

    Spinner responseMake;
    Spinner responseModel;
    SeekBar responseKiloSK;
    ArrayAdapter<Make> makeAdapter;
    ArrayAdapter<Model> modelAdapter;
    ArrayAdapter<Car> carAdapter;
    ArrayAdapter<Province> provinceAdapter;
    CarAPIController Context;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CANADA);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.Context = CarAPIController.getInstance();

        goToMainView(new View(this));
    }

    public void goToMainView(View view) {
        setContentView(R.layout.main);
        initMainView();
    }

    private void initMainView()
    {
        // Intialization of the main page elements
        responseKiloSK = (SeekBar) this.findViewById(R.id.reponseKiloSK);
        responseMake = (Spinner) this.findViewById(R.id.reponseMake);
        responseModel = (Spinner) this.findViewById(R.id.reponseModel);
        ((TextView) this.findViewById(R.id.reponseKilo)).setText("0");
        this.SetMakes(Context.GetMakes());
        this.SetProvinces(Context.GetProvince());
        responseMake.setOnItemSelectedListener(new MakeOnItemSelectedListener());
        responseModel.setOnItemSelectedListener(new ModelOnItemSelectedListener());
        responseKiloSK.setOnSeekBarChangeListener(new KiloSeekBarOnItemSelectedListener());
    }



    public void btnEvaluate_Click(View v)
    {
        Profile profile = new Profile();
        profile.CarId = ((Car) ((Spinner) MainActivity.this.findViewById(R.id.reponseCar)).getSelectedItem()).Id;
        profile.KMPerYear = ((SeekBar)MainActivity.this.findViewById(R.id.reponseKiloSK)).getProgress();
        profile.ProvinceId = ((Province)((Spinner) MainActivity.this.findViewById(R.id.reponseProvince)).getSelectedItem()).Id;

        MainActivity.this.SetFinancialEvaluation(Context.Calculate(profile));
        goToResultView();
    }

    private void populateSpinner(List<Object> _data, Spinner _spinner, ArrayAdapter _adapter) {
        _adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, _data);
        _spinner.setAdapter(_adapter);
    }

    public void goToResultView() {
        //Binding data in result vie
        ResultBinding binding = DataBindingUtil.setContentView(this, R.layout.result);
        binding.setFinancialEvaluation(MainActivity.this.GetFinancialEvaluation());
    }


    class MakeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            MainActivity.this.SetModels(Context.GetModels(((Make) responseMake.getSelectedItem()).Id));
        }
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    class ModelOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            MainActivity.this.SetCars(Context.GetCars(((Make) responseMake.getSelectedItem()).Id,((Model) responseModel.getSelectedItem()).Id));
        }
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    class KiloSeekBarOnItemSelectedListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            ((TextView)MainActivity.this.findViewById(R.id.reponseKilo)).setText(numberFormat.format(progress));
        }
        public void onStartTrackingTouch(SeekBar seekBar) {}
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}


