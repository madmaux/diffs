package org.mq.diff.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * SwaggerController: In charge of redirect home path to swagger documentation
 *
 * @author swagger.io
 *
 */
@Controller
@ApiIgnore
public class SwaggerController {

  /**
   *
   * homePath: Redirects each call to home url to the swagger documentation
   *
   * @return redirect, target: swagger-ui.thml
   *
   */
  @RequestMapping("/")
  public String homePath() {
    return "redirect:/swagger-ui.html";
  }

}
