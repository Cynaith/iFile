package com.cynaith.ifile.pojo.domain;


import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("filedata")
public class Filedata {

  @PrimaryKey
  private String fileid;
  @Column("fileno")
  private long fileno;
  @Column("filedata")
  private String filedata;


  public String getFileid() {
    return fileid;
  }

  public void setFileid(String fileid) {
    this.fileid = fileid;
  }


  public long getFileno() {
    return fileno;
  }

  public void setFileno(long fileno) {
    this.fileno = fileno;
  }


  public String getFiledata() {
    return filedata;
  }

  public void setFiledata(String filedata) {
    this.filedata = filedata;
  }

}
