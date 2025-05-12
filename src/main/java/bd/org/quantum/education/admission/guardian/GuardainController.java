package bd.org.quantum.education.admission.guardian;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.Image;
import bd.org.quantum.common.utils.ImageService;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.admission.student.StudentService;
import bd.org.quantum.education.common.Relation;
import bd.org.quantum.education.common.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class GuardainController {
    @Value("${file.path}")
    private String imageFilePath;

    private final GuardianService service;
    private final MessageSource messageSource;
    private final ImageService imageService;
    private final StudentService studentService;

    public GuardainController(GuardianService service,
                              MessageSource messageSource,
                              ImageService imageService,
                              StudentService studentService) {
        this.service = service;
        this.messageSource = messageSource;
        this.imageService = imageService;
        this.studentService = studentService;
    }

    @GetMapping(Routes.GUARDIAN_ENTRY)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_VIEW"})
    public String guardianEntry(@RequestParam(name = "id", defaultValue = "0") Long id,
                                @RequestParam(name = "action", required = false) String action,
                                @RequestParam(name = "result", required = false) boolean result,
                                Model model) {
        model.addAttribute("guardian", (id > 0) ? service.getGuardianById(id) : new Guardian());
        model.addAttribute("entryUrl", Routes.GUARDIAN_ENTRY);
        model.addAttribute("applicantUrl", Routes.CREATE_APPLICANT);
        model.addAttribute("relations", Relation.getAllRelations());
        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "guardian.create.success", model);
        }
        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "guardian.update.success", model);
        }
        return "admission/guardian/guardian-entry";
    }

    @PostMapping(Routes.GUARDIAN_ENTRY)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_GUARDAIN"})
    public String guardianEntry(@Valid Guardian guardian, BindingResult result, Model model) {
        service.validateGuardian(guardian, result);
        String action = guardian.getAction();
        try {
            if (!result.hasErrors()) {
                Guardian newGuardian = service.saveGuardian(guardian);
                return "redirect:" + Routes.GUARDIAN_ENTRY + "?id=" + newGuardian.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "guardian.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "guardian.create.error", model);
        }
        guardian.setStudent(studentService.getStudentById(guardian.getStudent().getId()));
        model.addAttribute("guardian", guardian);
        model.addAttribute("entryUrl", Routes.GUARDIAN_ENTRY);
        model.addAttribute("applicantUrl", Routes.CREATE_APPLICANT);
        model.addAttribute("relations", Relation.getAllRelations());
        return "admission/guardian/guardian-entry";
    }

    @GetMapping(Routes.GUARDIAN_LIST)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_VIEW"})
    public String guardianList(Model model) {
        model.addAttribute("searchUrl", Routes.GUARDIAN_SEARCH);
        model.addAttribute("entryUrl", Routes.GUARDIAN_ENTRY);
        return "admission/guardian/guardian-list";
    }

    @GetMapping(Routes.GUARDIAN_SEARCH)
    @ResponseBody
    public Page<Guardian> guardianSearch(SearchForm form) {
        return service.guardianSearch(form);
    }

    @PostMapping(Routes.GUARDIAN_IMAGE_UPLOAD)
    @ResponseBody
    public Image uploadImage(@RequestBody Image image, BindingResult result) {
        Image newImage = new Image();
        try {
            if (!result.hasErrors()) {
                image.setPath(imageFilePath);
                newImage = imageService.upload(image, "Image");
                newImage.setObjId(image.getObjId());
                service.updateImagePath(newImage.getPath(), newImage.getObjId());
                log.debug("image uploaded successfully");
            } else {
                log.debug("image uploaded error due to binding error");
            }
        } catch (Exception e) {
            log.debug("image uploaded exception..." + e);
        }
        return newImage;
    }

    @GetMapping(Routes.GUARDIAN_IMAGE_DOWNLOAD)
    @ResponseBody
    public Image downloadImage(@RequestParam String path) {
        Image image = new Image();

        if (!StringUtils.isEmpty(path)) {
            image = imageService.downloadImg(path);
        }
        return image;
    }

    @GetMapping(Routes.GET_GUARDIAN_LIST_BY_STUDENT_ID)
    @ResponseBody
    public ResponseEntity<List<Guardian>> getGuardianListByStudentId(@RequestParam Long id) {
        return new ResponseEntity<>(service.getGuardianListByStudentId(id), HttpStatus.OK) ;
    }
}
