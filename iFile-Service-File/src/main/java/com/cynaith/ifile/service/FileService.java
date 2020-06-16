package com.cynaith.ifile.service;

import com.cynaith.ifile.pojo.domain.Ifile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author: Cynaith
 **/
public interface FileService {
    boolean saveFile(MultipartFile file);
    File readFile(String fileid);
    List<Ifile> getFileList(String username);
}
