package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import thuvien.entity.DigitalDocument;
import java.util.List;

public interface DigitalDocumentRepository extends JpaRepository<DigitalDocument, Long> {
    
    @Query("SELECT d FROM DigitalDocument d WHERE " +
           "(:q IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
           "(:type IS NULL OR d.type = :type)")
    List<DigitalDocument> searchDigitalDocs(String q, String type);


}