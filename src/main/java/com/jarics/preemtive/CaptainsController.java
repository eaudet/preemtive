package com.jarics.preemtive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CaptainsController {

  CaptainsRepositoryService captainsRepositoryService;


  @Autowired
  public CaptainsController(
      CaptainsRepositoryService captainsRepositoryService) {
    this.captainsRepositoryService = captainsRepositoryService;
  }

  @Preemptive
  @RequestMapping(value = "/captain/{id}", method = RequestMethod.GET, produces = "application/json")
  public Captain captainsAge(@PathVariable("id") Long id){
    return captainsRepositoryService.getById(id);
  }

  @RequestMapping(value = "/captain", method = RequestMethod.POST, produces = "application/json")
  public Captain createAthlete(@RequestBody Captain captain) throws Exception {
    return captainsRepositoryService.update(captain);
  }

  @RequestMapping(value = "/captain/{id}", method = RequestMethod.DELETE, produces = "application/json")
  public Captain removeAthlete(@PathVariable("id") Long id) {
    return captainsRepositoryService.removeCaptain(id);
  }


}
