/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistry;

public class DatatypeConverterRegistryImpl implements DatatypeConverterRegistry {
  @Override
  public <I, O> DatatypeConverter<I, O> getConverter( Class<I> pInputType, Class<O> pOutputType ) {
    return null;
  }
}
