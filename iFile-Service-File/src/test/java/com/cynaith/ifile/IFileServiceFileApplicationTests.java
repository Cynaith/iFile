package com.cynaith.ifile;

import com.cynaith.ifile.pojo.domain.IFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.util.List;

@SpringBootTest
class IFileServiceFileApplicationTests {

    @Autowired
    CassandraTemplate template;
    @Test
    void contextLoads() {
        List<IFile> file = template.select("select * from file", IFile.class);
        file.forEach(iFile -> {
            System.out.println(iFile.getFileid());
        });
    }

}
