/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.api.principal.PrincipalProvider;
import com.anaptecs.jeaf.xfun.api.principal.PrincipalProviderFactory;

public class PrincipalProviderFactoryImpl implements PrincipalProviderFactory {

  @Override
  public PrincipalProvider getPrincipalProvider( ) {
    return new PrincipalProviderImpl();
  }
}
