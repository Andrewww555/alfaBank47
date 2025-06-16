import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String monthStr = String.valueOf(month);
        if(month<10) monthStr = "0"+monthStr;
        if(day<10) dayStr = "0"+dayStr;
        String yesterday = (calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+dayStr);
        double yesterdayRub = getRub(yesterday);
        double todayRub = getRub(null);
        System.out.println(yesterdayRub);
        System.out.println(todayRub);
        if(todayRub > yesterdayRub){
            getGif("rich");
        }else{
            getGif("broke");
        }
    }

    static double getRub(String date){
        final String APP_ID = "ad1c3a856f32428db34c670bfefe84c1";
        double rub = 0;
        String spec = "https://openexchangerates.org/api/latest.json?app_id="+APP_ID;
        if(date != null)
            spec = "https://openexchangerates.org/api/historical/"+date+".json?app_id="+APP_ID;
        try {
            URL url = new URL(spec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null){
                result.append(line);
            }
            JSONParser jsonParser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
                JSONObject jsonRates = (JSONObject) jsonObject.get("rates");
                rub = (double) jsonRates.get("RUB");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rub;
    }

    static void getGif(String search) {
        final String YOUR_API_KEY = "MGR9scZeBiM84H88nbtqoLxVvdN8vxzl";
        System.out.println("https://api.giphy.com/v1/gifs/search?q="+search+"&api_key="+YOUR_API_KEY);
        try {
            URL url = new URL("https://api.giphy.com/v1/gifs/search?q="+search+"&api_key="+YOUR_API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(String.valueOf(result));
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
            JSONObject jsonFirstData = (JSONObject) jsonArray.get((int) Math.round(Math.random()*jsonArray.size()-1));
            String fileId = jsonFirstData.get("id").toString(); // h0MTqLyvgG0Ss
            String fullPath = "https://i.giphy.com/"+fileId+".gif";
            System.out.println( fullPath );
        } catch (IOException e){
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}