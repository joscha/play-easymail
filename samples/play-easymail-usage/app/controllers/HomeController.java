package controllers;

import org.apache.commons.mail.EmailAttachment;

import com.feth.play.module.mail.DefaultMailer;
import com.feth.play.module.mail.Mailer.Mail;
import com.feth.play.module.mail.Mailer.Mail.Body;
import com.feth.play.module.mail.Mailer.MailerFactory;
import com.google.inject.Inject;

import play.Environment;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class HomeController extends Controller {
	private final Environment env;

	private final DefaultMailer defaultMailer;

	private final FormFactory formFactory;

	private final MailerFactory customMailer;

	private final Form<MailMe> FORM;

	public static class MailMe {
		@Email
		@Required
		private String email;

		public String getEmail() {
			return this.email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

	}

	@Inject
	public HomeController(final Environment env, final DefaultMailer defaultMailer, final FormFactory formFactory,
			final MailerFactory mailerFactory) {
		this.env = env;
		this.defaultMailer = defaultMailer;
		this.formFactory = formFactory;
		this.customMailer = mailerFactory;
		FORM = formFactory.form(MailMe.class);
	}

	public Result index() {
		return ok(index.render(FORM));
	}

	public Result sendMail() {
		final Form<MailMe> filledForm = FORM.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(index.render(filledForm));
		} else {
			final String email = filledForm.get().email;
			final Body body = new Body(views.txt.email.body.render().toString(),
					views.html.email.body.render().toString());

			{
				// simple usage
				defaultMailer.sendMail("play-easymail | it works!", body, email);
			}

			{
				// advanced usage
				final Mail customMail = new Mail("play-easymail | advanced", body, new String[] { email });
				customMail.addHeader("Reply-To", email);
				customMail.addAttachment("attachment.pdf", env.getFile("conf/sample.pdf"));
				byte[] data = "data".getBytes();
				customMail.addAttachment("data.txt", data, "text/plain", "A simple file", EmailAttachment.INLINE);
				defaultMailer.sendMail(customMail);
			}

			flash("message", "2 mails to '" + email + "' have been sent successfully!");
			return redirect(routes.HomeController.index());
		}
	}

}
