package com.feth.play.module.mail;

import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import com.google.inject.assistedinject.Assisted;
import com.typesafe.config.Config;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class Mailer implements IMailer {

	public static class Configs {
		public static final String MAILER = "play-easymail/";
		public static final String CONFIG_BASE = "play-easymail";
	}

	public class SettingKeys {
		public static final String FROM = "from";
		public static final String FROM_EMAIL = "email";
		public static final String FROM_NAME = "name";
		public static final String INCLUDE_XMAILER_HEADER = "includeXMailerHeader";
		public static final String DELAY = "delay";
		private static final String VERSION = "version";
	}

	protected final FiniteDuration delay;

	protected final String sender;

	protected final boolean includeXMailerHeader;

	protected final Config configuration;

	protected final MailerClient mailClient;

	protected final ActorSystem actorSystem;

	private String getVersion() {
		return getConfiguration().getString(SettingKeys.VERSION);
	}

	private Config getConfiguration() {
		return configuration.getConfig(Configs.CONFIG_BASE);
	}

	public static String getEmailName(final String email, final String name) {
		if (email == null || email.trim().isEmpty()) {
			throw new RuntimeException("email must not be null");
		}
		final StringBuilder sb = new StringBuilder();
		final boolean hasName = name != null && !name.trim().isEmpty();
		if (hasName) {
			sb.append("\"");
			sb.append(name);
			sb.append("\" <");
		}

		sb.append(email);

		if (hasName) {
			sb.append(">");
		}

		return sb.toString();
	}

	@Inject
	public Mailer(final Config configuration, @Assisted final Config mailerConfig,
				  final MailerClient mailClient, final ActorSystem actorSystem) {
		this.configuration = configuration;
		this.mailClient = mailClient;
		this.actorSystem = actorSystem;

		this.delay = Duration.create(mailerConfig.getLong(SettingKeys.DELAY), TimeUnit.SECONDS);

		final Config fromConfig = mailerConfig.getConfig(SettingKeys.FROM);
		this.sender = getEmailName(fromConfig.getString(SettingKeys.FROM_EMAIL),
				fromConfig.getString(SettingKeys.FROM_NAME));

		this.includeXMailerHeader = mailerConfig.getBoolean(SettingKeys.INCLUDE_XMAILER_HEADER);
	}

	public static class Mail extends Email {

		public static class HtmlBody extends Body {

			public HtmlBody(final String text) {
				super(null, text);
			}

		}

		public static class TxtBody extends Body {

			public TxtBody(final String text) {
				super(text, null);
			}

		}

		public static class Body {
			private final String html;
			private final String text;
			private final boolean isHtml;
			private final boolean isText;

			public Body(final String text) {
				this(text, null);
			}

			public Body(final String text, final String html) {
				this.isHtml = html != null && !html.trim().isEmpty();
				this.isText = text != null && !text.trim().isEmpty();

				if (!this.isHtml && !this.isText) {
					throw new RuntimeException("Text and HTML cannot both be empty or null");
				}
				this.html = (this.isHtml) ? html : null;
				this.text = (this.isText) ? text : null;
			}

			public boolean isHtml() {
				return this.isHtml;
			}

			public boolean isText() {
				return this.isText;
			}

			public boolean isBoth() {
				return isText() && isHtml();
			}

			public String getHtml() {
				return this.html;
			}

			public String getText() {
				return this.text;
			}
		}

		public Mail(final String subject, final String body, final String recipient) {
			this(subject, new Body(body), new String[] { recipient });
		}

		public Mail(final String subject, final String body, final List<String> recipient) {
			this(subject, new Body(body), recipient);
		}

		public Mail(final String subject, final Body body, final String recipient) {
			this(subject, body, new String[] { recipient });
		}

		public Mail(final String subject, final Body body, final String[] recipients) {
			this(subject, body, Arrays.asList(recipients));
		}

		public Mail(final String subject, final Body body, final List<String> recipients) {
			this(subject, body, recipients, null, null, null, null);
		}

		public Mail(final String subject, final Body body, final List<String> recipients, final List<String> cc) {
			this(subject, body, recipients, cc, null, null, null);
		}

		public Mail(final String subject, final Body body, final List<String> recipients, final List<String> cc,
				final List<String> bcc) {
			this(subject, body, recipients, cc, bcc, null, null);
		}

		public Mail(final String subject, final Body body, final List<String> recipients, final List<String> cc,
				final List<String> bcc, final List<String> replyTo) {
			this(subject, body, recipients, cc, bcc, null, replyTo);
		}

		public Mail(final String subject, final Body body, final List<String> recipients,
				final Map<String, List<String>> customHeaders) {
			this(subject, body, recipients, null, null, customHeaders, null);
		}

		public Mail(final String subject, final Body body, final List<String> recipients, final List<String> cc,
				final Map<String, List<String>> customHeaders) {
			this(subject, body, recipients, cc, null, customHeaders, null);
		}

		public Mail(final String subject, final Body body, final List<String> recipients, final List<String> cc,
				final List<String> bcc, final Map<String, List<String>> customHeaders) {
			this(subject, body, recipients, cc, bcc, customHeaders, null);
		}

		public Mail(final String subject, final Body body, final List<String> recipients, final List<String> cc,
				final List<String> bcc, final Map<String, List<String>> customHeaders, final List<String> replyTo) {
			if (subject == null || subject.trim().isEmpty()) {
				throw new RuntimeException("Subject must not be null or empty");
			}
			this.setSubject(subject);

			if (body == null) {
				throw new RuntimeException("Body must not be null or empty");
			}
			if (body.isText())
				this.setBodyText(body.getText());
			if (body.isHtml())
				this.setBodyHtml(body.getHtml());

			if (recipients == null || recipients.size() == 0) {
				throw new RuntimeException("There must be at least one recipient");
			}
			this.setTo(recipients);

			if (cc != null && cc.size() > 0)
				this.setCc(cc);
			if (bcc != null && bcc.size() > 0)
				this.setBcc(bcc);

			if (customHeaders != null) {
				for (final Entry<String, List<String>> entry : customHeaders.entrySet()) {
					final String headerName = entry.getKey();
					for (final String headerValue : entry.getValue()) {
						this.addHeader(headerName, headerValue);
					}
				}
			}
			if (replyTo != null && replyTo.size() > 0)
				this.setReplyTo(replyTo);
		}

		public Body getBody() {
			return new Body(getBodyText(), getBodyHtml());
		}
	}

	private class MailJob implements Runnable {

		private Mail mail;

		public MailJob(final Mail m) {
			this.mail = m;
		}

		@Override
		public void run() {
			if (Mailer.this.includeXMailerHeader) {
				this.mail.addHeader("X-Mailer", Configs.MAILER + getVersion());
			}
			mailClient.send(this.mail);
		}

	}

	public interface MailerFactory {
		public Mailer create(final Config mailerConfig);
	}

	@Override
	public Cancellable sendMail(final Mail email) {
		email.setFrom(this.sender);
		return actorSystem.scheduler().scheduleOnce(this.delay, new MailJob(email), actorSystem.dispatcher());
	}

	@Override
	public Cancellable sendMail(final String subject, final String textBody, final String recipient) {
		final Mail mail = new Mail(subject, new Mail.Body(textBody), Arrays.asList(recipient));
		return sendMail(mail);
	}

	@Override
	public Cancellable sendMail(final String subject, final Mail.Body body, final String recipient) {
		final Mail mail = new Mail(subject, body, Arrays.asList(recipient));
		return sendMail(mail);
	}
}
