/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistry;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistryFactory;

public class DatatypeConverterRegistryFactoryImpl implements DatatypeConverterRegistryFactory {

  @Override
  public DatatypeConverterRegistry getDatatypeConverterRegistry( ) {
    // TODO Auto-generated method stub
    return new DatatypeConverterRegistryImpl();
  }
}
