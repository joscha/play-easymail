package modules;

import com.feth.play.module.mail.IMailer;
import com.feth.play.module.mail.Mailer;
import com.feth.play.module.mail.Mailer.MailerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class CustomMailerBinder extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(IMailer.class, Mailer.class).build(MailerFactory.class));
	}

}
