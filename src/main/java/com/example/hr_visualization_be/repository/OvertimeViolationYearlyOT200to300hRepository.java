package com.example.hr_visualization_be.repository;

import com.example.hr_visualization_be.model.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OvertimeViolationYearlyOT200to300hRepository  extends JpaRepository<DummyEntity, Long> {
    @Query(value = """
              DECLARE @monthInput VARCHAR(7) = :monthInput;
              DECLARE @year INT = CAST(LEFT(@monthInput, 4) AS INT);

              -- Tính phạm vi từ 01-01 đến 31-12 của năm đó
              DECLARE @startOfYear DATE = DATEFROMPARTS(@year, 1, 1);
              DECLARE @endOfYear DATE = DATEFROMPARTS(@year, 12, 31);

              -- Làm sạch dữ liệu
              WITH CleanData AS (
                  SELECT
                      CASE\s
                          WHEN d.Dept IN ('Environment Management', 'General Affairs', 'HR', 'IT') THEN 'K Common'
                          ELSE d.Dept
                      END AS Dept,
                      d.Code AS Ma_NV,
                      TRY_CAST(w.Ngay_cong AS DATE) AS Ngay_cong,
                      TRY_CAST(w.Tong_so_gio_OT AS FLOAT) AS Gio_OT
                  FROM [F2Database].[dbo].[F2_HR_WT] w
                  JOIN [F2Database].[dbo].[F2_HR_Data] d
                      ON w.group_name = d.[Group]
                     AND w.Ma_NV = d.Code
                  WHERE
                      TRY_CAST(w.Ngay_cong AS DATE) BETWEEN @startOfYear AND @endOfYear
                      AND TRY_CAST(w.Tong_so_gio_OT AS FLOAT) IS NOT NULL
              ),

              -- Tính tổng OT theo nhân viên trong năm
              YearlyOT AS (
                  SELECT
                      Dept,
                      Ma_NV,
                      SUM(Gio_OT) AS Tong_Gio_OT_Trong_Nam
                  FROM CleanData
                  GROUP BY Dept, Ma_NV
              ),

              -- Lọc những người có tổng OT từ 200h đến dưới 300h
              ViPham AS (
                  SELECT
                      Dept,
                      Ma_NV,
                      Tong_Gio_OT_Trong_Nam
                  FROM YearlyOT
                  WHERE Tong_Gio_OT_Trong_Nam >= 200 AND Tong_Gio_OT_Trong_Nam < 300
              )

              -- Tổng hợp theo phòng ban
              SELECT
                  Dept,
                  COUNT(*) AS So_Luong_NV_OT_Tu_200_Den_299h_Nam,
                  @startOfYear AS Tu_Ngay,
                  @endOfYear AS Den_Ngay
              FROM ViPham
              GROUP BY Dept
              ORDER BY Dept;


        """, nativeQuery = true)
    List<Object[]> getOvertimeYearlyOT200to300hByMonth(@Param("monthInput") String month);
}
