package thuvien.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import thuvien.entity.Publisher;
import java.util.List;
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    List<Publisher> findByNameContainingIgnoreCase(String name);
}