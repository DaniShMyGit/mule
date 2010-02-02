/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.config;

import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.config.builders.AbstractConfigurationBuilder;
import org.mule.impl.annotations.processors.InjectAnnotationProcessor;
import org.mule.impl.annotations.processors.NamedAnnotationProcessor;
import org.mule.impl.endpoint.DefaultAnnotationsParserFactory;

/**
 * Enables Mule annotation processing so that annotated objects registered with the Mule registry will automatically
 * be configured. This builder also enables JSR-330 injection annotations {@link javax.inject.Inject} and {@link javax.inject.Named}.
 * <p/>
 * Internal Implementation note: We could have used a 'registry-bootstrap.properties' file to load the objects necessary
 * to enable annotations however, that method would not allow the annotation processors to be easily overridden when using
 * other platforms such as Google App Engine.
 *
 * @since 3.0.0
 */
public class AnnotationsConfigurationBuilder extends AbstractConfigurationBuilder
{
    @Override
    protected void doConfigure(MuleContext muleContext) throws Exception
    {
        //Make the annotation parsers available
        AnnotationsParserFactory factory = createAnnotationsParserFactory();
        muleContext.getRegistry().registerObject("_" + factory.getClass().getSimpleName(), factory);

        registerProcessors(muleContext.getRegistry());
    }

    protected AnnotationsParserFactory createAnnotationsParserFactory()
    {
        return new DefaultAnnotationsParserFactory();
    }

    protected void registerProcessors(MuleRegistry registry) throws RegistrationException
    {
        //Add support for JSR-330
        registry.registerObject("_" + InjectAnnotationProcessor.class.getSimpleName(), new InjectAnnotationProcessor());
        registry.registerObject("_" + NamedAnnotationProcessor.class.getSimpleName(), new NamedAnnotationProcessor());
    }
}
