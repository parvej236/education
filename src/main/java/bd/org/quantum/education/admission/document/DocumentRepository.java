package bd.org.quantum.education.admission.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {
    boolean existsByDocumentCode(String documentCode);
    List<Document> findAllByDocumentCode(String documentCode);
    boolean existsByDocumentCodeAndIdNot(String documentCode, Long id);
}