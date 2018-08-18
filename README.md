**Update**, 17 August 2018.

Although this project is technically dead, in light of certain events, I have patched and returned InflatableDonkey to a functional state.

Please don't take this an indication that this project is being resurrected, it is not. I apologise for this.

Once again, I would like to clarify that InflatableDonkey is only to be used to access backups that you own.


### What is it?
Java proof of concept iOS9+ iCloud backup retrieval tool. Educational/ personal backup usage only.

### Build
Requires [Java 8 JRE/ JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Maven](https://maven.apache.org).

[Download](https://github.com/horrorho/InflatableDonkey/archive/master.zip), extract and navigate to the InflatableDonkey-master folder:

```
~/InflatableDonkey-master $ mvn package
```
The executable Jar is located at /target/InflatableDonkey.jar

### Usage
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar --help
usage: InflatableDonkey (<token> | <appleid> <password>) [OPTION]...
iOS9 iCloud backup retrieval proof of concept tool.

 -o,--folder <string>            Output folder.
 -d,--device <id/s>              Device filter/s. Leave empty to select
                                 all devices/ disable user selection.
 -s,--snapshot <int/s>           Snapshot filter/s, 0 = first, 1 = next
                                 etc. Reverse selection with -1 = last, -2
                                 = previous etc.
    --domain <string/s>          Domain filter/s.
    --relative-path <string/s>   Relative path filter/s.
    --extension <string/s>       File extension filter/s.
    --item-type <item-type/s>    Only download the specified item type/s:
                                 ADDRESS_BOOK(addressbook.sqlitedb)
                                 CALENDAR(calendar.sqlitedb)
                                 CALL_HISTORY(call_history.db)
                                 MOVIES(.mov .mp4 .avi)
                                 NOTES(notes)
                                 PHOTOS(.jpg .jpeg)
                                 PNG(.png)
                                 SMS(sms.db)
                                 VOICEMAILS(voicemail)
    --size-max <kB>              Maximum file size.
    --size-min <kB>              Minimum file size.
    --date-min <date>            Minimum created timestamp.
    --date-max <date>            Maximum created timestamp.
    --mod-min <date>             Minimum last-modified timestamp.
    --mod-max <date>             Maximum last-modified timestamp.
    --mode <mode>                Data Protection decryption mode: AUTO CBC
                                 XTS OFF (AUTO).
    --threads <int>              Number of concurrent download threads.
                                 Default: 4.
    --turbo <int>                Engine thread multiplier for small
                                 assets. Default: 2.
    --snapshots                  List device/ snapshot information and
                                 exit.
    --domains                    List domains/ file count for the selected
                                 snapshot/s and exit.
    --token                      Display dsPrsID:mmeAuthToken and exit.
    --help                       Display this help and exit.

Dates are ISO format e.g. 2000-12-31. Filters are case insensitive.
Pass multiple argument values separated by spaces e.g. --extension png jpg
```

AppleId/ password.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur
```

DsPrsID/mmeAuthToken. Preferable for consecutive runs as repeated appleId/ password authentication over short periods may trip anti-flooding/ security controls.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar 1234567890:AQAAAABWJVgBHQvCSr4qPXsjQN9M9dQw9K7w/sB=
```

Print DsPrsID/mmeAuthToken and exit.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --token
```

With HTTPS proxy.
```
~/InflatableDonkey-master/target $ java -Dhttps.proxyHost=HOST -Dhttps.proxyPort=PORT -jar InflatableDonkey.jar elvis@lives.com uhhurhur --token
```

List devices/ snapshots.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --snapshots
```

By default the user is presented with a device/ snapshot selection screen. You can also pass in device/ snapshot details manually. Devices are referenced by their UUIDs or part of, snapshots by their indices.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --device B648BD20296F0C6D20DFB2F0A52C8314AF5FCEC7 --snapshot 0
```

There are a number of filters available. To download thumbs from WhatsApp:
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --domain whatsapp --extension thumb
```

Download a selection of photos (those ending in 0.jpg and 5.jpg) from the CameraRollDomain
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --domain camera --extension 0.jpg 5.jpg
```

List domains and the file count for each domain, then exit.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --domains
```

Download all files from the HomeDomain.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --domain homedomain
```

The are two data protection decryption modes but the detection algorithm is only about 90% accurate. If you experience file corruption please try setting the mode manually with either --mode CBC or --mode XTS.

For example CBC mode.
```
~/InflatableDonkey-master/target $ java -jar InflatableDonkey.jar elvis@lives.com uhhurhur --mode CBC
```

For further information please refer to the comments/ code in [Main](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/java/com/github/horrorho/inflatabledonkey/Main.java). Running the tool will detail the client/ server responses for each step, including headers/ protobufs. You can play with [logback.xml](https://github.com/horrorho/InflatableDonkey/blob/master/src/main/resources/logback.xml) and adjust the Apache HttpClient header/ wire logging levels.

### Additional notes
[CloudKit Notes](https://github.com/horrorho/InflatableDonkey/blob/master/CloudKit.md) describes some of the low level mechanics we have discovered over the last couple of weeks.

### Credits

[Yaldo425](https://github.com/Yaldo425) and [AsifMehmoood](https://github.com/asifmehmoood) - for supplying iOS11/ iOS 10 backups to work with, because as odd as it sounds I don't actually own an iPhone.

[Jirmi](https://github.com/jirmi) - for various suggestions and Protobuf assistance.

[Louti](https://github.com/Louti) - for additional insights on cryptographic key handling.

[ItsASmallWorld](https://github.com/ItsASmallWorld) - for deciphering key client/ server interactions and assisting with Protobuf definitions.

Oleksii K - for cryptographical assistance, before he was mysteriously abducted by aliens and subjected to various probing experiments.

[devzero0](https://github.com/devzero0) for creating a [Python implementation](https://github.com/devzero0/iOS9_iCloud_POC) of InflatableDonkey, sadly now outdated.

[hackappcom](https://github.com/hackappcom) for the venerable [iLoot](https://github.com/hackappcom/iloot).

[iphone-dataprotection](https://code.google.com/p/iphone-dataprotection/) highly influential and brilliant work.

My cat - for being my cat and keeping me company.

There have been some contributors who, rather like vampires, prefer the cover of darkness. You know who you are and thank you!

I've also probably forgotten others. If so I'm sorry and thank you too!
