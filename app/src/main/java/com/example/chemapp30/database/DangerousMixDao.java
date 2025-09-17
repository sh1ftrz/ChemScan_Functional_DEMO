package com.example.chemapp30.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DangerousMixDao {
    @Insert
    void insert(DangerousMix mix);

    @Query("SELECT * FROM DangerousMix WHERE (code1 = :c1 AND code2 = :c2) OR (code1 = :c2 AND code2 = :c1) LIMIT 1")
    DangerousMix getMix(String c1, String c2);
}
