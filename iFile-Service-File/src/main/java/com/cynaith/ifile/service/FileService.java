package com.cynaith.ifile.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Cynaith
 **/
public interface FileService {
    boolean saveFile(MultipartFile file);
}
