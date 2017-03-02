package com.anbillon.routine.sample;

import java.io.Serializable;

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class SerializableExtra implements Serializable {
  private static final long serialVersionUID = 2056085269735133677L;
  public final String id;

  public SerializableExtra(String id) {
    this.id = id;
  }
}
