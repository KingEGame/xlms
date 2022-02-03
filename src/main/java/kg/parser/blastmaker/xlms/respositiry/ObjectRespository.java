package kg.parser.blastmaker.xlms.respositiry;

import kg.parser.blastmaker.xlms.nowaday.PerMonth;
import kg.parser.blastmaker.xlms.objects.Object;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

public interface ObjectRespository extends JpaRepository<Object, Long> {

 @Modifying
 @Query(value = "SELECT * FROM object u WHERE u.type_of_work = ?1 order by u.time_of_come_loading", nativeQuery = true)
 List<Object> findObjectByTypeOfWork(String typeofwork);

 @Query(value = "select sum(distance) as distance, sum(abs(gas_for_begin_loading-gas_for_begin_unloading)) as waste_gas, sum(weight_fact) as weight_fact, sum(weight_norm) as weight_norm from object where date(time_of_come_loading) = date(?1)", nativeQuery = true)
List<PerMonth> objects (Timestamp time);

 @Modifying
 @Query(value = "SELECT * FROM object u WHERE u.type_of_work = ?1 and date(u.time_of_come_loading) = date(?2) order by u.time_of_come_loading", nativeQuery = true)
 List<Object> findObjectByTypeOfWorkAndTime(String typeofwork, Timestamp date);

 @Modifying
 @Query(value = "SELECT * FROM object u order by u.time_of_come_loading", nativeQuery = true)
 List<Object> findAllOrderBy();

 @Modifying
 @Query(value = "SELECT * FROM object u  WHERE date(u.time_of_come_loading) = date(?1) order by u.time_of_come_loading", nativeQuery = true)
 List<Object> findByTimeOfComeLoading( Timestamp time_of_come_loading2);

 @Modifying
 @Query(value = "SELECT * FROM object WHERE date(time_of_come_loading) = date(?1) and num_samosval = ?2 order by reise", nativeQuery = true)
 List<Object> findByTimeOfComeLoadingaAndNumSamosval( Timestamp time_of_come_loading2,Integer num_samosval);

 @Modifying
 @Query(value = "select  date(time_of_come_loading) as d from object group by d order by d", nativeQuery = true)
 List<Date> findAllDate();

 @Modifying
 @Query(value = "SELECT count(num_samosval) FROM object WHERE date(time_of_come_loading) = ?1 and type_samosval = ?2 group by num_samosval", nativeQuery = true)
 List<Integer> getCountSamosvalbyDateAndType( Timestamp time_of_come_loading2,  String type_samosval);

 @Modifying
 @Query(value = "select type_of_work from object group by type_of_work", nativeQuery = true)
 List<String> getAllTypeOfWork();

 @Modifying
 @Query(value = "select num_samosval from object where date(time_of_come_loading) = date(?1) group by num_samosval", nativeQuery = true)
 List<Integer> getNums(Timestamp date);

 @Query(value = "select max(reise) from object where date(time_of_come_loading) = ?1 and num_samosval = ?2", nativeQuery = true)
 Integer getMaxReise(Timestamp date, Integer num);

 @Query(value = "SELECT name_ex FROM object WHERE date(time_of_come_loading) = ?1 and num_ex = ?2 group by name_ex", nativeQuery = true)
    List<String> findCountEx(Timestamp date, String ex);

 @Query(value = "select count(reise) from object where date(time_of_come_loading) = date(?1) group by reise", nativeQuery = true)
 List<Integer> findbyReise(Timestamp time);

 @Query(value = "select name_ex from object where date(time_of_come_loading) = ?1 group by name_ex" , nativeQuery = true)
 List<String> AllNameExByDay(Timestamp time);

 @Query(value = "select * from object  where date(time_of_come_loading) = ?1 and name_ex = ?2 order by time_of_come_loading" , nativeQuery = true)
 List<Object> findAllObjectByEx(Timestamp time, String name);

 @Query(value = "select type_samosval from object where date(time_of_come_loading) = ?1 and name_ex = ?2 group by type_samosval" , nativeQuery = true)
 List<String> findAllTypeSamsvalByNameEx(Timestamp time, String name);

 @Query(value = "select count(num_samosval) from object where date(time_of_come_loading) = ?1 and name_ex = ?2 and type_samosval = ?3 group by num_samosval" , nativeQuery = true)
 List<Integer> findQuantitySamByEx(Timestamp time, String name, String type);

 @Query(value ="select num_ex from object group by num_ex", nativeQuery = true)
 List<String> getAllTypeExs();

 @Query(value = "select type_samosval from object group by type_samosval",nativeQuery = true )
 List<String> getAllTypeSamosval();




 // optimize work
 @Query(value = "select round(cast(sum(distance) as numeric), 5) as distance,\n" +
                "       sum(abs(gas_for_begin_loading-gas_for_begin_unloading)) as waste_gas,\n" +
                "       sum(weight_fact) as weight_fact, round(cast(sum(weight_norm) as numeric), 5) as weight_norm\n" +
                "from object\n" +
                "group by date(time_of_come_loading)",nativeQuery = true )
 List<String[]> getMonth();

 @Query(value = "select round(cast(sum(distance) as numeric), 5) as distance,\n" +
                "       sum(abs(gas_for_begin_loading-gas_for_begin_unloading)) as waste_gas,\n" +
                "       sum(weight_fact) as weight_fact," +
                "       round(cast(sum(weight_norm) as numeric), 5) as weight_norm\n" +
                "from object\n" +
                "where type_of_work = ?1\n" +
                "group by date(time_of_come_loading)",nativeQuery = true )
 List<String[]> getMonthByTypeOfWork(String type);


 @Query(value = "select name_ex,\n" +
                "       num_ex,\n" +
                "       type_samosval,\n" +
                "       sum(weight_fact) weight_fact,\n" +
                "       round(cast(sum(weight_norm) as numeric), 5) weight_norm,\n" +
                "       round(cast(sum(distance) as numeric),5) distance,\n" +
                "       sum(abs(gas_for_begin_unloading-gas_for_begin_loading)) waste_gas\n" +
                "from object\n" +
                "where date(time_of_come_loading) = date(?1)\n" +
                "group by name_ex, num_ex, type_samosval",nativeQuery = true )
 List<String[]> getByExesByDay(Timestamp time);

 @Query(value = "select name_ex,\n" +
                "       num_ex,\n" +
                "       type_samosval,\n" +
                "       sum(weight_fact) weight_fact,\n" +
                "       round(cast(sum(weight_norm) as numeric), 5) weight_norm,\n" +
                "       round(cast(sum(distance) as numeric),5) distance,\n" +
                "       sum(abs(gas_for_begin_unloading-gas_for_begin_loading)) waste_gas\n" +
                "from object\n" +
                "where date(time_of_come_loading) = date(?1)\n" +
                "      and type_of_work = ?2\n" +
                "group by name_ex, num_ex, type_samosval",nativeQuery = true )
 List<String[]> getByExesByDayByTypeOfWork(Timestamp time, String type);

 @Query(value = "select num_samosval,\n" +
                "       type_samosval,\n" +
                "       num_ex,\n" +
                "       (select count(reise)\n" +
                "       from object c\n" +
                "       where c.num_samosval = g.num_samosval\n" +
                "         and date(c.time_of_come_loading) = ?1) as count_reise,\n" +
                "       round(cast((select sum(b.distance / (extract(epoch from (b.time_of_begin_unloading - b.time_of_come_loading)) /\n" +
                "                                            3600)::double precision) / count(b.reise)\n" +
                "                   from object b\n" +
                "                   where date(b.time_of_come_loading) = '2019-11-01'\n" +
                "                     and b.num_samosval = g.num_samosval) as numeric), 5) as speed,\n" +
                "       sum(weight_fact) weight_fact,\n" +
                "       round(cast(sum(weight_norm) as numeric), 5) weight_norm,\n" +
                "       round(cast(sum(distance) as numeric),5) distance,\n" +
                "       sum(abs(gas_for_begin_unloading-gas_for_begin_loading)) waste_gas\n" +
                "from object g\n" +
                "where date(time_of_come_loading) = ?1\n" +
                "group by num_samosval, type_samosval, num_ex;",nativeQuery = true )
 List<String[]> getBySumosvalByDay(Timestamp time);



 @Query(value = "select num_samosval,\n" +
         "       type_samosval,\n" +
         "       num_ex,\n" +
         "       (select count(reise)\n" +
         "       from object c\n" +
         "       where c.num_samosval = g.num_samosval\n" +
         "         and date(c.time_of_come_loading) = ?1) as count_reise,\n" +
         "       round(cast((select sum(b.distance / (extract(epoch from (b.time_of_begin_unloading - b.time_of_come_loading)) /\n" +
         "                                            3600)::double precision) / count(b.reise)\n" +
         "                   from object b\n" +
         "                   where date(b.time_of_come_loading) = '2019-11-01'\n" +
         "                     and b.num_samosval = g.num_samosval) as numeric), 5) as speed,\n" +
         "       sum(weight_fact) weight_fact,\n" +
         "       round(cast(sum(weight_norm) as numeric), 5) weight_norm,\n" +
         "       round(cast(sum(distance) as numeric),5) distance,\n" +
         "       sum(abs(gas_for_begin_unloading-gas_for_begin_loading)) waste_gas\n" +
         "from object g\n" +
         "where date(time_of_come_loading) = ?1\n" +
         "      and type_of_work = ?2\n" +
         "group by num_samosval, type_samosval, num_ex;",nativeQuery = true )
 List<String[]> getBySumosvalByDayByTypeOfWork(Timestamp time, String type);

 @Query(value = "select reise,\n" +
                "       round(cast((distance / (extract(epoch from (time_of_begin_unloading - time_of_come_loading)) /\n" +
                "                               3600)::double precision) as numeric), 5) as speed,\n" +
                "       round(cast(((extract(epoch from (time_of_begin_unloading - time_of_come_loading)) /\n" +
                "                    3600)::double precision) as numeric), 5) as time_inHour,\n" +
                "       (time_of_begin_unloading-object.time_of_come_loading)::time as time_inTime,\n" +
                "       abs(gas_for_begin_unloading-object.gas_for_begin_loading) as waste,\n" +
                "       distance\n" +
                "from object\n" +
                "where date(time_of_come_loading) = ?1\n" +
                "      and type_of_work = ?3\n" +
                "      and num_samosval = ?2",nativeQuery = true )
 List<String[]> getDataByDayByNumsumByTypeOfWork(Timestamp time, Integer num, String type);


 @Query(value = "select reise,\n" +
         "       round(cast((distance / (extract(epoch from (time_of_begin_unloading - time_of_come_loading)) /\n" +
         "                               3600)::double precision) as numeric), 5) as speed,\n" +
         "       round(cast(((extract(epoch from (time_of_begin_unloading - time_of_come_loading)) /\n" +
         "                    3600)::double precision) as numeric), 5) as time_inHour,\n" +
         "       (time_of_begin_unloading-object.time_of_come_loading)::time as time_inTime,\n" +
         "       abs(gas_for_begin_unloading-object.gas_for_begin_loading) as waste,\n" +
         "       distance\n" +
         "from object\n" +
         "where date(time_of_come_loading) = ?1\n" +
         "      and num_samosval = ?2",nativeQuery = true )
 List<String[]> getDataByDayByNumsum(Timestamp time, Integer num);

 @Query(value = "SELECT *\n" +
         "FROM object g\n" +
         "WHERE date(g.time_of_come_loading) = date(?1)\n" +
         "    and g.num_samosval = (select o.num_samosval\n" +
         "                     from object o\n" +
         "                     where o.type_samosval = ?2 and date(o.time_of_come_loading) = date(?1)\n" +
         "                     group by o.num_samosval\n" +
         "                     order by (select count(b.reise) from object b where date(b.time_of_come_loading) = date(?1) and b.num_samosval = o.num_samosval)\n" +
         "                     limit 1)\n" +
         "order by g.reise", nativeQuery = true)
 List<Object> getObjectsByDateAndByTypeTruck(Timestamp time, String typeTruck);


// @Query(value = "select type_of_work from object group by type_of_work", nativeQuery = true)
// List<String> getAllTypeOfWork();

}
