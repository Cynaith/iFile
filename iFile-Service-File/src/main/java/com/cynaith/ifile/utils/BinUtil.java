package com.cynaith.ifile.utils;

import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
/**
 * @author: Cynaith
 **/

public class BinUtil {

    public static void main(String[] args){
        File file = new File("E://ONO白皮书v2.0.pdf");
        String fileName = file.getName();
        binToFile(fileToBinStr(file),fileName,"E://测试byte");
        getFileToByte(file);
    }

    /**
     * 文件转为二进制数组
     * @param file
     * @return
     */
    public static byte[] fileToBinArray(File file){
        try {
            InputStream fis = new FileInputStream(file);
            byte[] bytes = FileCopyUtils.copyToByteArray(fis);
            return bytes;
        }catch (Exception ex){
            throw new RuntimeException("transform file into bin Array 出错",ex);
        }
    }

    /**
     * 文件转为二进制字符串
     * @param file
     * @return
     */
    public static String fileToBinStr(File file){
        try {
            InputStream fis = new FileInputStream(file);
            byte[] bytes = FileCopyUtils.copyToByteArray(fis);
            return new String(bytes,"ISO-8859-1");
        }catch (Exception ex){
            throw new RuntimeException("transform file into bin String 出错",ex);
        }
    }


    /**
     * 二进制字符串转文件
     * @param bin
     * @param fileName
     * @param parentPath
     * @return
     */
    public static File binToFile(String bin,String fileName,String parentPath){
        try {
            File fout = new File(parentPath,fileName);
            fout.createNewFile();
            byte[] bytes1 = bin.getBytes("ISO-8859-1");
            FileCopyUtils.copy(bytes1,fout);

            //FileOutputStream outs = new FileOutputStream(fout);
            //outs.write(bytes1);
            //outs.flush();
            //outs.close();

            return fout;
        }catch (Exception ex){
            throw new RuntimeException("transform bin into File 出错",ex);
        }
    }

    /**
     * 文件转为二进制数组
     * 等价于fileToBin
     * @param file
     * @return
     */
    public static byte[] getFileToByte(File file) {
        byte[] by = new byte[(int) file.length()];
        try {
            InputStream is = new FileInputStream(file);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            byte[] bb = new byte[2048];
            int ch;
            ch = is.read(bb);
            while (ch != -1) {
                bytestream.write(bb, 0, ch);
                ch = is.read(bb);
            }
            by = bytestream.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("transform file into bin Array 出错",ex);
        }
        return by;
    }

}

