package com.cynaith.ifile.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.cynaith.ifile.pojo.domain.Filedata;
import com.cynaith.ifile.pojo.domain.Ifile;
import com.cynaith.ifile.service.FileService;
import com.cynaith.ifile.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraBatchOperations;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

/**
 * @author: Cynaith
 **/

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private CassandraTemplate template;

    @Override
    public boolean saveFile(MultipartFile multipartFile) {
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
        }finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] filebytes= null;
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

        System.out.println(filebytes.length);
        // 开启批处理
//        CassandraBatchOperations batchOps = template.batchOps();
//        batchOps.insert(ifile);
        template.insert(ifile);
        Filedata filedata = new Filedata();
        byte[][] cutByte = new byte[filebytes.length/10240+1][10240];
        for (int i = 0; i < filebytes.length; i++) {
            cutByte[i/10240][i-(i/10240)*10240] = filebytes[i];
            if (i%10240==0&&i>0||i+1==filebytes.length){
                filedata.setFileid(fileid);
                filedata.setFiledata(Arrays.toString(cutByte[i/10240-1]));
                if (i+1!=filebytes.length) filedata.setFileno(i/10240-1);
                else filedata.setFileno(i/10240);
                template.insert(filedata);
//                batchOps.insert(filedata);
//                if ((i/10240)%5==0&&i>10240){
//                    batchOps.execute();
//                }
            }
        }
//        batchOps.execute();



        // 操作完上的文件 需要删除在根目录下生成的文件
        File f = new File(file.toURI());
        if (f.delete()){
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }

        return false;
    }


}
