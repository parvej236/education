package bd.org.quantum.education.admission.document;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.common.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class DocumentController {
    private final DocumentService service;
    private final MessageSource messageSource;

    public DocumentController(DocumentService service, MessageSource messageSource) {
        this.service = service;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.DOCUMENTS_INFORMATION)
    @SecurityCheck(permissions = {"ADMISSION_DOCUMENT_CREATE"})
    public String documentsEntry(Model model) {
        Document documents = new Document();
        model.addAttribute("documents", documents);
        return "admission/document/document-information-form";
    }

    @PostMapping(Routes.DOCUMENTS_INFORMATION)
    @SecurityCheck(permissions = {"ADMISSION_DOCUMENT_CREATE"})
    public String examCenterEntry(@Valid Document documents, BindingResult result, Model model) {
        service.validateDocument(documents, result, documents.getId());
        try {
            if (!result.hasErrors()) {
                service.documentEntry(documents);
                SubmitResult.success(messageSource, "document.create.success", model);
            } else {
                SubmitResult.error(messageSource, "document.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "document.create.error", model);
        }
        model.addAttribute("documents", documents);
        return "admission/document/document-information-form";
    }

    @GetMapping(Routes.DOCUMENTS_INFORMATION_UPDATE)
    @SecurityCheck(permissions = {"ADMISSION_DOCUMENT_VIEW"})
    public String updateDocument(@RequestParam Long id, Model model) {

        Document documents = new Document();
        if (id != null && id > 0) {
            documents = service.findById(id);
        }
        model.addAttribute("documents", documents);
        return "admission/document/document-information-form";
    }

    @PostMapping(Routes.DOCUMENTS_INFORMATION_UPDATE)
    @SecurityCheck(permissions = {"ADMISSION_DOCUMENT_CREATE"})
    public String update(@Valid Document documents, BindingResult result, Model model) {
        service.validateDocument(documents, result, documents.getId());
        try {
            if (!result.hasErrors()) {
                service.update(documents.getId(), documents);
                return "redirect:" + Routes.DOCUMENTS;
            } else {
                SubmitResult.error(messageSource, "document.update.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "document.update.error", model);
        }
        model.addAttribute("documents", documents);
        return "admission/document/document-information-form";
    }

    @GetMapping(Routes.SEARCH_DOCUMENT)
    @ResponseBody
    public Page<Document> search(SearchForm searchForm) {
        return service.search(searchForm);
    }

    @GetMapping(Routes.DOCUMENTS)
    @SecurityCheck(permissions = {"ADMISSION_DOCUMENT_VIEW"})
    public String list(Model model) {
        model.addAttribute("searchUrl", Routes.SEARCH_DOCUMENT);
        model.addAttribute("updateUrl", Routes.DOCUMENTS_INFORMATION_UPDATE);
        return "/admission/document/document-list";
    }

    @GetMapping(Routes.GET_ADMISSION_DOCUMENT_BY_DOCUMENT_CODE)
    @ResponseBody
    public ResponseEntity<List<Document>> getAdmissionDocumentsByDocumentCode(@RequestParam(name = "documentCode") String documentCode) {
        return new ResponseEntity<>(service.findAllByDocumentCode(documentCode), HttpStatus.OK);
    }
}
