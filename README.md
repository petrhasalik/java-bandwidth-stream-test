java-bandwidth-stream-test
==========================

Java test project. The task was to develop an InputStream wrapper that is able to throttle the bandwidth that an input stream uses based on specific times.

e.g. between 8am - 5pm and  use up to 64kb, between 5pm - 12pm use up to 1mb, between 12pm - 8am use unlimited.

- The bandwidth available needs to be shared between multiple streams
- Transitioning between time periods should work seamlessly
- Unit tests should use TestNG
- Logging should use Slf4j
- Only the Apache Commons, Guava, Javolution-core, Joda time libraries may be utilised
- Must be Java 1.7 compatible


