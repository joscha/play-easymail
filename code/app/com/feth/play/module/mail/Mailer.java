package com.feth.play.module.mail;

import com.feth.play.module.mail.MailerImpl.Mail;

import akka.actor.Cancellable;

public interface Mailer {
	public Cancellable sendMail(final Mail email);

	public Cancellable sendMail(final String subject, final String textBody, final String recipient);

	public Cancellable sendMail(final String subject, final Mail.Body body, final String recipient);
}
