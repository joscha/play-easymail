package com.feth.play.module.mail;

import com.feth.play.module.mail.Mailer.Mail;

import akka.actor.Cancellable;

public interface IMailer {
	public Cancellable sendMail(final Mail email);

	public Cancellable sendMail(final String subject, final String textBody, final String recipient);

	public Cancellable sendMail(final String subject, final Mail.Body body, final String recipient);
}
