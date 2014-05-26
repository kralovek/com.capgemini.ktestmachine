package com.capgemini.ktestmachine.utils.ftp;

import java.io.PrintWriter;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.log4j.Logger;



public class PrintCommandListenerImpl implements ProtocolCommandListener {
	private static final Logger LOGGER = Logger
	.getLogger(PrintCommandListenerImpl.class);

	private PrintWriter __writer;

	public PrintCommandListenerImpl(PrintWriter writer) {
		__writer = writer;
	}

	public void protocolCommandSent(ProtocolCommandEvent event) {
		LOGGER.trace("BEGIN");
		try {
			__writer.print(event.getMessage());
			__writer.flush();
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	public void protocolReplyReceived(ProtocolCommandEvent event) {
		LOGGER.trace("BEGIN");
		try {
			__writer.print(event.getMessage());
			__writer.flush();
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}
}
