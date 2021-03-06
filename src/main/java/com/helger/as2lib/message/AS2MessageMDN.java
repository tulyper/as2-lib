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
package com.helger.as2lib.message;

import java.text.DecimalFormat;

import javax.annotation.Nonnull;

import com.helger.as2lib.partner.CPartnershipIDs;
import com.helger.as2lib.partner.Partnership;
import com.helger.as2lib.util.CAS2Header;
import com.helger.as2lib.util.DateUtil;
import com.phloc.commons.random.VerySecureRandom;

public class AS2MessageMDN extends AbstractMessageMDN
{
  public static final String MDNA_REPORTING_UA = "REPORTING_UA";
  public static final String MDNA_ORIG_RECIPIENT = "ORIGINAL_RECIPIENT";
  public static final String MDNA_FINAL_RECIPIENT = "FINAL_RECIPIENT";
  public static final String MDNA_ORIG_MESSAGEID = "ORIGINAL_MESSAGE_ID";
  public static final String MDNA_DISPOSITION = "DISPOSITION";
  public static final String MDNA_MIC = "MIC";
  public static final String DEFAULT_DATE_FORMAT = "ddMMyyyyHHmmssZ";

  public AS2MessageMDN (@Nonnull final AS2Message aMsg)
  {
    super (aMsg);
    // Swap from and to
    setHeader (CAS2Header.HEADER_AS2_TO, aMsg.getHeader (CAS2Header.HEADER_AS2_FROM));
    setHeader (CAS2Header.HEADER_AS2_FROM, aMsg.getHeader (CAS2Header.HEADER_AS2_TO));
  }

  @Override
  public String generateMessageID ()
  {
    final StringBuilder aSB = new StringBuilder ();
    final String sDateFormat = getPartnership ().getAttribute (CPartnershipIDs.PA_DATE_FORMAT, DEFAULT_DATE_FORMAT);
    aSB.append ("<OPENAS2-").append (DateUtil.getFormattedDateNow (sDateFormat));

    final DecimalFormat aRandomFormatter = new DecimalFormat ("0000");
    aSB.append ('-').append (aRandomFormatter.format (VerySecureRandom.getInstance ().nextInt (10000)));

    // Message details
    final Partnership aPartnership = getMessage ().getPartnership ();
    final String sReceiverID = aPartnership.getReceiverID (CPartnershipIDs.PID_AS2);
    final String sSenderID = aPartnership.getSenderID (CPartnershipIDs.PID_AS2);
    aSB.append ('@').append (sReceiverID).append ('_').append (sSenderID);

    return aSB.append ('>').toString ();
  }
}
