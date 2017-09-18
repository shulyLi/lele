package org.newbee.lele.common.tool;

import java.util.*;


public class StringTool {
    private final static Set<Character> data = new LinkedHashSet<>();
    private final static String fg = " \n\t\r,.?:" +
            ";\"\'=\\/<>!#$%^&*()_+-=~`";
    static {
        for(int i = 0 ; i < fg.length() ; ++i) {
            data.add(fg.charAt(i));
        }
    }

    /**
     *  {@link #data} 里面存储的　分割符，　分割符　的意思他们　都可以被认为是　空格去处理
     *  因此对已一个segment 转换成　{@link String[]}
     *  无非就是每次找到一个　String 然后放到答案数组内　 直到没有
     *
     *       １）找到首部：　　随着idx的增加　找到第一个不是分隔符的　标记为　start
     *       2）　找到尾部：　 随着idx的增加　找到第一个是分割符的　　标记为　end
     *
     *       那么　[start, end) 这个区间为一个　有效　分割，　如果这个分割长度大于等于１
     *
     *
     */
    public static String [] splitLineWord(String segment){
        if (segment == null) return null;
        List<String> ans = new ArrayList<>();
        int idx = 0;
        while (idx < segment.length()) {
            while (idx < segment.length() && data.contains(segment.charAt(idx))) idx++;
            int start = idx;
            while (idx < segment.length() && !data.contains(segment.charAt(idx))) idx++;
            if (start != idx){
                ans.add(segment.substring(start, idx));
            }
        }
        String[] result = new String[ans.size()];
        return ans.subList(0, ans.size()).toArray(result);
    }

    public static void main(String[] args) {
        for(String s : StringTool.splitLineWord("food  'foot   @#$ two three    \n and\t\r\"  ")) {
            System.out.println("+" + s + "+");
        }
    }
}
