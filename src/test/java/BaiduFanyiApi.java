import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

/**
 * @author shuly
 * @date 2017/9/17.
 */
public class BaiduFanyiApi {
    private static String URI = "http://fanyi.baidu.com";
    private HttpHost httpHost = HttpHost.create(URI);
    private HttpClient httpClient = HttpClientBuilder.create().build();

    private static final String [] KEY = {"word_proto",  "word_pl", "word_ing", "word_done", "word_past"};

    private static final String [] ignore = {"word_est", "word_third", "word_er"};

    private void solve(String word) throws IOException{
        HttpRequest request = RequestBuilder.post("/v2transapi")
                .addParameter(new BasicNameValuePair("from", "en"))
                .addParameter(new BasicNameValuePair("to", "zh"))
                .addParameter(new BasicNameValuePair("query", word))
                .addParameter(new BasicNameValuePair("source", "txt"))
                .build();
        HttpResponse response = httpClient.execute(httpHost, request);
        if (response.getStatusLine().getStatusCode() / 100 != 2) {
            System.out.println(response.getStatusLine().getStatusCode());
        }
        String it = EntityUtils.toString(response.getEntity());
        JSONObject map = JSONObject.parseObject(it);
        JSONObject dictResult = map.getJSONObject("dict_result");
        if (dictResult == null) {
            out("can^t find " + word);
            return;
        }
        JSONObject means = dictResult.getJSONObject("simple_means");

        if (means == null) {
            out("can^t find " + word + "^ simple_means");
            return;
        }
        JSONObject exchange = means.getJSONObject("exchange");
        if (exchange == null) {
            out("can^t find " + word + "^ exchange");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(word);
        for(String key : KEY) {
            String value = exchange.getString(key);
            sb
            .append('\t')
            .append(value==null || value.length() == 0  ? "None" : value.substring(2, value.length() - 2 ));
        }
        System.out.println(sb.toString());
    }
    private void out(String message) {
        System.out.println(message);
    }
    private void main() throws IOException{
        solve("one");
    }

    public static void main(String[] args) throws Exception{
        BaiduFanyiApi api = new BaiduFanyiApi();
        api.main();
    }
}
