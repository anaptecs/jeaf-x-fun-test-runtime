/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import java.util.Locale;

import com.anaptecs.jeaf.xfun.api.locale.LocaleProvider;

public class LocaleProviderImpl implements LocaleProvider {

  @Override
  public Locale getCurrentLocale( ) {
    return Locale.getDefault();
  }
}
