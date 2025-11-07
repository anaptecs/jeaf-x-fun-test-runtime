/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.api.locale.LocaleProvider;
import com.anaptecs.jeaf.xfun.api.locale.LocaleProviderFactory;

public class LocaleProviderFactoryImpl implements LocaleProviderFactory {

  @Override
  public LocaleProvider getLocaleProvider( ) {
    return new LocaleProviderImpl();
  }
}
