package com.github.cglirang.nlp.utils;

/**
 * Created by gefujiang on 2017/6/25.
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class TextReader {

    private String encoding = "utf-8";
    private BufferedReader reader = null;
	private long lineNumber = 0;
	private long textLineCount = 0;

    public TextReader(String path)throws Exception{
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
    }

    public String getLine()throws Exception{
        String line = reader.readLine();
        if(line != null)
            lineNumber++;
        return line;
    }

    public String readLine()throws Exception{
        return getTextLine();
    }
    /***
     * 返回一个文本行，跳过空行
     * */
    public String getTextLine()throws Exception{
        String line = null;
        while((line = reader.readLine()) != null){
            lineNumber++;
            line = line.trim();
            if(line.length()==0)
                continue;
            textLineCount++;
            return line;
        }
        return null;
    }

    ///当前行数
    public long getLineNumber(){return lineNumber;}

    ///当前非空行数
    public long getTextLineCount(){return textLineCount;}

    protected void finalize()throws Exception{
        reader.close();
        reader = null;
    }

    public void close()throws Exception{
        if (reader==null)
            return;
        reader.close();
        reader = null;
    }

}

