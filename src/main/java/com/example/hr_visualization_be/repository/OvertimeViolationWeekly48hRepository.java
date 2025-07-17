package com.example.hr_visualization_be.repository;

import com.example.hr_visualization_be.model.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OvertimeViolationWeekly48hRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = """
          DECLARE @monthInput VARCHAR(7) = :monthInput;
          DECLARE @year INT = CAST(LEFT(@monthInput, 4) AS INT);
          DECLARE @month INT = CAST(RIGHT(@monthInput, 2) AS INT);
          DECLARE @start DATE = DATEFROMPARTS(@year, @month, 21);
          DECLARE @end DATE = DATEADD(DAY, -1, DATEFROMPARTS(@year, @month + 1, 21));

          -- Làm sạch dữ liệu và tính tổng giờ làm (cả giờ làm chính thức và OT)
          WITH CleanData AS (
              SELECT\s
                  CASE\s
                      WHEN d.Dept = 'Environment Management' THEN 'K Common'
                      ELSE d.Dept\s
                  END AS Dept,
                  d.Code AS Ma_NV,
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
          ),
          -- Tính tổng giờ mỗi tuần
          WeeklyWork AS (
              SELECT\s
                  Dept,
                  Ma_NV,
                  DATEPART(YEAR, Ngay_cong) AS Nam,
                  DATEPART(WEEK, Ngay_cong) AS Tuan,
                  SUM(Tong_Gio) AS Tong_Gio_Trong_Tuan
              FROM CleanData
              GROUP BY Dept, Ma_NV, DATEPART(YEAR, Ngay_cong), DATEPART(WEEK, Ngay_cong)
          ),
          -- Lọc các tuần vi phạm > 48 giờ
          ViPham AS (
              SELECT Dept, Ma_NV, Nam, Tuan, Tong_Gio_Trong_Tuan
              FROM WeeklyWork
              WHERE Tong_Gio_Trong_Tuan > 48
          )

          -- Tổng hợp theo phòng ban
          SELECT\s
              Dept,
              COUNT(*) AS So_Lan_Vuot_48h_Tuan,
              @start AS Tu_Ngay,
              @end AS Den_Ngay
          FROM ViPham
          GROUP BY Dept
          ORDER BY Dept;
        
        """, nativeQuery = true)
    List<Object[]> getOvertimeWeekly48hByMonth(@Param("monthInput") String month);
}
