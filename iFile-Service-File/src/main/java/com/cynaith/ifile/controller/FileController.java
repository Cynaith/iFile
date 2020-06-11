package com.cynaith.ifile.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author: Cynaith
 **/

@RestController
public class FileController {

    @CrossOrigin
    @RequestMapping(value = "upload")
    public int uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename()+":"+ Arrays.toString(file.getBytes()));
        return 200;
    }
}
