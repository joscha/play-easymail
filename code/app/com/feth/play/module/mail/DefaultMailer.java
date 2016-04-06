package com.feth.play.module.mail;

import javax.inject.Inject;
import javax.inject.Singleton;

import akka.actor.ActorSystem;
import play.Configuration;
import play.libs.mailer.MailerClient;

@Singleton
public class DefaultMailer extends Mailer {
	@Inject
	public DefaultMailer(final Configuration configuration, final MailerClient mailClient,
			final ActorSystem actorSystem) {
		super(configuration, configuration.getConfig(Configs.CONFIG_BASE), mailClient, actorSystem);
	}

}
