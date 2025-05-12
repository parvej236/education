package bd.org.quantum.education.common;

import bd.org.quantum.authorizer.Authorizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CommonController {
    public final CommonService service;
    public final Authorizer authorizer;

    public CommonController(CommonService service,
                            Authorizer authorizer) {
        this.service = service;
        this.authorizer = authorizer;
    }

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping(Routes.UPAZILAS)
    @ResponseBody
    public ResponseEntity<List<Upazila>> getUpazilas(@RequestParam("district") Long district){
        return new ResponseEntity<>(service.upazilas(district), HttpStatus.OK);
    }

    @GetMapping(Routes.UNIONS)
    @ResponseBody
    public ResponseEntity<List<Unions>> getUnions(@RequestParam("upazila") Long upazila){
        return new ResponseEntity<>(service.unions(upazila), HttpStatus.OK);
    }
}