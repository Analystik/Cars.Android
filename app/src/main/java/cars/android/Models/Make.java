package cars.android.Models;

import android.os.AsyncTask;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Analystik on 2016-06-02.
 */
public class Make extends AsyncTask<Void, Void, List<Make>> {

    public int Id;
    public String Name;

    // What to display in the Spinner list.
    @Override
    public String toString() {
        return this.Name;
    }

    @Override
    protected List<Make> doInBackground(Void... params) {
        try {
            URL url = new URL("http://cars101.azurewebsites.net/api/makes");
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            hc.setRequestMethod("GET");
            int responseCode = hc.getResponseCode();

            if (responseCode == 200) {
                List<Make> makes = new ArrayList<>();
                BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                String line;
                StringBuilder responseOutput = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    JSONArray jsonArray = new JSONArray(line);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Make m = new Make();
                        m.Id = jsonArray.getJSONObject(i).getInt("Id");
                        m.Name = jsonArray.getJSONObject(i).getString("Name");
                        makes.add(m);
                    }
                }
                br.close();
                return makes;
            }
        } catch (Exception ex) {
            System.out.println("Erreur GET METHOD: " + ex);
        }
        return null;
    }
}