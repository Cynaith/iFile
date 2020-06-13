package com.cynaith.ifile;

import com.cynaith.ifile.pojo.domain.Ifile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class IFileServiceIfileApplicationTests {

    @Autowired
    CassandraTemplate cassandraTemplate;
    @Test
    void contextLoads() {
        List<Ifile> list = new ArrayList<>();
        list = cassandraTemplate.select("select * from file",Ifile.class);
        list.forEach(ifile -> {
            System.out.println(ifile.getFileid());
        });
    }

}
