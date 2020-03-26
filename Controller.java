package sample;

import com.google.gson.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SerializablePermission;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Controller{

    @FXML
    private TextArea textArea;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private Button searchButton;
    @FXML
    private TextArea earlyLink;
    @FXML
    private ChoiceBox itemsList;

   // choiceBox
    public void selectList(){
        itemsList.getItems().clear();
        if (choiceBox.getValue() != null){
            getItemList();
        }
    }

    // searchButton
    public void searchButtonClicked(){
        if (searchButton.isPressed()){
            textArea.clear();
        }
        getJson();
    }

    // listButton Action
    private void getItemList(){
        String domain = (String) choiceBox.getValue();
        JSONParser parser = new JSONParser();

        try {
            URL url = new URL("https://" + domain + ".com/products.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String string = "";
            while (null != (string = br.readLine())) {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement element = new JsonParser().parse(string);
                String pretty = gson.toJson(element);

                Object obj = parser.parse(String.valueOf(element));
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray list = (JSONArray) jsonObject.get("products");

                Iterator<JSONObject> iterator = list.iterator();
                while (iterator.hasNext()) {
                    JSONObject jsonObject1 = iterator.next();
                    String item = (String) jsonObject1.get("title");
                    ArrayList<String> itemList = new ArrayList<String>(Collections.singleton((item)));
                    //System.out.println(item);

                    itemsList.getItems().addAll(itemList);
                    //System.out.println(itemList);
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // searchButton Action
    private void getJson() {

        String domain = (String) choiceBox.getValue();
        String keyword = (String) itemsList.getValue();
        JSONParser parser = new JSONParser();

           try {
                URL url = new URL("https://" + domain + ".com/products.json");
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String str = "";
                while (null != (str = br.readLine())) {

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JsonElement element = new JsonParser().parse(str);
                    String pretty = gson.toJson(element);

                    textArea.clear();

                    if (keyword != null && pretty.contains(keyword)){

                        Object obj = parser.parse(String.valueOf(element));
                        JSONObject jsonObject = (JSONObject) obj;
                        JSONArray list = (JSONArray) jsonObject.get("products");

                        Iterator<JSONObject> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            JSONObject jsonObject1 = iterator.next();

                            if (jsonObject1.get("title").equals(keyword)){
                                earlyLink.setText("https://" + domain + ".com/products/" + jsonObject1.get("handle"));
                                String pretty2 = gson.toJson(jsonObject1.get("variants"));
                                //System.out.println(pretty2);

                                JSONArray list2 = (JSONArray) jsonObject1.get("variants");
                                Iterator<JSONObject> iterator1 = list2.iterator();

                                while (iterator1.hasNext()){
                                    JSONObject jsonObject2 = iterator1.next();

                                    String var = gson.toJson(jsonObject2.get("id"));
                                    String size = gson.toJson(jsonObject2.get("title"));

                                    textArea.appendText("\n" + size + " - " + var);

                                }

                            }


                        }

                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
               e.printStackTrace();
           }

    }

}
/*



 */