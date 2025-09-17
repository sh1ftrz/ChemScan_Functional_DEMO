package com.example.chemapp30.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ChemicalDao {
    @Insert
    void insert(Chemical chemical);

    @Query("SELECT * FROM Chemical WHERE code = :code LIMIT 1")
    Chemical getChemicalByCode(String code);

    @Query("SELECT * FROM Chemical")
    List<Chemical> getAll();
}
