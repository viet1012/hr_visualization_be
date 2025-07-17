package com.example.hr_visualization_be.repository.detail;

import com.example.hr_visualization_be.model.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OvertimeViolationDaily12hDetailRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = """
        DECLARE @monthInput VARCHAR(7) = :monthInput;
        DECLARE @dept NVARCHAR(100) = :dept;

        DECLARE @year INT = CAST(LEFT(@monthInput, 4) AS INT);
        DECLARE @month INT = CAST(RIGHT(@monthInput, 2) AS INT);

        DECLARE @start DATE = DATEFROMPARTS(@year, @month, 21);
        DECLARE @end DATE = DATEADD(DAY, -1, DATEFROMPARTS(@year, @month + 1, 21));

        WITH CleanData AS (
            SELECT
                d.Dept,
                d.Code AS Ma_NV,
                d.Name AS Ten_NV,
                d.[Group] AS Group_Name,
                TRY_CAST(w.Ngay_cong AS DATE) AS Ngay_cong,
                TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) AS Gio_WT,
                TRY_CAST(w.Tong_so_gio_OT AS FLOAT) AS Gio_OT,
                (TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) + TRY_CAST(w.Tong_so_gio_OT AS FLOAT)) AS Tong_Gio
            FROM [F2Database].[dbo].[F2_HR_WT_OT] w
            JOIN [F2Database].[dbo].[F2_HR_Data] d
                ON w.group_name = d.[Group]
               AND w.Ma_NV = d.Code
            WHERE
                TRY_CAST(w.Ngay_cong AS DATE) BETWEEN @start AND @end
                AND TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) IS NOT NULL
                AND TRY_CAST(w.Tong_so_gio_OT AS FLOAT) IS NOT NULL
                AND (TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) + TRY_CAST(w.Tong_so_gio_OT AS FLOAT)) > 12
                AND d.Dept = @dept
        )
        SELECT Dept, Group_Name, Ma_NV, Ten_NV, Ngay_cong, Gio_WT, Gio_OT, Tong_Gio
        FROM CleanData
        ORDER BY Ma_NV, Ngay_cong;
        """, nativeQuery = true)
    List<Object[]> getOvertime12HDetailByDeptAndMonth(@Param("monthInput") String monthInput, @Param("dept") String dept);
}
