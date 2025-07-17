package com.example.hr_visualization_be.repository;

import com.example.hr_visualization_be.model.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OvertimeViolationRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = """
    
        SET DATEFIRST 1;  -- Bắt đầu tuần từ Thứ Hai
         DECLARE @monthInput VARCHAR(7) = :monthInput;
            
                               DECLARE @year INT = CAST(LEFT(@monthInput, 4) AS INT);
                               DECLARE @month INT = CAST(RIGHT(@monthInput, 2) AS INT);
            
                               -- Khoảng thời gian từ 21 tháng trước đến 20 tháng hiện tại
                               DECLARE @start DATE = DATEFROMPARTS(@year, @month, 21);
                               DECLARE @end DATE = DATEADD(DAY, -1, DATEFROMPARTS(@year, @month + 1, 21));
            
                               -- Cả năm
                               DECLARE @startOfYear DATE = DATEFROMPARTS(@year, 1, 1);
                               DECLARE @endOfYear DATE = DATEFROMPARTS(@year, 12, 31);
            
                               -- 1. Dữ liệu theo tháng
                               WITH CleanData AS (
                                   SELECT
                                       CASE
                                           WHEN d.Dept IN ('Environment Management', 'General Affairs', 'HR', 'IT') THEN 'K Common'
                                           ELSE d.Dept
                                       END AS Dept,
                                       d.Code AS Ma_NV,
                                       TRY_CAST(w.Ngay_cong AS DATE) AS Ngay_cong,
                                       TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) AS Gio_WT,
                                       TRY_CAST(w.Tong_so_gio_OT AS FLOAT) AS Gio_OT,
                                       (TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) + TRY_CAST(w.Tong_so_gio_OT AS FLOAT)) AS Tong_Gio,
                                       DATEADD(DAY, 1 - DATEPART(WEEKDAY, TRY_CAST(w.Ngay_cong AS DATE)), TRY_CAST(w.Ngay_cong AS DATE)) AS StartOfWeek
                                   FROM [F2Database].[dbo].[F2_HR_WT_OT] w
                                   JOIN [F2Database].[dbo].[F2_HR_Data] d
                                       ON w.group_name = d.[Group]
                                      AND w.Ma_NV = d.Code
                                   WHERE
                                       TRY_CAST(w.Ngay_cong AS DATE) BETWEEN @start AND @end
                                       AND TRY_CAST(w.Tong_gio_lam_viec AS FLOAT) IS NOT NULL
                                       AND TRY_CAST(w.Tong_so_gio_OT AS FLOAT) IS NOT NULL
                               ),
            
                               -- Vi phạm > 12h/ngày
                               Over12h AS (
                                   SELECT Dept, COUNT(*) AS Tong_Lan_Vuot_12Gio
                                   FROM CleanData
                                   WHERE Tong_Gio > 12
                                   GROUP BY Dept
                               ),
            
                               -- Vi phạm > 48h/tuần (theo tuần thực tế Thứ 2 - Chủ nhật)
                               WeeklyWork AS (
                                   SELECT
                                       Dept,
                                       Ma_NV,
                                       StartOfWeek,
                                       SUM(Gio_WT) AS Tong_Gio_Trong_Tuan
                                   FROM CleanData
                                   GROUP BY Dept, Ma_NV, StartOfWeek
                               ),
                               Over48h AS (
                                   SELECT Dept, COUNT(*) AS So_Lan_Vuot_48h_Tuan
                                   FROM WeeklyWork
                                   WHERE Tong_Gio_Trong_Tuan > 48
                                   GROUP BY Dept
                               ),
            
                               -- Vi phạm > 40h OT/tháng
                               MonthlyOT AS (
                                   SELECT Dept, Ma_NV, SUM(Gio_OT) AS Tong_Gio_OT_Trong_Thang
                                   FROM CleanData
                                   GROUP BY Dept, Ma_NV
                               ),
                               Over40OT AS (
                                   SELECT Dept, COUNT(*) AS So_NV_Vuot_40h_OT_Thang
                                   FROM MonthlyOT
                                   WHERE Tong_Gio_OT_Trong_Thang > 40
                                   GROUP BY Dept
                               ),
            
                               -- 2. Dữ liệu theo năm (OT từ 200h–299h và trên 300h)
                               YearCleanData AS (
                                   SELECT
                                       CASE
                                           WHEN d.Dept IN ('Environment Management', 'General Affairs', 'HR', 'IT') THEN 'K Common'
                                           ELSE d.Dept
                                       END AS Dept,
                                       d.Code AS Ma_NV,
                                       TRY_CAST(w.Ngay_cong AS DATE) AS Ngay_cong,
                                       TRY_CAST(w.Tong_so_gio_OT AS FLOAT) AS Gio_OT
                                   FROM  [F2Database].[dbo].[F2_HR_WT_OT] w
                                   JOIN [F2Database].[dbo].[F2_HR_Data] d
                                       ON w.group_name = d.[Group]
                                      AND w.Ma_NV = d.Code
                                   WHERE
                                       TRY_CAST(w.Ngay_cong AS DATE) BETWEEN @startOfYear AND @endOfYear
                                       AND TRY_CAST(w.Tong_so_gio_OT AS FLOAT) IS NOT NULL
                               ),
                               YearlyOT AS (
                                   SELECT Dept, Ma_NV, SUM(Gio_OT) AS Tong_Gio_OT_Trong_Nam
                                   FROM YearCleanData
                                   GROUP BY Dept, Ma_NV
                               ),
                               OT200to299 AS (
                                   SELECT Dept, COUNT(*) AS So_NV_OT_Tu_200_Den_299h_Nam
                                   FROM YearlyOT
                                   WHERE Tong_Gio_OT_Trong_Nam >= 200 AND Tong_Gio_OT_Trong_Nam < 300
                                   GROUP BY Dept
                               ),
                               OTTren300 AS (
                                   SELECT Dept, COUNT(*) AS So_NV_OT_Tren_300h_Nam
                                   FROM YearlyOT
                                   WHERE Tong_Gio_OT_Trong_Nam >= 300
                                   GROUP BY Dept
                               )
            
                               -- 3. Tổng hợp
                               SELECT
                                   COALESCE(o12.Dept, o48.Dept, o40.Dept, oYear.Dept, o300.Dept) AS Dept,
                                   ISNULL(o12.Tong_Lan_Vuot_12Gio, 0) AS Tong_Lan_Vuot_12Gio,
                                   ISNULL(o48.So_Lan_Vuot_48h_Tuan, 0) AS So_Lan_Vuot_48h_Tuan,
                                   ISNULL(o40.So_NV_Vuot_40h_OT_Thang, 0) AS So_NV_Vuot_40h_OT_Thang,
                                   ISNULL(oYear.So_NV_OT_Tu_200_Den_299h_Nam, 0) AS So_NV_OT_Tu_200_Den_299h_Nam,
                                   ISNULL(o300.So_NV_OT_Tren_300h_Nam, 0) AS So_NV_OT_Tren_300h_Nam,
                                   @start AS Tu_Ngay,
                                   @end AS Den_Ngay
                               FROM Over12h o12
                               FULL OUTER JOIN Over48h o48 ON o12.Dept = o48.Dept
                               FULL OUTER JOIN Over40OT o40 ON COALESCE(o12.Dept, o48.Dept) = o40.Dept
                               FULL OUTER JOIN OT200to299 oYear ON COALESCE(o12.Dept, o48.Dept, o40.Dept) = oYear.Dept
                               FULL OUTER JOIN OTTren300 o300 ON COALESCE(o12.Dept, o48.Dept, o40.Dept, oYear.Dept) = o300.Dept
            
                               ORDER BY Dept;
            

    """, nativeQuery = true)
    List<Object[]> getOvertime(@Param("monthInput") String month);
}
