package com.example.hr_visualization_be.repository;

import com.example.hr_visualization_be.model.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OvertimeViolationDaily12hRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = """
             DECLARE @monthInput VARCHAR(7) = :monthInput;
        
                        DECLARE @year INT = CAST(LEFT(@monthInput, 4) AS INT);
                        DECLARE @month INT = CAST(RIGHT(@monthInput, 2) AS INT);
        
                        DECLARE @start DATE = DATEFROMPARTS(@year, @month, 21);
                        DECLARE @end DATE = DATEADD(DAY, -1, DATEFROMPARTS(@year, @month + 1, 21));
        
                        WITH CleanData AS (
                            SELECT\s
                                d.Dept,
                                TRY_CAST(w.Ngay_cong AS DATE) AS Ngay_cong,
                                TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) AS Gio_WT,
                                TRY_CAST(w.Tong_so_gio_OT AS FLOAT) AS Gio_OT,
                                (TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) + TRY_CAST(w.Tong_so_gio_OT AS FLOAT)) AS Tong_Gio
                            FROM [F2Database].[dbo].[F2_HR_WT] w
                            JOIN [F2Database].[dbo].[F2_HR_Data] d
                                ON w.group_name = d.[Group]
                               AND w.Ma_NV = d.Code
                            WHERE\s
                                TRY_CAST(w.Ngay_cong AS DATE) BETWEEN @start AND @end
                                AND TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) IS NOT NULL
                                AND TRY_CAST(w.Tong_so_gio_OT AS FLOAT) IS NOT NULL
                                AND (TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) + TRY_CAST(w.Tong_so_gio_OT AS FLOAT)) > 12
                        )
                        SELECT
                            Dept,
                            COUNT(*) AS Tong_Lan_Vuot_12Gio,
                            @start AS Tu_Ngay_ThangLamViec,
                            @end AS Den_Ngay_ThangLamViec
                        FROM CleanData
                        GROUP BY Dept
                        ORDER BY Dept;

        """, nativeQuery = true)
    List<Object[]> getOvertimeDaily12hByMonth(@Param("monthInput") String month);
}
