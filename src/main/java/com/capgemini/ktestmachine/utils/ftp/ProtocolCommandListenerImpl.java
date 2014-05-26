package com.capgemini.ktestmachine.utils.ftp;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.log4j.Logger;



public class ProtocolCommandListenerImpl implements ProtocolCommandListener {
	private static final Logger LOGGER = Logger
	.getLogger(ProtocolCommandListenerImpl.class);
	
	public void protocolCommandSent(ProtocolCommandEvent protocolcommandevent) {
		LOGGER.trace("BEGIN");
		try {
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	public void protocolReplyReceived(ProtocolCommandEvent protocolcommandevent) {
		LOGGER.trace("BEGIN");
		try {
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}
}
