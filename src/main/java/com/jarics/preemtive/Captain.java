package com.jarics.preemtive;

import lombok.EqualsAndHashCode;
import org.dizitart.no2.objects.Id;

@EqualsAndHashCode
public class Captain {
  @Id
  long id;
  long version;
  long age;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  public long getAge() {
    return age;
  }

  public void setAge(long age) {
    this.age = age;
  }
}
