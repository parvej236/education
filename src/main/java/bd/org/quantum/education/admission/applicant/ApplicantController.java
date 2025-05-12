package bd.org.quantum.education.admission.applicant;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.Image;
import bd.org.quantum.common.utils.ImageService;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.admission.document.DocumentService;
import bd.org.quantum.education.admission.examcenter.ExamCenterService;
import bd.org.quantum.education.admission.student.StudentService;
import bd.org.quantum.education.common.CommonService;
import bd.org.quantum.education.common.Community;
import bd.org.quantum.education.common.Religion;
import bd.org.quantum.education.common.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class ApplicantController {

    @Value("${file.path}")
    private String imageFilePath;

    private final ApplicantService service;
    private final MessageSource messageSource;
    private final CommonService commonService;
    private final AcademicReferenceService academicReferenceService;
    private final ExamCenterService examCenterService;
    private final DocumentService documentsService;
    private final ImageService imageService;
    private final StudentService studentService;

    public ApplicantController(ApplicantService service,
                               MessageSource messageSource,
                               CommonService commonService,
                               AcademicReferenceService academicReferenceService,
                               ExamCenterService examCenterService,
                               DocumentService documentsService,
                               StudentService studentService,
                               ImageService imageService) {
        this.service = service;
        this.messageSource = messageSource;
        this.commonService = commonService;
        this.academicReferenceService = academicReferenceService;
        this.examCenterService = examCenterService;
        this.documentsService = documentsService;
        this.imageService = imageService;
        this.studentService = studentService;
    }

    @GetMapping(Routes.CREATE_APPLICANT)
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_VIEW"})
    public String create(@RequestParam(name = "id", defaultValue = "0") Long id,
                         @RequestParam(name = "result", required = false) boolean result,
                         @RequestParam(name = "action", required = false) String action,
                         Model model) {

        model.addAttribute("applicant", (id > 0) ? service.getApplicantById(id) : new Applicant());
        model.addAttribute("entryLink", Routes.CREATE_APPLICANT);
        model.addAttribute("clazzList", academicReferenceService.getAllClass());
        model.addAttribute("examCenterList", examCenterService.getAllExamCenters());
        model.addAttribute("documentsList", documentsService.findAll());
        model.addAttribute("communities", Community.values());
        model.addAttribute("religions", Religion.values());
        referenceData(model);

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "applicant.create.success", model);
        }
        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "applicant.update.success", model);
        }
        return "admission/applicant/applicant-form";
    }

    @PostMapping(Routes.CREATE_APPLICANT)
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_CREATE"})
    public String create(@Valid Applicant applicant, BindingResult result, Model model) {
        service.validateApplicant(applicant, result);
        String action = applicant.getAction();

        try {
            if (!result.hasErrors()) {
                applicant = service.create(applicant);
                return "redirect:" + Routes.CREATE_APPLICANT + "?id=" + applicant.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "applicant.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "applicant.create.error", model);
        }

        model.addAttribute("applicant", applicant);
        model.addAttribute("entryLink", Routes.CREATE_APPLICANT);
        model.addAttribute("clazzList", academicReferenceService.getAllClass());
        model.addAttribute("examCenterList", examCenterService.getAllExamCenters());
        model.addAttribute("documentsList", documentsService.findAll());
        model.addAttribute("communities", Community.values());
        model.addAttribute("religions", Religion.values());
        referenceData(model);

        return "admission/applicant/applicant-form";
    }

    @PostMapping(Routes.APPLICANT_IMAGE_UPLOAD)
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

    @GetMapping(Routes.APPLICANT_IMAGE_DOWNLOAD)
    @ResponseBody
    public Image downloadImage(@RequestParam String path) {
        Image image = new Image();

        if (!StringUtils.isEmpty(path)) {
            image = imageService.downloadImg(path);
        }
        return image;
    }

    private void referenceData(Model model) {
        model.addAttribute("countries", commonService.getCountryList());
        model.addAttribute("districts", commonService.getDistrictList());
    }

    @GetMapping(Routes.APPLICANTS)
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_VIEW"})
    public String applicantList(Model model) {
        model.addAttribute("searchUrl", Routes.SEARCH_APPLICANT);
        model.addAttribute("entryUrl", Routes.CREATE_APPLICANT);
        model.addAttribute("religionList", commonService.religions());
        model.addAttribute("communityList", commonService.communities());
        model.addAttribute("examCenterList", examCenterService.getAllExamCenters());
        model.addAttribute("districts", studentService.districts());
        model.addAttribute("upazilasByDistrict", Routes.UPAZILAS);
        model.addAttribute("academicClasses", academicReferenceService.getAllClass());
        return "admission/applicant/applicant-list";
    }

    @GetMapping(Routes.SEARCH_APPLICANT)
    @ResponseBody
    public Page<Applicant> search(ApplicantSearchCriteria searchForm) {
        return service.search(searchForm);
    }

    @GetMapping(Routes.APPLICANT_LIST)
    @ResponseBody
    public List<Applicant> getRegistrations(ApplicantSearchCriteria search) {
        search.setUnpaged(true);
        search.setSelected(null);
        search.setAdmitted(false);
        return service.search(search).getContent();
    }
}