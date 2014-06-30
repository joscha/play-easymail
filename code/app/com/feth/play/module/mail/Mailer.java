package com.feth.play.module.mail;

import akka.actor.Cancellable;
import com.feth.play.module.mail.Mailer.Mail.Body;
import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;
import play.Configuration;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class Mailer {

    private static final String MAILER = "play-easymail/";

    private static final String CONFIG_BASE = "play-easymail";

    public static class SettingKeys {
        public static final String FROM = "from";
        public static final String FROM_EMAIL = "email";
        public static final String FROM_NAME = "name";
        public static final String DELAY = "delay";
        private static final String VERSION = "version";
    }

    private final MailerPlugin plugin;

    private final FiniteDuration delay;

    private final String sender;

    private static Mailer instance = null;

    public static Mailer getDefaultMailer() {
        if (instance == null) {
            instance = new Mailer(getConfiguration());
        }
        return instance;
    }

    private static String getVersion() {
        return getConfiguration().getString(SettingKeys.VERSION);
    }

    private static Configuration getConfiguration() {
        return play.Play.application().configuration().getConfig(CONFIG_BASE);
    }

    public static Mailer getCustomMailer(final Configuration config) {
        return new Mailer(config);
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

    protected Mailer(final Configuration config) {
        plugin = play.Play.application().plugin(MailerPlugin.class);
        delay = Duration.create(config.getLong(SettingKeys.DELAY, 1L), TimeUnit.SECONDS);

        final Configuration fromConfig = config.getConfig(SettingKeys.FROM);
        sender = getEmailName(fromConfig.getString(SettingKeys.FROM_EMAIL),
                fromConfig.getString(SettingKeys.FROM_NAME));
    }

    public static class Mail {

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
                    throw new RuntimeException(
                            "Text and HTML cannot both be empty or null");
                }
                this.html = (this.isHtml) ? html : null;
                this.text = (this.isText) ? text : null;
            }

            public boolean isHtml() {
                return isHtml;
            }

            public boolean isText() {
                return isText;
            }

            public boolean isBoth() {
                return isText() && isHtml();
            }

            public String getHtml() {
                return html;
            }

            public String getText() {
                return text;
            }
        }

        private final String subject;
        private final String[] recipients;
        private String from;
        private final Body body;
        private final Map<String, List<String>> customHeaders;

        public Mail(final String subject, final Body body,
                    final String[] recipients) {
            this(subject, body, recipients, null);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients,
                    final Map<String, List<String>> customHeaders) {
            if (subject == null || subject.trim().isEmpty()) {
                throw new RuntimeException("Subject must not be null or empty");
            }
            this.subject = subject;

            if (body == null) {
                throw new RuntimeException("Body must not be null or empty");
            }

            this.body = body;

            if (recipients == null || recipients.length == 0) {
                throw new RuntimeException(
                        "There must be at least one recipient");
            }
            this.recipients = recipients;

            if (customHeaders != null) {
                this.customHeaders = customHeaders;
            } else {
                this.customHeaders = new HashMap<String, List<String>>(1);
            }
        }

        public String getSubject() {
            return subject;
        }

        public String[] getRecipients() {
            return recipients;
        }

        public String getFrom() {
            return from;
        }

        private void setFrom(final String from) {
            this.from = from;
        }

        public Body getBody() {
            return body;
        }

        public Map<String, List<String>> getCustomHeaders() {
            return customHeaders;
        }

        public void addCustomHeader(String name, String... values) {
            this.customHeaders.put(name, Arrays.asList(values));
        }
    }

    private class MailJob implements Runnable {

        private Mail mail;

        public MailJob(final Mail m) {
            mail = m;
        }

        @Override
        public void run() {
            final MailerAPI api = plugin.email();

            api.setSubject(mail.getSubject());
            api.setRecipient(mail.getRecipients());
            api.setFrom(mail.getFrom());
            api.addHeader("X-Mailer", MAILER + getVersion());

            for (final Entry<String, List<String>> entry : mail
                    .getCustomHeaders().entrySet()) {
                final String headerName = entry.getKey();
                for (final String headerValue : entry.getValue()) {
                    api.addHeader(headerName, headerValue);
                }
            }

            if (mail.getBody().isBoth()) {
                // sends both text and html
                api.send(mail.getBody().getText(), mail.getBody().getHtml());
            } else if (mail.getBody().isText()) {
                // sends text/text
                api.send(mail.getBody().getText());
            } else {
                // if(mail.isHtml())
                // sends html
                api.sendHtml(mail.getBody().getHtml());
            }
        }

    }

    public Cancellable sendMail(final Mail email) {
        email.setFrom(sender);
        return Akka
                .system()
                .scheduler()
                .scheduleOnce(delay, new MailJob(email),
                        Akka.system().dispatcher());
    }

    public Cancellable sendMail(final String subject, final String textBody, final String recipient) {
        final Mail mail = new Mail(subject, new Body(textBody), new String[]{recipient});
        return sendMail(mail);
    }

    public Cancellable sendMail(final String subject, final Body body,
                                final String recipient) {
        final Mail mail = new Mail(subject, body, new String[]{recipient});
        return sendMail(mail);
    }
}
