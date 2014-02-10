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
package com.helger.as2lib.partner;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.as2lib.util.IStringMap;
import com.helger.as2lib.util.StringMap;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.string.ToStringGenerator;

public class Partnership implements Serializable
{
  // Sender partner type
  public static final String PARTNERSHIP_TYPE_SENDER = "sender";
  // Receiver partner type
  public static final String PARTNERSHIP_TYPE_RECEIVER = "receiver";
  // Email address
  public static final String PID_EMAIL = "email";
  // AS1 or AS2
  public static final String PA_PROTOCOL = "protocol";
  // Subject sent in messages
  public static final String PA_SUBJECT = "subject";
  // optional content transfer encoding value
  public static final String PA_CONTENT_TRANSFER_ENCODING = "content_transfer_encoding";

  private String m_sName;
  private final StringMap m_aAttributes = new StringMap ();
  private final StringMap m_aReceiverIDs = new StringMap ();
  private final StringMap m_aSenderIDs = new StringMap ();

  public void setName (final String sKey)
  {
    m_sName = sKey;
  }

  public String getName ()
  {
    return m_sName;
  }

  public void setAttribute (final String sKey, final String sValue)
  {
    m_aAttributes.setAttribute (sKey, sValue);
  }

  @Nullable
  public String getAttribute (final String sKey)
  {
    return m_aAttributes.getAttributeAsString (sKey);
  }

  @Nonnull
  public IStringMap getAttributes ()
  {
    return new StringMap (m_aAttributes);
  }

  public void addAttributes (@Nullable final IStringMap aAttributes)
  {
    if (aAttributes != null)
      m_aAttributes.setAttributes (aAttributes.getAllAttributes ());
  }

  public void setAttributes (@Nullable final IStringMap aAttributes)
  {
    m_aAttributes.clear ();
    addAttributes (aAttributes);
  }

  public void setReceiverID (@Nullable final String sKey, final String sValue)
  {
    m_aReceiverIDs.setAttribute (sKey, sValue);
  }

  @Nullable
  public String getReceiverID (@Nullable final String sKey)
  {
    return m_aReceiverIDs.getAttributeAsString (sKey);
  }

  public boolean containsReceiverID (@Nullable final String sKey)
  {
    return m_aReceiverIDs.containsAttribute (sKey);
  }

  @Nonnull
  public StringMap getReceiverIDs ()
  {
    return new StringMap (m_aReceiverIDs);
  }

  public void setReceiverIDs (@Nullable final IStringMap aReceiverIDs)
  {
    m_aReceiverIDs.clear ();
    if (aReceiverIDs != null)
      m_aReceiverIDs.setAttributes (aReceiverIDs.getAllAttributes ());
  }

  public void setSenderID (final String sKey, final String sValue)
  {
    m_aSenderIDs.setAttribute (sKey, sValue);
  }

  @Nullable
  public String getSenderID (@Nullable final String sKey)
  {
    return m_aSenderIDs.getAttributeAsString (sKey);
  }

  public boolean containsSenderID (@Nullable final String sKey)
  {
    return m_aSenderIDs.containsAttribute (sKey);
  }

  @Nonnull
  public StringMap getSenderIDs ()
  {
    return new StringMap (m_aSenderIDs);
  }

  public void setSenderIDs (@Nullable final IStringMap aSenderIDs)
  {
    m_aSenderIDs.clear ();
    if (aSenderIDs != null)
      m_aSenderIDs.setAttributes (aSenderIDs.getAllAttributes ());
  }

  public boolean matches (@Nonnull final Partnership aPartnership)
  {
    return compareIDs (m_aSenderIDs, aPartnership.m_aSenderIDs) &&
           compareIDs (m_aReceiverIDs, aPartnership.m_aReceiverIDs);
  }

  protected boolean compareIDs (@Nonnull final IStringMap aIDs, @Nonnull final IStringMap aCompareTo)
  {
    if (aIDs.containsNoAttribute ())
      return false;

    for (final Map.Entry <String, String> aEntry : aIDs)
    {
      final String sCurrentValue = aEntry.getValue ();
      final String sCompareValue = aCompareTo.getAttributeObject (aEntry.getKey ());
      if (!EqualsUtils.equals (sCurrentValue, sCompareValue))
        return false;
    }

    return true;
  }

  public void copyFrom (@Nonnull final Partnership aPartnership)
  {
    if (aPartnership.getName () != null)
      setName (aPartnership.getName ());
    setSenderIDs (aPartnership.m_aSenderIDs);
    setReceiverIDs (aPartnership.m_aReceiverIDs);
    setAttributes (aPartnership.m_aAttributes);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("name", m_sName)
                                       .append ("senderIDs", m_aSenderIDs)
                                       .append ("receiverIDs", m_aReceiverIDs)
                                       .append ("attributes", m_aAttributes)
                                       .toString ();
  }
}
