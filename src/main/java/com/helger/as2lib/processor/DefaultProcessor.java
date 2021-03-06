/**
 * The FreeBSD Copyright
 * Copyright 1994-2008 The FreeBSD Project. All rights reserved.
 * Copyright (C) 2014 Philip Helger ph[at]phloc[dot]com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *    1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE FREEBSD PROJECT ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE FREEBSD PROJECT OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation
 * are those of the authors and should not be interpreted as representing
 * official policies, either expressed or implied, of the FreeBSD Project.
 */
package com.helger.as2lib.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.as2lib.exception.NoModuleException;
import com.helger.as2lib.exception.OpenAS2Exception;
import com.helger.as2lib.exception.ProcessorException;
import com.helger.as2lib.message.IMessage;
import com.helger.as2lib.processor.module.IProcessorActiveModule;
import com.helger.as2lib.processor.module.IProcessorModule;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;

public class DefaultProcessor extends AbstractProcessor
{
  private final List <IProcessorModule> m_aModules = new ArrayList <IProcessorModule> ();

  public final void addModule (@Nonnull final IProcessorModule aModule)
  {
    ValueEnforcer.notNull (aModule, "Module");
    m_aModules.add (aModule);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IProcessorModule> getAllModules ()
  {
    return ContainerHelper.newList (m_aModules);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IProcessorActiveModule> getAllActiveModules ()
  {
    final List <IProcessorActiveModule> ret = new ArrayList <IProcessorActiveModule> ();
    for (final IProcessorModule aModule : getAllModules ())
      if (aModule instanceof IProcessorActiveModule)
        ret.add ((IProcessorActiveModule) aModule);
    return ret;
  }

  public void handle (@Nonnull final String sAction,
                      @Nonnull final IMessage aMsg,
                      @Nullable final Map <String, Object> aOptions) throws OpenAS2Exception
  {
    final List <Throwable> aCauses = new ArrayList <Throwable> ();
    boolean bModuleFound = false;

    for (final IProcessorModule aModule : getAllModules ())
      if (aModule.canHandle (sAction, aMsg, aOptions))
      {
        try
        {
          bModuleFound = true;
          aModule.handle (sAction, aMsg, aOptions);
        }
        catch (final OpenAS2Exception ex)
        {
          aCauses.add (ex);
        }
      }

    if (!aCauses.isEmpty ())
      throw new ProcessorException (this, aCauses);
    if (!bModuleFound)
      throw new NoModuleException (sAction, aMsg, aOptions);
  }

  public void startActiveModules ()
  {
    for (final IProcessorActiveModule aModule : getAllActiveModules ())
      try
      {
        aModule.start ();
      }
      catch (final OpenAS2Exception ex)
      {
        ex.terminate ();
      }
  }

  public void stopActiveModules ()
  {
    for (final IProcessorActiveModule aModule : getAllActiveModules ())
      try
      {
        aModule.stop ();
      }
      catch (final OpenAS2Exception ex)
      {
        ex.terminate ();
      }
  }
}
