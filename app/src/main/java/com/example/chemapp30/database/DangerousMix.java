package com.example.chemapp30.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DangerousMix {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String code1;
    private String code2;
    private String warning;

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCode1() { return code1; }
    public void setCode1(String code1) { this.code1 = code1; }
    public String getCode2() { return code2; }
    public void setCode2(String code2) { this.code2 = code2; }
    public String getWarning() { return warning; }
    public void setWarning(String warning) { this.warning = warning; }
}
