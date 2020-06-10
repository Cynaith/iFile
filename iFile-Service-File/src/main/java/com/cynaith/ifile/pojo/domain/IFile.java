package com.cynaith.ifile.pojo.domain;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Table("file")
public class IFile {

  @PrimaryKey
  private long fileid;
  private LocalDate createtime;
  private String filedata;
  private String filename;
  private String filesuffix;
  private String filetype;
  private long userid;


  public long getFileid() {
    return fileid;
  }

  public void setFileid(long fileid) {
    this.fileid = fileid;
  }

  public LocalDate getCreatetime() {
    return createtime;
  }

  public void setCreatetime(LocalDate createtime) {
    this.createtime = createtime;
  }



  public String getFiledata() {
    return filedata;
  }

  public void setFiledata(String filedata) {
    this.filedata = filedata;
  }


  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }


  public String getFilesuffix() {
    return filesuffix;
  }

  public void setFilesuffix(String filesuffix) {
    this.filesuffix = filesuffix;
  }


  public String getFiletype() {
    return filetype;
  }

  public void setFiletype(String filetype) {
    this.filetype = filetype;
  }


  public long getUserid() {
    return userid;
  }

  public void setUserid(long userid) {
    this.userid = userid;
  }

}
