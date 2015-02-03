jOptionsHouseAPI
================

IMPORTANT!!! - OptionsHouse recently merged with TradeMonster.  OH informed me that the
old Options House API would be deprecated!  This code may no longer be relevant and
may not work anymore.  I plan on developing a new Java library soon that will work
with the new TradeMonster API.

Java Library for using the OptionsHouse trading API.  This library contains some convenient
classes in Java that allow you to access the OptionsHouse API trading platform from
within a Java application, using only a few simple lines of code.  See the files in
the "examples" subdirectory for examples on how to use the API.


#### Dependencies ####
* GSON library for parsing JSON in Java (https://code.google.com/p/google-gson/)
* Java software development kit (http://www.oracle.com/technetwork/java/javase/downloads/)


#### Notes and Disclaimers ####
* OptionsHouse requires you to sign their API agreement before using the
  trading API.
* OptionsHouse asks you to only send 1 API message per second.  They reserve the 
  right to revoke your API access if you bombard their servers with requests (in
  excess of 1 message per second.)
* I think OptionsHouse requires an account balance of $30,000 before granting API access.
* This software is provided AS-IS with NO WARRANTY.  Please practice with a virtual trading
  account before using it for real.  Please let me know of any bugs you find!  I have done
  my best to try to make the library accurate and error-free, but I can't provide any
  guarantees!


#### Tips ####
* You can turn on very verbose debugging by calling: OptionsHouseUtilities.setDebugMsgTracing(true);
* Beware that verbose debugging will print almost EVERYTHING to the console (including your
  login credentials you send along with the OhLogin().
* In order to adhere to the one-second-between-messages requirement, you may want to implement
  some kind of synchronized queue of "Oh" objects.  I may try to include this in a future release.
* Use the OhKeepAlive class to keep a session alive.  I have discovered that sometimes OptionsHouse will
expire a session, even if you are regularly sending other messages.


#### Feedback / Encouragement ####
* If you found this library useful, give me a shout out and send me an email!
* If you don't have an OptionsHouse brokerage account yet, and you are thinking of
  signing up, you can sign up using my referral link (http://oh.tellapal.com/a/clk/NXxZx)
  if you are so inclined. Signing up via my refreral link instead of the main OptionsHouse
  website will earn a small referral bonus for me.


#### Version 1.0.3 ####
* Added getAccountNumberForAccountId() to OhAccountList in order to provide
  access to account numbers.  (When OptionsHouse changed clearing firms from
  Penson to Apex, they re-numbered the accounts.  This method returns the
  account number that was newly given when switched over to Apex, I believe.)

#### Version 1.0.2 ####
* Fixes #12 - OhAccountList Bug - Data not being property retrieved for first
  account in the list

#### Version 1.0.1 ####
* Fixes #11 - Works around a bug in the OptionsHouse API for getting
  account positions when the account has exactly one position.
* Small internal changes of comments and visibility of internal class members.


#### Version 1.0.0 ####
* Initial release

