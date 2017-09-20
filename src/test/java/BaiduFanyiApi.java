import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.newbee.lele.common.tool.StringTool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class BaiduFanyiApi {
    private static String URI = "http://fanyi.baidu.com";
    private HttpHost httpHost = HttpHost.create(URI);
    private HttpClient httpClient = HttpClientFactory.create();

    private static final String [] KEY = {"word_proto",  "word_pl", "word_ing", "word_done", "word_past"};

    private static final String [] ignore = {"word_est", "word_third", "word_er"};

    private URL OUT = ClassLoader.getSystemClassLoader().getResource("dataConvertSimple");

    private FileChannel channel ;
    private ByteBuffer buffer;

    public BaiduFanyiApi(){
        try {
            RandomAccessFile OUT_FILE = new RandomAccessFile(OUT.getFile(), "rw");
            channel = OUT_FILE.getChannel();
            buffer = ByteBuffer.allocate(300);
            buffer.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    private ExecutorService executorService = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
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
            .append('|')
            .append(value==null || value.length() == 0  ? "" : value.substring(2, value.length() - 2 ));
        }
        buffer.put(sb.toString().getBytes());
        if (buffer.position()  + 200 > buffer.limit()) {
            buffer.flip();
            while (buffer.hasRemaining()) channel.write(buffer);
            buffer.clear();
        }
        System.out.println(sb.toString());
    }
    private void out(String message) {
        System.out.println(message);
    }
    private void main() throws Exception{
        URL url = ClassLoader.getSystemClassLoader().getResource("yasi.word");
        if (url == null) {
            System.out.println("我擦不可能啊 url is null");
            System.exit(-1);
        }
        Set<String> use = new HashSet<>();
        try {
            RandomAccessFile file = new RandomAccessFile(url.getFile(), "r");
            String line;
            while( (line = file.readLine()) != null) {
                String[] wordsArray = StringTool.splitLineWord(line);
                if (wordsArray.length == 0) continue;
                for(int i = 0 ; i < wordsArray.length; ++ i) {
                    if (!use.contains(wordsArray[i]) && wordsArray[i].matches("[a-zA-Z]+")) {
                        use.add(wordsArray[i]);
                        final String it = wordsArray[i];
                        {
                            try {
                                solve(it);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Thread.sleep(TimeUnit.MILLISECONDS.toMillis(500));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            buffer.flip();
            while (buffer.hasRemaining()) channel.write(buffer);
            buffer.clear();
            channel.close();
        }
    }

    public static void main(String[] args) throws Exception{
        BaiduFanyiApi api = new BaiduFanyiApi();
        api.solve("speed");
        //api.main();

    }
}
