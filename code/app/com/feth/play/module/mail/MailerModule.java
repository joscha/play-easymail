package com.feth.play.module.mail;

import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

public class MailerModule extends Module {

	@Override
	public Seq<Binding<?>> bindings(Environment env, Configuration conf) {
		return seq(bind(DefaultMailer.class).toSelf());
	}

}
