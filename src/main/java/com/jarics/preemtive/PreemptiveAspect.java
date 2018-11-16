package com.jarics.preemtive;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
/**
 * This aspect manages the distributed hashmap and push notification.
 */
public class PreemptiveAspect {

  @Before("@annotation(com.jarics.preemtive.Preemptive)")
  public void updateHasMap(){
    long start = System.currentTimeMillis();

    long executionTime = System.currentTimeMillis() - start;

    System.out.println(" executed in " + executionTime + "ms");

  }

}
