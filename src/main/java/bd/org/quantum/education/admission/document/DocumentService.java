package bd.org.quantum.education.admission.document;

import bd.org.quantum.common.utils.SearchForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@Slf4j
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public Page<Document> search(SearchForm searchForm) {

        Specification<Document> specification = DocumentSpecification.all()
                .and(DocumentSpecification.omniText(searchForm));

        Page<Document> documents;
        Pageable pageable = PageRequest.of(
                searchForm.getPage(),
                searchForm.getPageSize(),
                Sort.by(Sort.Direction.fromString(searchForm.getSortDirection()), searchForm.getSortBy())
        );

        if (searchForm.isUnpaged()) {
            List<Document> list = repository.findAll(specification, pageable.getSort());
            documents = new PageImpl<>(list);
        } else {
            documents = repository.findAll(specification, pageable);
        }
        return documents;
    }

    public Document documentEntry(Document documents) {
        return repository.save(documents);
    }

    public Document update(Long id, Document documentForm) {
        Document documents = findById(id);
        BeanUtils.copyProperties(documentForm, documents);
        return repository.save(documentForm);
    }

    public void validateDocument(Document document, BindingResult result, Long id) {
        if (document.getDocumentCode().trim().isEmpty() || existsDocumentByCode(document.getDocumentCode(), id)) {
            if (document.getDocumentCode().trim().isEmpty()) {
                result.rejectValue("documentCode", "error.required");
                document.setDocumentCode("");
            }else {
                result.rejectValue("documentCode", "document.code.exists");
            }
        }
        if(document.getDocumentName().trim().isEmpty()){
            result.rejectValue("documentName", "error.required");
            document.setDocumentName("");
        }
        if(document.getId() != null){
            if(repository.existsByDocumentCodeAndIdNot(document.getDocumentCode(), document.getId())){
                result.rejectValue("documentCode", "document.code.exists");
            }
        }
    }

    public boolean existsDocumentByCode(String documentCode, Long documentId) {
        if (documentId != null) {
            return false;
        }
        return repository.existsByDocumentCode(documentCode);
    }

    public List<Document> findAll() {
        return repository.findAll();
    }

    public Document findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Document> findAllByDocumentCode(String documentCode) {
        return repository.findAllByDocumentCode(documentCode);
    }
}
