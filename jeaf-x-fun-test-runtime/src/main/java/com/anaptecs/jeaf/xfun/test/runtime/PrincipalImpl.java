/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import java.security.Principal;

public class PrincipalImpl implements Principal {

  @Override
  public String getName( ) {
    return System.getProperty("user.name");
  }
}
