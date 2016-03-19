package modules;

import com.feth.play.module.mail.Mailer;
import com.feth.play.module.mail.MailerImpl;
import com.feth.play.module.mail.MailerImpl.MailerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class CustomMailerBinder extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(Mailer.class, MailerImpl.class).build(MailerFactory.class));
	}

}
