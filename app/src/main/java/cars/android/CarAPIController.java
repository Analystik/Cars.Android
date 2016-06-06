package cars.android;

import java.util.ArrayList;
import java.util.List;

import cars.android.Models.Car;
import cars.android.Models.FinancialEvaluation;
import cars.android.Models.Make;
import cars.android.Models.Model;
import cars.android.Models.Profile;
import cars.android.Models.Province;

/**
 * Created by Analystik on 2016-06-02.
 */
public class CarAPIController {

   /* private Profile profile;
    private FinancialEvaluation financialEvaluation;*/

    private static CarAPIController instance = null;

    protected CarAPIController() {}

    public static CarAPIController getInstance()
    {
        if(instance == null)
            instance = new CarAPIController();
        return  instance;
    }


    public List<Make> GetMakes() {
        try {
            return new Make().execute().get();
        }
        catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }

    public List<Model> GetModels(int _makeId) {
        try {
            return new Model().execute(_makeId).get();
        }
        catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }

    public List<Car> GetCars(int makeId, int modelId) {
        try {
            return new Car().execute(makeId,modelId).get();
        }
        catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }

    public List<Province> GetProvince() {
        try {
            return new Province().execute().get();
        }
        catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }

    public FinancialEvaluation Calculate(Profile _result)
    {
        try {
            return new Profile().execute(_result).get();
        }
        catch(Exception ex)
        {
            return new FinancialEvaluation();
        }
    }

}