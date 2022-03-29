package kg.parser.blastmaker.xlms.respositiry;

import kg.parser.blastmaker.xlms.objects.ExcavatorDTO;
import kg.parser.blastmaker.xlms.objects.TruckDTO;
import kg.parser.blastmaker.xlms.objects.TruckTypeDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TruckRepository extends JpaRepository<TruckDTO, Long>, JpaSpecificationExecutor<TruckDTO> {
    @Query(value = "select * from truck where number = ?1", nativeQuery = true)
    TruckDTO getByNumber(int name);
}
