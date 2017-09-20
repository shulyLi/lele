package org.newbee.lele.data;

import org.newbee.lele.common.tool.StringTool;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class Convert {
    private static Map<String, String> convert = new HashMap<>();

    static {
        URL url = ClassLoader.getSystemClassLoader().getResource("dataConvert");
        if (url == null) {
            System.out.println("我擦不可能啊 url is null");
            System.exit(-1);
        }
        try {
            RandomAccessFile file = new RandomAccessFile(url.getFile(), "r");
            String line;
            while( (line = file.readLine()) != null) {

                String[] wordsArray = StringTool.splitLineWord(line);
                if (wordsArray.length == 0) continue;

                for(int i = 1 ; i < wordsArray.length; ++ i) {
                    convert.put(wordsArray[i], wordsArray[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("单词同步完毕");
        }
    }
    static String convertWord(String word) {
        word = word.toLowerCase();
        return convert.getOrDefault(word, word);
    }
}
