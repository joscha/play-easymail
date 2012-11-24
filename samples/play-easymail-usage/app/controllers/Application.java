package controllers;

import com.feth.play.module.mail.Mailer;
import com.feth.play.module.mail.Mailer.Mail.Body;

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

			final Body body = new Body(views.txt.email.body.render().toString(),
					views.html.email.body.render().toString());
			Mailer.getDefaultMailer().sendMail("play-easymail | it works!",
					body, email);

			flash("message", "Mail to '" + email
					+ "' has been sent successfully!");
			return redirect(routes.Application.index());
		}
	}

}