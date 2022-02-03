package kg.parser.blastmaker.xlms.respositiry;

import kg.parser.blastmaker.xlms.objects.Excavator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExcavatorRepo extends JpaRepository<Excavator, Long> {

    @Query(value = "select * from ex where name = ?1", nativeQuery = true)
    List<Excavator> getByName(String name);
}
