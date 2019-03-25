import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat.Encoding;

import com.sun.xml.internal.bind.v2.runtime.output.Encoded;

import java.util.*;

public class WordCorrection {
    public static void main(String[] args) {
        Dictionary dict = new Dictionary();
        TestTxt testtxt = new TestTxt();
        //String[] recommend_words;
        //WordCorrection/englishDictionary.txt
        String dict_path = "WordCorrection/englishDictionary.txt", test_path = "WordCorrection/test.txt";

        dict.dict_array = dict.load_dict(dict_path);
        testtxt.testtxt_arraylist = testtxt.load_txt(test_path);

        for (int i = 0; i < testtxt.testtxt_arraylist.size(); i++) {
            String word = (String) testtxt.testtxt_arraylist.get(i);
            // dict.dict_array = dict.load_dict(dict_path);
            if (!dict.is_in_dictionary(word)) {
                dict.recommend_tips(word);
                Scanner scan = new Scanner(System.in);
                String commend = scan.nextLine();
                String choosen = dict.append(word, commend);
                testtxt.testtxt_arraylist.set(i, choosen);
            }
        }
        dict.write_dictionary("WordCorrection/TempDictionary.txt");
        testtxt.write_corrected_txt("WordCorrection/Temptest.txt");
        System.out.println("Bye~");
    }
}

class Dictionary {
    ArrayList dict_array = new ArrayList<String>();
    ArrayList dict_vector = new ArrayList<int[]>();

    ArrayList load_dict(String dic_path) {
        /* 从dictionary.txt读取字典中所有的单词，存储到一个动态数组中 */
        //File dictFile = new File(dic_path);
        ArrayList dictionary_arraylist = new ArrayList<String>();
        try {
            InputStream in = new FileInputStream(dic_path);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
            String lineTxt = null;
            while ((lineTxt = buffer.readLine()) != null) {
                dictionary_arraylist.add(lineTxt);
                dict_vector.add(get_vector(lineTxt));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary_arraylist;
    }

    String append(String word, String scanned_commend) {
        /* 将新单词附加入已有的字典动态数组中 */
        switch (scanned_commend) {
        case "1":
            word = this.recommend(word)[0];
            break;
        case "2":
            word = this.recommend(word)[1];
            break;
        case "3":
            word = this.recommend(word)[2];
            break;
        case "a":
        case "add":
            dict_array.add(word);
            dict_vector.add(get_vector(word));
            break;
        case "t":
        case "tape":
            System.out.println("Tape your word");
            Scanner scan = new Scanner(System.in);
            word = scan.nextLine();
            dict_array.add((String)word);
            dict_vector.add(get_vector(word));
            break;
        default:
            /*
             * system.out.println("bad commend!"); word = this.append(word,
             * scanned_commend);
             */
        }
        String choosen = word;
        return choosen;
    }

    boolean is_in_dictionary(String word) {
        /* 判断某单词是否属于当前字典库中所存在的拼写形式 */
        return dict_array.contains(word);
    }

    void write_dictionary(String dict_path) {
        try {
            PrintWriter pw = new PrintWriter(dict_path);

            for (int i = 0; i < dict_array.size(); i++) {
                pw.write((String) dict_array.get(i));
                pw.write("\n");
            }
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void recommend_tips(String word) {
        System.out.println(word + "：此词不在词典里，请输入指令进行操作");
        String[] recommend_words = new String[3];
        recommend_words = this.recommend(word);
        System.out.println("1.使用 \"" + recommend_words[0] + "\" 进行替换");
        System.out.println("2.使用 \"" + recommend_words[1] + "\" 进行替换");
        System.out.println("3.使用 \"" + recommend_words[2] + "\" 进行替换");
        System.out.println("输入 a/add 将改词加入字典");
        System.out.println("输入 t/tape 进行自定义替换");
    }

    String[] recommend(String word) {
        String[] recommend_words = new String[3];
        int[] rank_pos = {0,0,0};
        int[] rank_value = {0,0,0};
        int[] wrong_word_vector = get_vector(word);
        for(int index=0;index<dict_vector.size();index++){
            int similarity = 0;
            for(int i = 0;i<26;i++){
                similarity += ((int[])dict_vector.get(index))[i]* wrong_word_vector[i];
                if(similarity>rank_value[0]){
                    rank_pos[2] = rank_pos[1];
                    rank_value[2] = rank_value[1];
                    rank_pos[1] = rank_pos[0];
                    rank_value[1] = rank_value[0];
                    rank_pos[0] = index;
                    rank_value[0] = similarity;
                }else if(similarity>rank_value[1]){
                    rank_pos[2] = rank_pos[1];
                    rank_value[2] = rank_value[1];
                    rank_pos[1] = index;
                    rank_value[1] = similarity;
                }else if(similarity>rank_value[2]){
                    rank_pos[2] = index;
                    rank_value[2] = similarity;
                }
            }
        }
        for(int i = 0;i<3;i++){
            recommend_words[i] = (String) dict_array.get(rank_pos[i]);
        }
        return recommend_words;
    }

    int[] get_vector(String word) {
        int[] vector = new int[26];
        Arrays.fill(vector, 0);
        char[] splited = word.toCharArray();
        for (int i = 0; i < splited.length; i++) {
            if (Integer.valueOf(splited[i]) > 96 && Integer.valueOf(splited[i]) < 123) {
                vector[(Integer.valueOf(splited[i]) - 97)]++;
            } else if (Integer.valueOf(splited[i]) < 91 && Integer.valueOf(splited[i]) > 64) {
                vector[(Integer.valueOf(splited[i]) - 65)]++;
            }
        }
        return vector;
    }
}

class TestTxt {
    ArrayList testtxt_arraylist = new ArrayList<String>();

    ArrayList load_txt(String txt_path) {
        File testFile = new File(txt_path);
        ArrayList testtxt_arraylist = new ArrayList<String>();
        String encoding = "GBK";

        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(testFile), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                testtxt_arraylist.add(lineTxt);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testtxt_arraylist;
    }

    void correct_word(String word) {
    }

    void write_corrected_txt(String corrected_txt_path) {
        try {
            PrintWriter pw = new PrintWriter(corrected_txt_path);

            for (int i = 0; i < testtxt_arraylist.size(); i++) {
                pw.write((String) this.testtxt_arraylist.get(i));
                pw.write("\n");
            }
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
