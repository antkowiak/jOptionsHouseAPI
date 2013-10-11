jOptionsHouseAPI
================

Java Library for using the OptionsHouse trading API.  This library contains some convenient
classes in Java that allow you to access the OptionsHouse API trading platform from
within a Java application.


#### Dependencies ####
* GSON library for parsing JSON in Java (https://code.google.com/p/google-gson/)
* Java software development kit (http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html)


#### Notes and Dislaimers ####
* OptionsHouse requires you to sign their API agreement before using the
  trading API.
* OptionsHouse asks you to only send 1 API message per second.  They reserve the 
  right to revoke your API access if you bombard their servers with requests (in
  excess of 1 message per second.)
* This software is provided AS-IS with NO WARRANTY.  Please practice with a virtual trading
  account before using it for real.  Please let me know of any bugs you find!  I have done
  my best to try to make the library accurate and error-free, but I can't provide any
  guarantees!

#### Bugs ####
* No known bugs at the time of release.
* If you find bugs, please report them to Ryan Antkowiak (antkowiak@gmail.com)
  or raise an issue in GitHub.


#### Version 1.0.0 ####
* Initial release

