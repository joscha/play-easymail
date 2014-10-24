package controllers;

import com.feth.play.module.mail.Mailer;
import com.feth.play.module.mail.Mailer.Mail.Attachment;
import com.feth.play.module.mail.Mailer.Mail.Body;

import java.io.File;

import org.apache.commons.mail.EmailAttachment;

import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.mvc.*;

import views.html.index;

import static play.data.Form.form;

public class Application extends Controller {

    public static class MailMe {
        @Email
        @Required
        public String email;
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
                final Mailer.Mail customMail = new Mailer.Mail("play-easymail | advanced", body, new String[]{ email });
                customMail.addCustomHeader("Reply-To", email);
                customMail.addAttachment(new Attachment("attachment.pdf", new File("/some/path/attachment.pdf")));
                byte[] data = "data".getBytes();
                customMail.addAttachment(new Attachment("data.txt", data, "text/plain", "A simple file", EmailAttachment.INLINE));
                defaultMailer.sendMail(customMail);
            }

            flash("message", "2 mails to '" + email + "' have been sent successfully!");
            return redirect(routes.Application.index());
        }
    }

}
