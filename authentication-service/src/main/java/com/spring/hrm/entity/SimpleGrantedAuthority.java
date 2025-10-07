package com.spring.hrm.entity;

import org.springframework.util.Assert;

public class SimpleGrantedAuthority implements GrantedAuthority{
   private static final long serialVersionUID = 620L;
   private final String role;

   public SimpleGrantedAuthority(String role) {
      Assert.hasText(role, "A granted authority textual representation is required");
      this.role = role;
   }

   public String getAuthority() {
      return this.role;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj instanceof SimpleGrantedAuthority) {
         SimpleGrantedAuthority sga = (SimpleGrantedAuthority)obj;
         return this.role.equals(sga.getAuthority());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.role.hashCode();
   }

   public String toString() {
      return this.role;
   }
}
