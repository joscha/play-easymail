package controllers;

import com.feth.play.module.mail.Mailer;
import com.feth.play.module.mail.Mailer.Mail;
import com.feth.play.module.mail.Mailer.Mail.Body;

import java.io.File;

import org.apache.commons.mail.EmailAttachment;

import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.libs.mailer.MailerPlugin;
import play.mvc.*;
import play.Play;
import views.html.index;
import static play.data.Form.form;

public class Application extends Controller {

    public static class MailMe {
        @Email
        @Required
        public String email;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
        
    }

    private static final Form<MailMe> FORM = form(MailMe.class);

    public static Result index() {
        return ok(index.render(FORM));
    }

    public static Result sendMail() {
        final Form<MailMe> filledForm = FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(index.render(filledForm));
        } else {
            final String email = filledForm.get().email;
            final Body body = new Body(
                    views.txt.email.body.render().toString(),
                    views.html.email.body.render().toString()
            );
            final Mailer defaultMailer = Mailer.getDefaultMailer();

            {
                // simple usage
                defaultMailer.sendMail("play-easymail | it works!", body, email);
            }

            {
                // advanced usage
                final Mail customMail = new Mail("play-easymail | advanced", body, new String[]{ email });
                customMail.addHeader("Reply-To", email);
                customMail.addAttachment("attachment.pdf", Play.application().getFile("conf/sample.pdf"));
                byte[] data = "data".getBytes();
                customMail.addAttachment("data.txt", data, "text/plain", "A simple file", EmailAttachment.INLINE);
                defaultMailer.sendMail(customMail);
            }

            flash("message", "2 mails to '" + email + "' have been sent successfully!");
            return redirect(routes.Application.index());
        }
    }

}
