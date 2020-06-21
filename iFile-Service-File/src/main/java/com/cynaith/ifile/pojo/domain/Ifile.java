package com.cynaith.ifile.pojo.domain;


import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

@Table("file")
public class Ifile implements Serializable {

  private static final long serialVersionUID = 8680836303185822596L;

  @PrimaryKey
  @Column("username")
  private String username;
  @Column("createtime")
  private Date createtime;
  @Column("fileid")
  private String fileid;
  @Column("filename")
  private String filename;
  @Column("filesuffix")
  private String filesuffix;
  @Column("filesize")
  private String filesize;
  @Column("filetype")
  private String filetype;


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public Date getCreatetime() {
    return createtime;
  }

  public void setCreatetime(Date createtime) {
    this.createtime = createtime;
  }

  public String getFileid() {
    return fileid;
  }

  public void setFileid(String fileid) {
    this.fileid = fileid;
  }

  public String getFilesize() {
    return filesize;
  }

  public void setFilesize(String filesize) {
    this.filesize = filesize;
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

}
