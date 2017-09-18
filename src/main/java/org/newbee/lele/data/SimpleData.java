package org.newbee.lele.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;



public class SimpleData {

    private Map<String, AtomicInteger> map = new TreeMap<>();
    public void clear() {
        map.clear();
    }

    public void addWord(String word) {
        word = Convert.convertWord(word);
        AtomicInteger cnt = map.get(word);
        if (cnt == null) {
            cnt = new AtomicInteger(0);
            map.put(word, cnt);
        }
        cnt.addAndGet(1);
    }
    public int query(String word) {
        word = Convert.convertWord(word);
        return map.getOrDefault(word, new AtomicInteger(0)).get();
    }


    public List<Item> queryAll() {
        List<Item> ans = new ArrayList<>();
        for(Map.Entry<String, AtomicInteger> entry : map.entrySet()) {
            Item in = new Item();
            in.word = entry.getKey();
            in.cnt  = entry.getValue().get();
            ans.add(in);
        }
        return ans;
    }


    public static class Item{
        public String  word;
        public int cnt;

        public String getWord() {
            return word;
        }

        public Integer getCnt() {
            return (cnt);
        }
    }

    public static void main(String[] args) {
        SimpleData trie = new SimpleData();
        trie.addWord("ni");
        trie.addWord("ni");
        trie.addWord("NI");
        trie.addWord("Ni");
        trie.addWord("wo");
        trie.addWord("wo");
        trie.addWord("ta");
        trie.addWord("ta");
        trie.addWord("t");
        System.out.println(trie.query("ni"));
        System.out.println(trie.query("Ni"));
        System.out.println(trie.query("NI"));
        System.out.println(trie.query("wo"));
        System.out.println(trie.query("ta"));
        System.out.println(trie.query("t"));
    }
}