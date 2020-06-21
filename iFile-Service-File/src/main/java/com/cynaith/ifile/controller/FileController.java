package com.cynaith.ifile.controller;

import cn.hutool.json.JSONString;
import cn.hutool.json.JSONSupport;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.cynaith.ifile.pojo.domain.Ifile;
import com.cynaith.ifile.pojo.resultVo.ResponseVo;
import com.cynaith.ifile.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Cynaith
 **/

@RestController
@CrossOrigin
public class FileController {

    @Autowired
    FileService fileService;


    @RequestMapping(value = "upload")
    public ResponseVo uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        return ResponseVo.ok("ok",fileService.saveFile(file));
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
    public ResponseVo getFileList(@RequestBody String string){
        JSONObject jsonObject = JSONObject.parseObject(string);
        String username = (String) jsonObject.get("username");
        List<Ifile> ifileList = fileService.getFileList(username);
        for (Ifile ifile : ifileList) {
            ifile.setFiletype(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ifile.getCreatetime()));
        }
        return ResponseVo.ok("ok",ifileList);
    }
}
