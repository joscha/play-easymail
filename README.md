# [![Build Status](https://travis-ci.org/joscha/play-easymail.png?branch=master)](https://travis-ci.org/joscha/play-easymail) play-easymail - an extension to the Typesafe mailer plugin for Play! Framework 2.x (Java)

This plugin uses concepts from [Play20StartApp][] to make sending emails (text, html, mixed) easier. A sample is included.

#### Version information
**play-easymail currently needs Play! Framework 2.x**

play-easymail is cross-tested with Java 1.6, Java 1.7 (Up to `0.6.x`) and Java 1.8 (from `0.7.0`)

* The `master` branch contains the code for Play! Framework 2.4.x (play-easymail version `0.7.0` and up).
* The `2.3.x` branch contains the code for 2.3.x (play-easymail version `0.6 - 0.6.x`).
* The `2.2.x` branch contains the code for 2.2.x (play-easymail version `0.5 - 0.5.x`).
* The `2.1.x` branch contains the code for 2.1.x (play-easymail version `0.2 - 0.3.x`).
* The `2.0.x` is a maintenance branch for the 2.0.x series of Play! Framework (play-easymail version `0.1`).

## Features
Sending email through an easy-to-use API and allowing for text-only, html-only and mixed emails. Preferably based on Play! templates (see sample).

## Code sample
```java
Mailer.getDefaultMailer().sendMail(
    "Your subject",
    "Your text body",
    "recipient@email.com"
);
```
You can also have a look at the [sample](samples/play-easymail-usage/app/controllers/Application.java) for a more advanced use-case (content from templates and setting custom headers).

## Versions
* **0.7.0** [preview on 2015-06-09, release 2015-10-31]
  * Support for play 2.4.x (thanks @vmouta, @mkurz)
  * Use of new play-mailer-3.x (thanks @vmouta, @mkurz)
  * ATTENTION: This is for Play 2.4 - if you have Play 2.3 or older, use a `0.6.x` version.
* **0.6.6** [2014-10-28]
  * Fix delay setting location (thanks @mkurz)
  * Attachment support (thanks @mkurz)
* **0.6.5** [2014-10-20]
  * Allow disabling X-Mailer header (thanks @mkurz)
  * Allow setting CC and BCC (thanks @mkurz)
* **0.6.4** [2014-07-28]
  * Use Java 6 for releasing binaries (thanks @rui-ferreira)
* **0.6.3** [2014-07-05]
  * Added `setReplyTo` method (thanks @cornelcroi)
* **0.6.2** [2014-06-30]
  * Fix `addCustomHeader` method
  * Add `sendMail` convenience method for text-only mails
  * Add to sample: show advanced Mail example (add Reply-To header)
* **0.6.1** [2014-06-29]
  * Add Scala 2.10.x binary to repository
  * Test against Scala 2.10.x
  * ATTENTION: Binaries are not published in ivy style any more, please update your resolver URLs (see [#189](https://github.com/joscha/play-authenticate/issues/189))
* **0.6.0** [2014-06-10]
  * Version for Play 2.3.x
* **0.5.2** [2014-07-05]
  * Added `setReplyTo` method (thanks @cornelcroi)
  * Last version for Play 2.2.x
* **0.5.1** [2014-07-01]
  * Backport fix for `addCustomHeader` method
* **0.5** [2013-10-23]
  * First version for Play 2.2.x
* **0.3** [2013-09-01]
  * A mail can now have custom headers (see issue #6) - thanks @jtammen
* **0.2** [2013-02-06]
  * Version for 2.1.0
* **0.1** [2012-07-09]
  * Initial release

## License

Copyright (c) 2012-2015 Joscha Feth

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.


[Play20StartApp]: https://github.com/yesnault/Play20StartApp
