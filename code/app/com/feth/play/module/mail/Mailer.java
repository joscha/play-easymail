package com.feth.play.module.mail;

import akka.actor.Cancellable;
import com.feth.play.module.mail.Mailer.Mail.Body;
import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;
import play.Configuration;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.io.File;
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
        public static final String INCLUDE_XMAILER_HEADER = "includeXMailerHeader";
        public static final String DELAY = "delay";
        private static final String VERSION = "version";
    }

    private final MailerPlugin plugin;

    private final FiniteDuration delay;

    private final String sender;

    private final boolean includeXMailerHeader;

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

        includeXMailerHeader = config.getBoolean(SettingKeys.INCLUDE_XMAILER_HEADER, true);
    }

    public static class Mail {


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

        public static class Attachment {
            private byte[] data;
            private String mimeType;
            private File file;
            private String name;
            private String description;
            private String disposition;

            public Attachment(final String name, final byte[] data, final String mimeType) {
                this(name, data, mimeType, null);
            }

            public Attachment(final String name, final byte[] data, final String mimeType, final String description) {
                this(name, data, mimeType, description, null);
            }

            public Attachment(final String name, final byte[] data, final String mimeType, final String description, final String disposition) {
                if (name == null || name.trim().isEmpty()) {
                    throw new RuntimeException("Name must not be null or empty");
                }
                if (data == null) {
                    throw new RuntimeException("Data must not be null");
                }
                this.name = name;
                this.data = data;
                this.mimeType = mimeType;
                this.description = description;
                this.disposition = disposition;
            }

            public Attachment(final String name, final File file) {
                this(name, file, null);
            }

            public Attachment(final String name, final File file, String description) {
                this(name, file, description, null);
            }

            public Attachment(final String name, final File file, final String description, final String disposition) {
                if (name == null || name.trim().isEmpty()) {
                    throw new RuntimeException("Name must not be null or empty");
                }
                if (file == null) {
                    throw new RuntimeException("File must not be null");
                }
                this.name = name;
                this.file = file;
                this.description = description;
                this.disposition = disposition;
            }

            public byte[] getData() {
                return this.data;
            }

            public String getMimeType() {
                return this.mimeType;
            }

            public File getFile() {
                return this.file;
            }

            public String getName() {
                return this.name;
            }

            public String getDescription() {
                return this.description;
            }

            public String getDisposition() {
                return this.disposition;
            }
        }

        private final String subject;
        private final String[] recipients;
        private final String[] cc;
        private final String[] bcc;
        private String from;
        private final Body body;
        private String replyTo;

        private final Map<String, List<String>> customHeaders;
        private final List<Attachment> attachments = new ArrayList<Attachment>(1);

        public Mail(final String subject, final Body body,
                    final String[] recipients) {
            this(subject, body, recipients, null, null, null, null);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients, final String[] cc) {
            this(subject, body, recipients, cc, null, null, null);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients, final String[] cc, final String[] bcc) {
            this(subject, body, recipients, cc, bcc, null, null);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients, final String replyTo) {
            this(subject, body, recipients, null, null, null, replyTo);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients, final String[] cc, final String replyTo) {
            this(subject, body, recipients, cc, null, null, replyTo);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients, final String[] cc, final String[] bcc, final String replyTo) {
            this(subject, body, recipients, cc, bcc, null, replyTo);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients,
                    final Map<String, List<String>> customHeaders) {
            this(subject, body, recipients, null, null, customHeaders, null);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients, final String[] cc,
                    final Map<String, List<String>> customHeaders) {
            this(subject, body, recipients, cc, null, customHeaders, null);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients, final String[] cc, final String[] bcc,
                    final Map<String, List<String>> customHeaders) {
            this(subject, body, recipients, cc, bcc, customHeaders, null);
        }

        public Mail(final String subject, final Body body,
                    final String[] recipients, final String[] cc, final String[] bcc,
                    final Map<String, List<String>> customHeaders, final String replyTo) {
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

            this.cc = cc;
            this.bcc = bcc;

            if (customHeaders != null) {
                this.customHeaders = customHeaders;
            } else {
                this.customHeaders = new HashMap<String, List<String>>(1);
            }

            if (replyTo != null) {
                this.replyTo = replyTo;
            }
        }


        public String getSubject() {
            return subject;
        }

        public String[] getRecipients() {
            return recipients;
        }

        public String[] getCc() {
            return cc;
        }

        public String[] getBcc() {
            return bcc;
        }

        public String getFrom() {
            return from;
        }

        private void setFrom(final String from) {
            this.from = from;
        }

        public String getReplyTo() {
            return replyTo;
        }

        public void setReplyTo(final String replyTo) {
            this.replyTo = replyTo;
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

        public List<Attachment> getAttachments() {
            return attachments;
        }

        public void addAttachment(final Attachment... attachments) {
            this.attachments.addAll(Arrays.asList(attachments));
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
            if (mail.getCc() != null) {
                api.setCc(mail.getCc());
            }
            if (mail.getBcc() != null) {
                api.setBcc(mail.getBcc());
            }
            api.setFrom(mail.getFrom());
            if (includeXMailerHeader) {
                api.addHeader("X-Mailer", MAILER + getVersion());
            }

            for (final Entry<String, List<String>> entry : mail
                    .getCustomHeaders().entrySet()) {
                final String headerName = entry.getKey();
                for (final String headerValue : entry.getValue()) {
                    api.addHeader(headerName, headerValue);
                }
            }
            if (mail.getReplyTo() != null) {
                api.setReplyTo(mail.getReplyTo());
            }
            for (final Mail.Attachment attachment : mail.getAttachments()) {
                if (attachment.getData() != null) {
                    api.addAttachment(attachment.getName(), attachment.getData(), attachment.getMimeType(), attachment.getDescription(), attachment.getDisposition());
                } else {
                    api.addAttachment(attachment.getName(), attachment.getFile(), attachment.getDescription(), attachment.getDisposition());
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
