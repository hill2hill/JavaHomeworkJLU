import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;


import javax.sound.sampled.AudioFormat.Encoding;

public  class WordCorrection {
    public static void main(String[] args) {
        Dictionary dict = new Dictionary();
        TestTxt testtxt = new TestTxt();
        String[] recommend_words;
        String dict_path = "./Dictionary.txt", test_path = "./test.txt";

        dict.dict_array = dict.load_dict(dict_path);
        testtxt.testtxt_arraylist = testtxt.load_txt(test_path);
        
        for(int i = 0;i<testtxt.testtxt_arraylist.size();i++){
            String word = (String)testtxt.testtxt_arraylist.get(i);
            dict.dict_array = dict.load_dict(dict_path);
            if (!dict.is_in_dictionary(word)) {
                recommend_words = dict.recommend(word);
                Scanner scan = new Scanner(System.in);
                String commend = scan.nextLine();
                dict.append(word, commend);
                dict.write_dictionary();
            }
        }

        System.out.println("Bye~");
    }
}

class Dictionary {
    ArrayList dict_array = new ArrayList<String>();

    ArrayList load_dict(String dic_path) {
        /* 从dictionary.txt读取字典中所有的单词，存储到一个动态数组中 */
        File dictFile = new File(dic_path);
        System.out.println(dictFile.isFile());
        System.out.println(dictFile.exists());
        if (dictFile.isFile() && dictFile.exists()) {
            ArrayList dictionary_arraylist = new ArrayList<String>();
            try{
                InputStream in = new FileInputStream(dic_path);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
                String lineTxt = null;
                while((lineTxt = buffer.readLine())!=null){
                dictionary_arraylist.add(lineTxt);
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return dictionary_arraylist;
            }
        ArrayList dictionary_arraylist = new ArrayList<String>();
        dictionary_arraylist.add("aaa");
        return dictionary_arraylist;  
    }
    

    void append(String word, String scanned_commend) {
        /* 将新单词附加入已有的字典动态数组中 */
        switch (scanned_commend) {
        case "1":
            word = this.recommend(word)[0];
            break;

        case "2":
            word = this.recommend(word)[1];
            break;

        case "a":
        case "add":
            break;

        case "t":
        case "tape":
            System.out.println("tape your word");
            Scanner scan = new Scanner(System.in);
            word = scan.nextLine();
            break;
        default:
            /*
             * system.out.println("bad commend!"); word = this.append(word,
             * scanned_commend);
             */
        }
        dict_array.add(word);
    }

    boolean is_in_dictionary(String word) {
        /* 判断某单词是否属于当前字典库中所存在的拼写形式 */        
        return dict_array.contains(word);
    }

    void write_dictionary() {
    }

    String[] recommend(String word) {
        String[] recommend_words = new String[3];
        recommend_words[0] = "a";
        recommend_words[1] = "b";
        recommend_words[2] = "c";
        return recommend_words;
    }
}

class TestTxt {
    ArrayList testtxt_arraylist = new ArrayList<String>();

    ArrayList load_txt(String txt_path) {
        File testFile = new File(txt_path);
        if (testFile.isFile() && testFile.exists()) {
            ArrayList testtxt_arraylist = new ArrayList<String>();
            String encoding = "GBK";

            try{
                InputStreamReader read = new InputStreamReader(new FileInputStream(testFile),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                while((lineTxt = bufferedReader.readLine())!=null){
                testtxt_arraylist.add(lineTxt);
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            


            return testtxt_arraylist;
        }
        return testtxt_arraylist;
    }

    void correct_word(String word) {
    }

    void read_a_word() {
    }
}
