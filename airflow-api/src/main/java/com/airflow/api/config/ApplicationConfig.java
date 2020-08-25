package com.airflow.api.config;

import com.airflow.api.client.AirFlowClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    @Bean
    public RestTemplate restTemplateCountries() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = poolingHttpClientConnectionManager(20, 20);
        RequestConfig requestConfig = requestConfig(10000, 20000);
        CloseableHttpClient httpClient = httpClient(poolingHttpClientConnectionManager, requestConfig);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

    @Bean
    public Map<String, String> getProperties() {
        Map<String, String> props = new HashMap<>();

        props.put("api_client", "airflow.api.client.json_client");
        props.put("ENDPOINT_URL", "http://127.0.0.1:8080");
        props.put("DUMMY_PROP_THREE", "prop_three");
        props.put("DUMMY_PROP_FOUR", "prop_four");

        return props;
    }

    @Bean
    public AirFlowClient createAirflowClient() {
        return new AirFlowClient(getProperties(), restTemplateCountries());
    }

    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(int maxTotal, int maxPerRoute) {
        PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
        result.setMaxTotal(maxTotal);
        result.setDefaultMaxPerRoute(maxPerRoute);
        return result;
    }

    @Bean
    public Gson createGson(){
        return new GsonBuilder().setPrettyPrinting().create();
    }

    private RequestConfig requestConfig(int connectionTimeout, int socketTimeout) {
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(10000)      //how long wait for a connection from connection manager
                .setConnectTimeout(connectionTimeout)   //how long wait for establishing connection
                .setSocketTimeout(socketTimeout)        //how long wait between packets
                .build();
        return config;
    }

    private CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
                                           RequestConfig requestConfig) {
        CloseableHttpClient closeableHttpClient = HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return closeableHttpClient;
    }

}
