/*
 * Copyright (C) 2020 Nathan Crause <nathan@crause.name>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package photohosting.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class MailerServiceImpl implements MailerService {

	@Override
	public Message send(ServletContext servletContext, String name, String recipient, String subject, Map<String, ?> args) throws MessagingException {
		try {
			Session session = getSession();
			Message message = new MimeMessage(session);
			
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject(subject);
			message.setFrom();
			message.setContent(processTemplate(getTemplate(servletContext, name), args), "text/html");
			
			Transport.send(message);
			
			return message;
		}
		catch (NamingException ex) {
			throw new MessagingException("Failed to initialize session", ex);
		}
		catch (IOException ex) {
			throw new MessagingException("Failed to read template", ex);
		}
	}
	
	private Session getSession() throws NamingException {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		
		return (Session) envCtx.lookup("mail/PhotoHosting");
	}
	
	private String getTemplate(ServletContext servletContext, String name) throws IOException {
		InputStream in = servletContext.getResourceAsStream(String.format("/mail/%s.html", name));
		
		return IOUtils.toString(in, Charset.forName("UTF-8"));
	}
	
	private String processTemplate(String template, Map<String, ?> args) {
		StringSubstitutor subs = new StringSubstitutor(args);
		
		return subs.replace(template);
	}
	
}
