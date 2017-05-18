package br.com.felix.appcomunicacao.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WebServiceCliente {

    public String getDados(){
        URL url;
        String response = "";
        try {
            url = new URL("http://www.json-generator.com/api/json/get/ckMXJZiXhe?indent=2");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);

            OutputStream os = conn.getOutputStream();

            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
            else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = "{\"status\": \"false\", \"mensagens\":[ \"Erro, entre em contato\"]}";
        }

        return response;
    }

}
