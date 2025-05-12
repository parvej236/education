package bd.org.quantum.education.common;

import bd.org.quantum.common.resttemplate.RestTemplateService;
import bd.org.quantum.common.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CommonService {

    public static final String COUNTRY_LIST = "/api/country-list";
    public static final String DISTRICT_LIST = "/api/district-list";
    public static final String UPAZILAS = "/api/upazilas/";
    public static final String UNIONS = "/api/unions/";

    @Value("${resource.api.url}")
    private String resourceApiUrl;

    private final RestTemplateService restService;

    CommonService(RestTemplateService restService){
        this.restService = restService;
    }

    public List<Country> getCountryList() {
        List<Country> list = null;
        final String uri = UrlUtils.getUrl(resourceApiUrl, COUNTRY_LIST);
        try {
            list = restService.getForList(uri, new ParameterizedTypeReference<>(){});
        } catch (Exception e) {
            log.debug("Error: ", e);
        }
        return list;
    }

    public List<District> getDistrictList() {
        List<District> list = null;
        final String uri = UrlUtils.getUrl(resourceApiUrl, DISTRICT_LIST);
        try {
            list = restService.getForList(uri, new ParameterizedTypeReference<>(){});
        } catch (Exception e) {
            log.debug("Error: ", e);
        }
        return list;
    }

    public Map<String, String> getCountryCodeMap(){
        Map<String, String> map = new LinkedHashMap<>();

        for (Country dto  : getCountryList()){
            map.put( dto.getNameIso2() + " +" + dto.getDialCode().toString(),dto.getNameIso2() + " +" + dto.getDialCode().toString());
        }
        return sortByValue(map);
    }

    private static Map<String, String> sortByValue(Map<String, String> unSortMap) {
        List<Map.Entry<String, String>> list = new LinkedList<Map.Entry<String, String>>(unSortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : list) {
            if (!entry.getKey().equals("NONE")){
                sortedMap.put(entry.getKey(), entry.getValue());
            }
        }
        sortedMap.put("NONE","NONE");
        return sortedMap;
    }

    public List<Upazila> upazilas(Long district) {
        final String uri = UrlUtils.getUrl(resourceApiUrl, UPAZILAS + district);
        return restService.getForList(uri, new ParameterizedTypeReference<>(){});
    }

    public List<Unions> unions(Long upazila) {
        final String uri = UrlUtils.getUrl(resourceApiUrl, UNIONS + upazila);
        return restService.getForList(uri, new ParameterizedTypeReference<>(){});
    }
    public List<Religion> religions() {
        return new ArrayList<>(List.of(Religion.values()));
    }
    public List<Community> communities() {
        return new ArrayList<>(List.of(Community.values()));
    }
}