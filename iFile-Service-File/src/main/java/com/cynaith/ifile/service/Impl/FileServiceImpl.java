package com.cynaith.ifile.service.Impl;

import com.cynaith.ifile.pojo.domain.IFile;
import com.cynaith.ifile.service.FileService;
import org.springframework.data.cassandra.core.CassandraTemplate;

/**
 * @author: Cynaith
 **/
public class FileServiceImpl implements FileService {
    private CassandraTemplate template;

    @Override
    public boolean saveFile() {
        System.out.println(template.select("select * from file", IFile.class));
        return false;
    }
}
