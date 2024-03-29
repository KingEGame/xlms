package kg.parser.blastmaker.xlms.respositiry;

import kg.parser.blastmaker.xlms.objects.ExcavatorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExcavatorRepository extends JpaRepository<ExcavatorDTO, Long>, JpaSpecificationExecutor<ExcavatorDTO> {

    @Query(value = "select * from excavator where name = ?1", nativeQuery = true)
    ExcavatorDTO getByName(String name);
}
