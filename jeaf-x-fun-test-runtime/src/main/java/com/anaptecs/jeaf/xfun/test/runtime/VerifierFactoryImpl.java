/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.api.checks.Verifier;
import com.anaptecs.jeaf.xfun.api.checks.VerifierFactory;
import com.anaptecs.jeaf.xfun.fallback.checks.FallbackVerifierImpl;

public class VerifierFactoryImpl implements VerifierFactory {

  @Override
  public Verifier getVerifier( ) {
    return new FallbackVerifierImpl();
  }
}
