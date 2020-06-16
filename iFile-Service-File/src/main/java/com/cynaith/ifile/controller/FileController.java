package com.cynaith.ifile.controller;

import com.cynaith.ifile.pojo.resultVo.ResponseVo;
import com.cynaith.ifile.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author: Cynaith
 **/

@RestController
@CrossOrigin
public class FileController {

    @Autowired
    FileService fileService;


    @RequestMapping(value = "upload")
    public int uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        fileService.saveFile(file);
        return 200;
    }

    @RequestMapping("file")
    public ResponseEntity<Object> readFile(String fileid){
       File file =  fileService.readFile(fileid);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource( new FileInputStream( file ) );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String fileCode=(String)System.getProperties().get("file.encoding");

        String fileName = file.getName();

        try {
            fileName = new String (fileName.getBytes(fileCode),fileCode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //设置中文文件名与后缀
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(fileName,"utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add ( "Content-Disposition",String.format("attachment;filename=\"%s\"",encodedFileName));
        headers.add ( "Cache-Control","no-cache,no-store,must-revalidate" );
        headers.add ( "Pragma","no-cache" );
        headers.add ( "Expires","0" );

        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .headers ( headers )
                .contentLength ( file.length ())
                .contentType(MediaType.valueOf("application/vnd.ms-excel;charset=UTF-8"))
                .body(resource);

        // 操作完上的文件 需要删除在根目录下生成的文件
        File f = new File(file.toURI());
        if (f.delete()) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
        return responseEntity;
    }

    @RequestMapping("filelist")
    public ResponseVo getFileList(String username){
        return ResponseVo.ok("ok",fileService.getFileList(username));
    }
}
