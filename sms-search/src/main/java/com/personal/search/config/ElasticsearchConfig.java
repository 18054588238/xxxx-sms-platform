package com.personal.search.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @ClassName ElasticsearchConfig
 * @Author liupanpan
 * @Date 2026/1/20
 * @Description 使用原生高级REST客户端的配置
 */
@Configuration
public class ElasticsearchConfig {

    @Value("#{'${elasticsearch.hostAndPosts}'.split(',')}")
    private String[] hostAndPosts;

    @Value("${elasticsearch.username}")
    private String username;
    @Value("${elasticsearch.password}")
    private String password;

    @Bean
    public RestHighLevelClient restHighLevelClient() {

        // 创建凭证提供者
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(username, password));

        // 构建HttpHost数组
        HttpHost[] hostPorts = Arrays.stream(hostAndPosts).map(host -> {
            String[] hostPort = host.split(":");
            return new HttpHost(hostPort[0], Integer.parseInt(hostPort[1]));
        }).toArray(HttpHost[]::new);

        // 构建RestClientBuilder
        RestClientBuilder builder = RestClient.builder(hostPorts)
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)); // 配置认证信息

        return new RestHighLevelClient(builder);
    }
}
