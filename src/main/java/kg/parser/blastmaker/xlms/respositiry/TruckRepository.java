package kg.parser.blastmaker.xlms.respositiry;

import kg.parser.blastmaker.xlms.objects.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    @Query(value = "select * from truck where name = ?1", nativeQuery = true)
    List<Truck> getByName(String name);
}
