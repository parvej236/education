package bd.org.quantum.education.admission.examcenter;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.common.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Slf4j
@Controller
public class ExamCenterController {

    private final ExamCenterService service;;
    private final MessageSource messageSource;

    public ExamCenterController(ExamCenterService service, MessageSource messageSource) {
        this.service = service;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.EXAM_CENTER_ENTRY)
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_EXAM_CENTER"})
    public String examCenterEntry(Model model){
        ExamCenter examCenter = new ExamCenter();
        model.addAttribute("examCenter", examCenter);
        return "admission/examcenter/exam-center-entry-form";
    }

    @PostMapping(Routes.EXAM_CENTER_ENTRY)
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_EXAM_CENTER"})
    public String examCenterEntry(@Valid ExamCenter examCenter, BindingResult result, Model model){
        service.validateExamCenter(examCenter, result, examCenter.getId());
        try {
            if (!result.hasErrors()) {
                service.examCenterEntry(examCenter);
                SubmitResult.success(messageSource, "exam.center.create.success", model);
            } else {
                SubmitResult.error(messageSource, "exam.center.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "exam.center.create.error", model);
        }
        model.addAttribute("examCenter", examCenter);
        return "admission/examcenter/exam-center-entry-form";
    }

    @GetMapping(Routes.EXAM_CENTER_UPDATE)
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_VIEW"})
    public String examCenterUpdate(@RequestParam Long id, Model model){
        ExamCenter examCenter = new ExamCenter();
        if (id != null && id > 0) {
            examCenter = service.get(id);
        }
        model.addAttribute("examCenter", examCenter);
        return "admission/examcenter/exam-center-entry-form";
    }

    @PostMapping(Routes.EXAM_CENTER_UPDATE)
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_EXAM_CENTER"})
    public String update(@Valid ExamCenter examCenter, BindingResult result, Model model) {
        service.validateExamCenter(examCenter, result, examCenter.getId());
        try {
            if (!result.hasErrors()) {
                service.update(examCenter.getId(), examCenter);
                SubmitResult.success(messageSource, "exam.center.update.success", model);

                return "redirect:" + Routes.EXAM_CENTER_LIST;

            } else {
                SubmitResult.error(messageSource, "exam.center.update.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "exam.center.update.error", model);
        }
        model.addAttribute("examCenter", examCenter);
        return "admission/examcenter/exam-center-entry-form";
    }

    @GetMapping(Routes.EXAM_CENTER_LIST)
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_VIEW"})
    public String list(Model model) {
        model.addAttribute("searchUrl", Routes.SEARCH_EXAM_CENTER);
        model.addAttribute("updateUrl", Routes.EXAM_CENTER_UPDATE);
        return "admission/examcenter/exam-center-list";
    }

    @GetMapping(Routes.SEARCH_EXAM_CENTER)
    @ResponseBody
    public Page<ExamCenter> search(SearchForm searchForm) {
        return service.search(searchForm);
    }
}
