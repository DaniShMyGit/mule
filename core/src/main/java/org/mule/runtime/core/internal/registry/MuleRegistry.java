/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.internal.registry;

import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.construct.FlowConstruct;
import org.mule.runtime.core.api.transformer.Transformer;
import org.mule.runtime.core.api.transformer.TransformerException;

import java.util.List;

/**
 * Adds lookup/register/unregister methods for Mule-specific entities to the standard Registry interface.
 */
public interface MuleRegistry extends InternalRegistry {

  Transformer lookupTransformer(String name);

  FlowConstruct lookupFlowConstruct(String name);

  /**
   * This method will return a list of {@link org.mule.runtime.core.api.transformer.Transformer} objects that accept the given
   * input and return the given output type of object
   * <p/>
   * All {@link Transformer}s found will have a source that is compatible with {@param source}
   * (since if a transformer can transform a super type, it should be able to transform any type that extends it)
   * and a target such that {@param target} isCompatibleWith() the {@link Transformer}'s one
   * (since if we want a transformer that returns an specific type, it should return exactly that type or any type that extends it.)
   *
   * @param source The desired input type for the transformer
   * @param result the desired output type for the transformer
   * @return a list of matching transformers. If there were no matches an empty list is returned.
   * @since 3.0.0
   */
  List<Transformer> lookupTransformers(DataType source, DataType result);

  /**
   * Will find a transformer that is the closest match to the desired input and output.
   *
   * @param source The desired input type for the transformer
   * @param result the desired output type for the transformer
   * @return A transformer that exactly matches or the will accept the input and output parameters
   * @throws TransformerException will be thrown if there is more than one match
   * @since 3.0.0
   */
  Transformer lookupTransformer(DataType source, DataType result) throws TransformerException;

  /**
   * Will execute any processors on an object and fire any lifecycle methods according to the current lifecycle without actually
   * registering the object in the registry. This is useful for prototype objects that are created per request and would clutter
   * the registry with single use objects. Not that this will only be applied to Mule registies. Thrid party registries such as
   * Guice support wiring, but you need to get a reference to the container/context to call the method. This is so that wiring
   * mechanisms dont trip over each other.
   *
   * @param object the object to process
   * @return the same object with any processors and lifecycle methods called
   * @throws MuleException if the registry fails to perform the lifecycle change or process object processors for the object.
   */
  Object applyProcessorsAndLifecycle(Object object) throws MuleException;

  /**
   * Will execute any processors on an object without actually registering the object in the registry. This is useful for
   * prototype objects that are created per request and would clutter the registry with single use objects. Not that this will
   * only be applied to Mule registries. Third party registries such as Guice support wiring, but you need to get a reference to
   * the container/context to call the method. This is so that wiring mechanisms dont trip over each other.
   *
   * @param object the object to process
   * @return the same object with any processors called
   * @throws MuleException if the registry fails to process object processors for the object.
   */
  Object applyProcessors(Object object) throws MuleException;

  /**
   * @return the {@link MuleContext} that owns this registry instance. Non null.
   */
  MuleContext getMuleContext();
}
