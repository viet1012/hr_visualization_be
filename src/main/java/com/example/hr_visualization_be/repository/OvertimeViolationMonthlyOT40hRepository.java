package com.example.hr_visualization_be.repository;

import com.example.hr_visualization_be.model.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OvertimeViolationMonthlyOT40hRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = """
            DECLARE @monthInput VARCHAR(7) = :monthInput;
            DECLARE @year INT = CAST(LEFT(@monthInput, 4) AS INT);
            DECLARE @month INT = CAST(RIGHT(@monthInput, 2) AS INT);
            DECLARE @start DATE = DATEFROMPARTS(@year, @month, 21);
            DECLARE @end DATE = DATEADD(DAY, -1, DATEFROMPARTS(@year, @month + 1, 21));
        
            -- Làm sạch dữ liệu
            WITH CleanData AS (
                SELECT
                    CASE
                        WHEN d.Dept = 'Environment Management' THEN 'K Common'
                        ELSE d.Dept
                    END AS Dept,
                    d.Code AS Ma_NV,
                    TRY_CAST(w.Ngay_cong AS DATE) AS Ngay_cong,
                    TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) AS Gio_WT,
                    TRY_CAST(w.Tong_so_gio_OT AS FLOAT) AS Gio_OT
                FROM [F2Database].[dbo].[F2_HR_WT] w
                JOIN [F2Database].[dbo].[F2_HR_Data] d
                    ON w.group_name = d.[Group]
                   AND w.Ma_NV = d.Code
                WHERE
                    TRY_CAST(w.Ngay_cong AS DATE) BETWEEN @start AND @end
                    AND TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) IS NOT NULL
                    AND TRY_CAST(w.Tong_so_gio_OT AS FLOAT) IS NOT NULL
            ),
            -- Tổng OT theo nhân viên trong tháng
            MonthlyOT AS (
                SELECT
                    Dept,
                    Ma_NV,
                    SUM(Gio_OT) AS Tong_Gio_OT_Trong_Thang
                FROM CleanData
                GROUP BY Dept, Ma_NV
            ),
            -- Lọc những người vi phạm OT > 40h/tháng
            ViPham AS (
                SELECT
                    Dept,
                    Ma_NV,
                    Tong_Gio_OT_Trong_Thang
                FROM MonthlyOT
                WHERE Tong_Gio_OT_Trong_Thang > 40
            )

            -- Tổng hợp số người vi phạm theo phòng ban
            SELECT
                Dept,
                COUNT(*) AS So_Luong_NV_OT_Vuot_40h_Thang,
                @start AS Tu_Ngay,
                @end AS Den_Ngay
            FROM ViPham
            GROUP BY Dept
            ORDER BY Dept;

        """, nativeQuery = true)
    List<Object[]> getOvertimeMonthly40hByMonth(@Param("monthInput") String month);
}
