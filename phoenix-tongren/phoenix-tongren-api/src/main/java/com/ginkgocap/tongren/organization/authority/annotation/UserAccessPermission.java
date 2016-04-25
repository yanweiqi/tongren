package com.ginkgocap.tongren.organization.authority.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;


@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserAccessPermission {
	OrganizationRoles role() default OrganizationRoles.MEMBER;
	OrganizationAuthorities authority() default OrganizationAuthorities.ORGANIZATION_MANAGE_SHOW;
}
