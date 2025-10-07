package com.spring.hrm.entity;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {
   String getAuthority();
}
