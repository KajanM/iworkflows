package com.kajan.iworkflows.controller.nextcloud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nextcloud")
public class NextcloudController {

    //private final Logger logger = LoggerFactory.getLogger(NextcloudController.class);
    //
    //@Autowired
    //private RestTemplate restTemplate;
    //
    //@Autowired
    //private NextcloudService nextcloudService;
    //
    //@Value("${nextcloud.uri.userinfo}")
    //private String USER_INFOR_URI;

    //@GetMapping("/nextcloud-user")
    //@ResponseBody
    //public String getNextcloudUserInfo(Principal principal) {
    //
    //    HttpEntity<String> httpEntity = new HttpEntity<>("", nextcloudService.getNextcloudHeaders(principal));
    //    ResponseEntity<String> responseEntity = restTemplate.exchange(USER_INFOR_URI.replace(PLACEHOLDER_USERID, principal.getName()), HttpMethod.GET, httpEntity, String.class);
    //    return responseEntity.toString();
    //}

}
