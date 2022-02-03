//package kg.parser.blastmaker.xlms.respositiry;
//
//
//import kg.parser.blastmaker.xlms.objects.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//
//public interface UserRepository extends JpaRepository<User, Long> {
//
//    @Query(value="select TOP 1 from urs where name = ?1", nativeQuery = true)
//    User findbyName(String name);
//}
