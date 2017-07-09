package com.feth.play.module.mail;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import play.libs.mailer.MailerClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultMailer extends Mailer {
	@Inject
	public DefaultMailer(final Config configuration, final MailerClient mailClient,
						 final ActorSystem actorSystem) {
		super(configuration, configuration.getConfig(Configs.CONFIG_BASE), mailClient, actorSystem);
	}

}
