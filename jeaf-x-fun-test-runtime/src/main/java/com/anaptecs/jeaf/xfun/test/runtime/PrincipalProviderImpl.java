/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import java.security.Principal;

import com.anaptecs.jeaf.xfun.api.principal.PrincipalProvider;

public class PrincipalProviderImpl implements PrincipalProvider {

  @Override
  public Principal getCurrentPrincipal( ) {
    return new PrincipalImpl();
  }
}
