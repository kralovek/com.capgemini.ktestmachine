package com.capgemini.ktestmachine.utils.ftp;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;

public class UtilsSFtpJsch {

	public static FTPClient connect(String host, int port, String login,
			String password) throws ABaseException {

		FTPSClient client = new FTPSClient("TLS");

		client.addProtocolCommandListener(new PrintCommandListenerImpl(new PrintWriter(System.out)));
		//client.setCopyStreamListener(createListener());
		//FTPClientConfig cfg = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		//client.configure(cfg);

		
		
		try {
			try {
				client.connect(host, 22);
			} catch (Exception ex) {
				throw new TechnicalException(
						"Impossible to estabilish the connection to remote ftpHost: "
								+ host, ex);
			}

			try {
				if (!client.login(login, password)) {
					throw new TechnicalException(
							"Impossible to estabilish the connection to remote ftpHost: "
									+ host + ". Bad ftpLogin or ftpPassword");
				}
			} catch (IOException ex) {
				throw new TechnicalException(
						"Impossible de se connecter vers le remote ftpHost: "
								+ host, ex);
			}
			return client;
		} catch (ABaseException ex) {
			try {
				client.disconnect();
			} catch (IOException ex2) {
				// rien � faire
			} finally {
			}
			throw ex;
		}
	}
}
