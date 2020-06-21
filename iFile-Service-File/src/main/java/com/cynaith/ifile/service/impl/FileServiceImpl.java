package com.cynaith.ifile.service.impl;

import cn.hutool.core.util.IdUtil;
import com.cynaith.ifile.pojo.domain.Filedata;
import com.cynaith.ifile.pojo.domain.Ifile;
import com.cynaith.ifile.service.FileService;
import com.cynaith.ifile.utils.FileUtil;
import com.github.houbb.sisyphus.annotation.annotation.Retry;
import com.github.houbb.sisyphus.annotation.annotation.RetryWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: Cynaith
 **/

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private CassandraTemplate template;

    @Override
    public Ifile saveFile(MultipartFile multipartFile) {
//        获取文件名、后缀
        String fileName = multipartFile.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String fileid = IdUtil.randomUUID();
        File file = new File(fileName);
//        MultipartFile转File
        OutputStream out = null;
        try {
            InputStream in = multipartFile.getInputStream();
            out = new FileOutputStream(file);
            byte[] bytes = multipartFile.getBytes();
            for (int i = 0; i < bytes.length; i++) {
                out.write(bytes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] filebytes = null;
        try {
            filebytes = FileUtil.getBytes(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Ifile ifile = new Ifile();
        ifile.setUsername("Cynaith");
        ifile.setCreatetime(new Date());
        ifile.setFileid(fileid);
        ifile.setFilesuffix(suffixName);
        ifile.setFiletype(suffixName);
        ifile.setFilename(fileName);
        ifile.setFilesize(FileUtil.getPrintSize(file));
//        System.out.println(filebytes.length);
        template.insert(ifile);
        Filedata filedata = new Filedata();
        byte[][] cutByte = new byte[filebytes.length / 10240 + 1][10240];
        for (int i = 0; i < filebytes.length; i++) {
            cutByte[i / 10240][i - (i / 10240) * 10240] = filebytes[i];
            if ((i % 10240 == 0 && i > 0) || i + 1 == filebytes.length) {
                filedata.setFileid(fileid);

                if (i + 1 != filebytes.length) {
                    filedata.setFileno(i / 10240 - 1);
                    filedata.setFiledata(Arrays.toString(cutByte[i / 10240 - 1]));
                } else {
//                    去除结尾 0
                    byte[] temp = new byte[i - i / 10240 * 10240 + 1];
                    System.arraycopy(cutByte[i / 10240], 0, temp, 0, i - i / 10240 * 10240 + 1);
                    filedata.setFileno(i / 10240);
                    filedata.setFiledata(Arrays.toString(temp));
                }

                template.insert(filedata);
            }
        }
//        System.out.println(Arrays.toString(filebytes));
//        File file2 = new File("/Users/admin/Desktop/test/" + fileName);
//        File file1 = FileUtil.getFile(filebytes, file2);

        // 操作完上的文件 需要删除在根目录下生成的文件
        File f = new File(file.toURI());
        if (f.delete()) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }

        // 将时间放至Type中存放
        ifile.setFiletype(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ifile.getCreatetime()));
        return ifile;
    }

    @Override
    public File readFile(String fileid) {
        List<Filedata> filedataList = template.select("select * from ifile.filedata where fileid = \'" + fileid + "\'", Filedata.class);
        // question : Query输出为字节数组的String
        char[] filebytes = new char[filedataList.size() * 10242 * 4];
        String filename = null;

        int count = 0;
        for (Filedata filedata : filedataList) {
//            获取当前part的数据
            String filestring = filedata.getFiledata();
//            filestring --> string[]
//            获取每一段字节数组的ASCII 码
            /**
             * 1. 提取数据库中ASCII编码数组转换为String
             * 先操作输入流AscII 将ASCII[] 转换为 string[]
             * string[] 为数据库文本格式，去除'[' ']' ' '
             * string[] 为字节数组
             * string[] 转换为 char[]
             *
             * 2. ASCII编码数组转换为 byte[]
             */
            String[] strings = filestring.replace('[', ' ').replace(']', ' ').trim().split(",");

            for (int j = 0; j < strings.length; j++) {
                int stringToInt = (int) Integer.parseInt(strings[j].trim());
                if (stringToInt == 32) {

                } else {
                    filebytes[count] = (char) stringToInt;
                    count++;
                }


            }

        }
//        ASCII 转码
        String newStrings = new String(filebytes);
        String[] newbytes = newStrings.replace("][", ",").replace('[', ' ').replace(']', ' ').trim().split(",");

//
        byte[] finalbytes = new byte[newbytes.length];
        for (int i = 0, j = 0; i < finalbytes.length; i++) {
            try {
                finalbytes[j] = (byte) Integer.parseInt(newbytes[i].trim());
            } catch (NumberFormatException e) {
                continue;
            }
            j++;
        }

//        获取用户所有文件
        List<Ifile> ifiles = template.select("select * from ifile.file where username = \'Cynaith\'", Ifile.class);
        for (Ifile ifile : ifiles) {
            if (ifile.getFileid().equals(fileid)) {
                filename = ifile.getFilename();
                break;
            }
        }
//        System.out.println(Arrays.toString(finalbytes));

        File file = new File("./" + filename);
        return FileUtil.getFile(finalbytes, file);
    }


    @Override
    public List<Ifile> getFileList(String username) {
        List<Ifile> ifileList = template.select("select * from ifile.file where username = \'" + username + "\'", Ifile.class);
        return ifileList;
    }
}