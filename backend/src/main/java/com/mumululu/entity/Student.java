package com.mumululu.entity;

import java.time.LocalDateTime;

public class Student {

    private Long id;
    private String studentNo;
    private String name;
    private Integer gender;
    private Long classId;
    private String phone;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isDeleted;

    // 关联字段，非数据库列
    private String className;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
