package com.cynaith.ifile.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author: Cynaith
 **/
public interface FileService {
    boolean saveFile(MultipartFile file);
    File readFile(String fileid);
}
